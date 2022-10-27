package com.tin.admin.controller;

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
	public String adminOrder(Model model, HttpServletRequest request, Authentication authentication) {
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
		List<Order> orderList = orderService.findByUsernameList(username);
		model.addAttribute("orders", orderList);
		return "admin/Order/Order";
	}
	
	@RequestMapping("/admin-detailOrder/{id}")
	public String adminDetailOrder(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("order", orderService.findById(id));
		return "admin/Order/DetailOrder";
	}
	
	@RequestMapping("/admin-verifyOrder/{id}")
	public String adminVerifyOrder(@PathVariable("id") Integer id, Model model,
			HttpServletRequest request, Authentication authentication) {
		Order order = orderService.findById(id);
		order.setOrderStatus("Hoàn thành");
		orderService.updateOrder(order);
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
		List<Order> orderList = orderService.findByUsernameList(username);
		model.addAttribute("orders", orderList);
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
			HttpServletRequest request, Authentication authentication) {
		Order order = orderService.findById(id);
		order.setOrderStatus("Đã hủy đơn");
		orderService.updateOrder(order);
		//
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
		List<Order> orderList = orderService.findByUsernameList(username);
		model.addAttribute("orders", orderList);
		return "admin/Order/Order";
	}
	//Xác nhận hoàn thành
//	@RequestMapping("/admin-successOrder/{id}")
//	public String adminSuccessOrder(@PathVariable("id") Integer id, Model model,
//			HttpServletRequest request, Authentication authentication) {
//		Order order = orderService.findById(id);
//		order.setOrderStatus("Hoàn thành");
//		orderService.updateOrder(order);
//		//
//		Account account = accountService.findByUsername(request.getRemoteUser());
//		String username = null;
//		if (account == null) {
//			CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
//			Account accountOauth = accountService.findByEmail(oauth2User.getEmail());
//			username = accountOauth.getUsername();
//		} else {
//			username = account.getUsername();
//		}
//		model.addAttribute("userRequest", accountService.findByUsername(username));
//		//Danh sách đơn hàng đã đặt
//		List<Order> orderList = orderService.findByUsernameList(username);
//		model.addAttribute("orders", orderList);
//		return "admin/Order/Order";
//	}
	
}
