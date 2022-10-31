package com.tin.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminCategoryController {
	
	@RequestMapping("/admin/categories")
	public String adminCategories(Model model) {
		return "admin/Category/categories";
	}
}
