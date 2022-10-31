package com.tin.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminBrand {
	
	@RequestMapping("/admin/brands")
	public String adminBrand(Model model) {
		return "admin/Brand/brands";
	}

}
