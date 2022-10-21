package com.tin.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.tin.entity.Account;
import com.tin.entity.Order;
import com.tin.service.AccountService;
import com.tin.service.OrderService;
import com.tin.service.PaymentService;

import javassist.expr.NewArray;
          
@Controller
public class OrderController {
	
	public static final String URL_PAYPAL_SUCCESS = "pay/success";
	public static final String URL_PAYPAL_CANCEL = "pay/cancel";
	
	@Autowired
	OrderService orderService;
	  
	@Autowired
	AccountService accountService;
	
	@Autowired
	private PaymentService paymentService;
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@RequestMapping("/order/checkout")
	public String checkout(Model model,HttpServletRequest request) {
		model.addAttribute("orderValidate", new Order());
		return "user/checkout";
	}
	@RequestMapping("/order/tracking")
	public String tracking(Model model,HttpServletRequest request) {
		String username = request.getRemoteUser();
		model.addAttribute("ordersTracking", orderService.findByUsernameTracking(username));
		return "user/trackingorder";
	}
	@RequestMapping("/order/list")
	public String list(Model model,HttpServletRequest request) {
		String username = request.getRemoteUser();
		model.addAttribute("orders", orderService.findByUsername(username));
		return "user/listorder";
	}
	@RequestMapping("/order/detail/{id}")
	public String detail(@PathVariable("id") Integer id,Model model) {
		model.addAttribute("order", orderService.findById(id));
		return "user/detailorder";
	}
	  
	@PostMapping("/thanh-toan")
	public String pay(HttpServletRequest request,@RequestParam("orderId") String orderId ,@RequestParam("price") Double price){
		String cancelUrl = "http://localhost:8080/" + URL_PAYPAL_CANCEL;
		String successUrl = "http://localhost:8080/" + URL_PAYPAL_SUCCESS;
		try {
			Payment payment = paymentService.createPayment(
					price/23000,
					"USD",
					"paypal",
					"sale",
					orderId, 
					cancelUrl,
					successUrl);
			for(Links links : payment.getLinks()){
				if(links.getRel().equals("approval_url")){
					return "redirect:" + links.getHref();
				}
			} 
		} catch (PayPalRESTException e) {
			System.out.println("lỗi dòng 68");
			log.error(e.getMessage());
		}
		return "redirect:/";
	}
	@GetMapping(URL_PAYPAL_CANCEL)
	public String cancelPay(){
		System.out.println("trong hàm cancel");
		return "user/cancel2";
	}
	@GetMapping(URL_PAYPAL_SUCCESS)
	public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
		try {
			Payment payment = paymentService.executePayment(paymentId, payerId);
			//upadate booking
			Order order = orderService.findById(Integer.parseInt(payment.getTransactions().get(0).getDescription()));
			order.setOrderStatus("Đã thanh toán");
			orderService.updateOrder(order);
			
			if(payment.getState().equals("approved")){
				return "user/success2";
			}
		} catch (PayPalRESTException e) {
			System.out.println("lỗi dòng 96");
			log.error(e.getMessage());
		}
		return "redirect:/";
	}
	
}
