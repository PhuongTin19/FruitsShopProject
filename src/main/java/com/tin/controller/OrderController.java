package com.tin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tin.entity.Account;
import com.tin.entity.Order;
import com.tin.service.AccountService;
import com.tin.service.OrderService;

import javassist.expr.NewArray;
          
@Controller
public class OrderController {
	@Autowired
	OrderService orderService;
	  
	@Autowired
	AccountService accountService;
	
	@RequestMapping("/order/checkout")
	public String checkout(Model model,HttpServletRequest request) {
		model.addAttribute("orderValidate", new Order());
		return "user/checkout";
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
	
}
