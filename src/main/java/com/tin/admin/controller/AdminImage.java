package com.tin.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminImage {
	@RequestMapping("/admin/images")
	public String adminImage(Model model) {
		return "admin/Image/images";
	}

}
