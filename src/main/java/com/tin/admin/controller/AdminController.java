package com.tin.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {
	@RequestMapping({"/admin","/admin/home/index"})
	public String admin(Model model,HttpServletRequest request) {
		return "/admin/index.html";
	}
}
