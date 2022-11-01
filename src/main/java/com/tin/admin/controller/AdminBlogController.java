package com.tin.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminBlogController {
	@RequestMapping("/admin/blogs")
	public String adminBlog(Model model) {
		return "admin/Blog/blogs";
	}

}
