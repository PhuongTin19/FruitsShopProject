package com.tin.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminProductController {
	@RequestMapping("/admin/products")
	public String adminProduct(Model model) {
		return "/admin/Product/products.html";
	}
}
