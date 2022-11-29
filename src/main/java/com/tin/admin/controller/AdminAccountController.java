package com.tin.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
  
@Controller
public class AdminAccountController {
	@RequestMapping("/admin/accounts")
	public String adminProduct(Model model) {
		return "/admin/Account/accounts.html";
	}
	
	@RequestMapping("/staff")
	public String staff(Model model) {
		return "/admin/Account/staff.html";
	}
	
	@RequestMapping("/inforAdmin")
	public String inforAdmin(Model model) {
		return "/admin/Account/admin.html";
	}
	
	@RequestMapping("/individual")
	public String individual(Model model) {
		return "/admin/Account/individual.html";
	}
}
