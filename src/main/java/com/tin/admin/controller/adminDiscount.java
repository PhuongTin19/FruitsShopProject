package com.tin.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class adminDiscount {

	@RequestMapping("/admin/discounts")
	public String adminDiscount(Model model) {
		return "admin/Discount/discounts";
	}
}
