package com.tin.admin.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tin.custom.CustomOAuth2User;
import com.tin.custom.UserServices;
import com.tin.entity.Account;
import com.tin.entity.Order;
import com.tin.service.AccountService;
import com.tin.service.OrderService;
 
@Controller
public class AdminOrderController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	UserServices userServices;
	 
	@RequestMapping("/admin-order")
	public String adminOrder(Model model, HttpServletRequest request, Authentication authentication,
			@RequestParam(name="page",defaultValue = "1") int page) throws ParseException {		
		userServices.getUserName(request, authentication);
		//Danh sách đơn hàng đã đặt
		String day1 = request.getParameter("day"); 
		String end1 = request.getParameter("end");
		model.addAttribute("day",day1);
		model.addAttribute("end",end1);
		System.out.println(day1);
		System.out.println(end1);
		Page<Order> orderList ;
		if(day1 == null || end1 == null) {
			orderList = orderService.findByOrder(Date.valueOf("2022-01-01"),Date.valueOf("2022-12-31"),page-1,10);
			model.addAttribute("orders", orderList.getContent());
			model.addAttribute("totalPage", orderList.getTotalPages());
			model.addAttribute("currentPage", page);
		}else {
			orderList = orderService.findByOrder(Date.valueOf(day1),Date.valueOf(end1),page-1,10);
			model.addAttribute("orders", orderList.getContent());
			model.addAttribute("totalPage", orderList.getTotalPages());
			model.addAttribute("currentPage", page);
		}
		return "admin/Order/Order";
	}
	
	@RequestMapping("/admin-detailOrder/{id}")
	public String adminDetailOrder(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("order", orderService.findById(id));
		return "admin/Order/DetailOrder";
	}
	
	@RequestMapping("/admin-verifyOrder/{id}")
	public String adminVerifyOrder(@PathVariable("id") Integer id, Model model,
			HttpServletRequest request, Authentication authentication,
			@RequestParam(name="page",defaultValue = "1") int page) {
		Order order = orderService.findById(id);
		order.setOrderStatus("Hoàn thành");
		orderService.updateOrder(order);
		//
		userServices.getUserName(request, authentication);
		//Danh sách đơn hàng đã đặt
		Page<Order> orderList = orderService.findByOrder(page-1,10);
		model.addAttribute("orders", orderList.getContent());
		model.addAttribute("totalPage", orderList.getTotalPages());
		model.addAttribute("currentPage", page);
		try {
			userServices.sendMailPurchaseSuccess(order);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/admin-order";
	}
	//Hủy đơn
	@RequestMapping("/admin-cancelOrder/{id}")
	public String adminCancelOrder(@PathVariable("id") Integer id, Model model,
			HttpServletRequest request, Authentication authentication,
			@RequestParam(name="page",defaultValue = "1") int page) {
		Order order = orderService.findById(id);
		order.setOrderStatus("Đã hủy đơn");
		orderService.updateOrder(order);
		//
		userServices.getUserName(request, authentication);
		//Danh sách đơn hàng đã đặt
		Page<Order> orderList = orderService.findByOrder(page-1,10);
		model.addAttribute("orders", orderList.getContent());
		model.addAttribute("totalPage", orderList.getTotalPages());
		model.addAttribute("currentPage", page);
		return "admin/Order/Order";
	}
	
	//Lọc order theo trạng thái
	//chưa thanh toán
	@RequestMapping("/admin-order/unpaid")
	public String doGetFindAllByOrderUnpaid(Model model,HttpServletRequest request,Authentication authentication,
			 @RequestParam(name="page",defaultValue = "1") int page) {
		userServices.getUserName(request, authentication);
		Page<Order> orderList = orderService.findByOrderStatus("Chưa thanh toán",page-1,10);
		model.addAttribute("orders", orderList.getContent());
		model.addAttribute("totalPage", orderList.getTotalPages());
		model.addAttribute("currentPage", page);
		return "admin/Order/Order";
	}
	//Đã hủy đơn
	@RequestMapping("/admin-order/cancel")
	public String doGetFindAllByOrderCancel(Model model,HttpServletRequest request,Authentication authentication,
			 @RequestParam(name="page",defaultValue = "1") int page) {
		userServices.getUserName(request, authentication);
		Page<Order> orderList = orderService.findByOrderStatus("Đã hủy đơn",page-1,10);
		model.addAttribute("orders", orderList.getContent());
		model.addAttribute("totalPage", orderList.getTotalPages());
		model.addAttribute("currentPage", page);
		return "admin/Order/Order";
	}
	//Hoàn thành
	@RequestMapping("/admin-order/success")
	public String doGetFindAllByOrderSuccess(Model model,HttpServletRequest request,Authentication authentication,
			 @RequestParam(name="page",defaultValue = "1") int page) {
		userServices.getUserName(request, authentication);
		Page<Order> orderList = orderService.findByOrderStatus("Hoàn thành",page-1,10);
		model.addAttribute("orders", orderList.getContent());
		model.addAttribute("totalPage", orderList.getTotalPages());
		model.addAttribute("currentPage", page);
		return "admin/Order/Order";
	}
	
	
	//Chi tiết trạng thái
	@RequestMapping("/detailReceipt")
	public String detailReceipt(Model model, HttpServletRequest request, Authentication authentication,
			@RequestParam(name="page",defaultValue = "1") int page) throws ParseException {		
		userServices.getUserName(request, authentication);
		//Danh sách đơn hàng đã đặt
//		String day1 = request.getParameter("day"); 
//		String end1 = request.getParameter("end");
//		model.addAttribute("day",day1);
//		model.addAttribute("end",end1);
//		System.out.println(day1);
//		System.out.println(end1);
//		Page<Order> orderList ;
//		if(day1 == null || end1 == null) {
//			orderList = orderService.findByOrder(Date.valueOf("2022-01-01"),Date.valueOf("2022-12-31"),page-1,10);
//			model.addAttribute("orders", orderList.getContent());
//			model.addAttribute("totalPage", orderList.getTotalPages());
//			model.addAttribute("currentPage", page);
//		}else {
//			orderList = orderService.findByOrder(Date.valueOf(day1),Date.valueOf(end1),page-1,10);
//			model.addAttribute("orders", orderList.getContent());
//			model.addAttribute("totalPage", orderList.getTotalPages());
//			model.addAttribute("currentPage", page);
//		}
		List<Object[]>orderList = orderService.detailReceipt();
		model.addAttribute("ordersAll", orderList);
		List<Object[]>orderListStatus1 = orderService.detailReceiptStatus("Chưa thanh toán");
		model.addAttribute("orderListStatus1", orderListStatus1);
		List<Object[]>orderListStatus2 = orderService.detailReceiptStatus("Hoàn thành");
		model.addAttribute("orderListStatus2", orderListStatus2);
		List<Object[]>orderListStatus3 = orderService.detailReceiptStatus("Đã hủy đơn");
		model.addAttribute("orderListStatus3", orderListStatus3);
//		model.addAttribute("totalPage", orderList.getTotalPages());
//		model.addAttribute("currentPage", page);
		return "admin/Order/DetailReceipt";
	}
	
}
