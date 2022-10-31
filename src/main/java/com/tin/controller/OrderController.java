package com.tin.controller;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.tin.custom.CustomOAuth2User;
import com.tin.custom.UserServices;
import com.tin.entity.Account;
import com.tin.entity.Blog;
import com.tin.entity.Order;
import com.tin.entity.Product;
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

	@Autowired
	UserServices userServices;

	private Logger log = LoggerFactory.getLogger(getClass());

	@RequestMapping("/cart") 
	public String test4() {
		return "user/shoping-cart";
	}
	@RequestMapping("/order/checkout")
	public String checkout(Model model, HttpServletRequest request) {
		Account username = accountService.findByUsername(request.getRemoteUser());
		model.addAttribute("username", username);
		model.addAttribute("orderValidate", new Order());
		
		return "user/checkout";
	}
 
	@RequestMapping("/order/tracking")
	public String tracking(Model model, HttpServletRequest request) {
		String username = request.getRemoteUser();
		model.addAttribute("ordersTracking", orderService.findByUsernameTracking(username));
		return "user/trackingorder";
	}

	@RequestMapping("/order/list")
	public String list(Model model, HttpServletRequest request, Authentication authentication,
			@RequestParam("page") Optional<Integer> p) {
		Account account = accountService.findByUsername(request.getRemoteUser());
		String username = null;
		if (account == null) {
			CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
			Account accountOauth = accountService.findByEmail(oauth2User.getEmail());
			username = accountOauth.getUsername();
		} else {
			username = account.getUsername();
		}
		model.addAttribute("userRequest", accountService.findByUsername(username));
		//Danh sách đơn hàng đã đặt
		Page<Order> orderList = orderService.findByUsername(username, PageRequest.of(p.orElse(0), 10));
		model.addAttribute("orders", orderList);
		if(orderList.isEmpty()) {
			model.addAttribute("noOrder","Chưa có đơn hàng"); 
		}
		return "user/listorder";
	}

	@RequestMapping("/order/detail/{id}")
	public String detail(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("order", orderService.findById(id));
		return "user/detailorder";
	}

	@PostMapping("/thanh-toan")
	public String pay(HttpServletRequest request, @RequestParam("orderId") String orderId,
			@RequestParam("price") Double price) {
		String cancelUrl = "http://localhost:8080/" + URL_PAYPAL_CANCEL;
		String successUrl = "http://localhost:8080/" + URL_PAYPAL_SUCCESS;
		try {
			Payment payment = paymentService.createPayment(price / 23000, "USD", "paypal", "sale", orderId, cancelUrl,
					successUrl);
			for (Links links : payment.getLinks()) {
				if (links.getRel().equals("approval_url")) {
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
	public String cancelPay() {
		System.out.println("trong hàm cancel");
		return "user/cancel2";
	}

	@GetMapping(URL_PAYPAL_SUCCESS)
	public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
		try {
			Payment payment = paymentService.executePayment(paymentId, payerId);
			Order order = orderService.findById(Integer.parseInt(payment.getTransactions().get(0).getDescription()));
			order.setOrderStatus("Hoàn thành");
			orderService.updateOrder(order);

			if (payment.getState().equals("approved")) {
				try {
					userServices.sendMailPurchaseSuccess(order);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "user/success2";
			}
		} catch (PayPalRESTException e) {
			System.out.println("lỗi dòng 96");
			log.error(e.getMessage());
		}
		return "redirect:/";
	}

	// hủy đơn  
 
	@GetMapping("/cancel-order/{id}")
	public String cancelOrder(@PathVariable("id") Integer id, Model model) {
		Order order = orderService.findById(id);
		model.addAttribute("order", order);
		return "/user/cancelOrder";
	}

	@PostMapping("/process-cancel-order")
	public String processCancelOrder(Order order,HttpServletRequest request, Model model)
			throws UnsupportedEncodingException, MessagingException {
		try {
//			System.out.println(order.getAddress());
//			Order order = orderService.findById(id);
			userServices.cancelOrder(order, getSiteURL(request));
			model.addAttribute("message", "Vui lòng kiểm tra email.");
			model.addAttribute("order", order);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/order/list";
	}

	private String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}

//	public void cancelOrderAuto() {
//		Order order = orderService.findById(null);
//		Calendar now = Calendar.getInstance();
//		Timer t = new Timer();
//		t.schedule(new TimerTask() {
//			public void run() {
//				if (!order.getOrderStatus().equalsIgnoreCase("Hoàn thành")) {
//					System.out.println("Đang chạy timer if");
//					order.setOrderStatus("Đã hủy đơn");
//					orderService.updateOrder(order);
//				} else {
//					System.out.println("Đang chạy timer else");
//				}
//			}
//		}, 60000);
//	}
//	@GetMapping("/verify")
//	public String verifyUser(@Param("code") String code, Model model) {
//		System.out.println("code: " + code);
//		if (userServices.verifyCancelOrder(code)) {
//			model.addAttribute("message", "Xác nhận hủy đơn thành công");
//			return "forward:/order/list";
//		} else {
//			return "/index";
//		}
//	}

}
