package com.tin.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class test {
	
	@Autowired
	static HttpSession session;
	
	private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static void main(String[] args) {
//		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
//		String rawPass = "123456";
//		boolean checkPassword = bcrypt.matches(rawPass, "$2a$10$iQDtqWhDGAXCAnNhQcz9MO9ML0WiCr50P7nG5OcUa5FKE9wyJTTqK");
//			System.out.print(checkPassword);
		int a[] = {3,2,5,6,7};
		System.out.print(a[1]);
   
		
	}
	
	
	
//	
//	@GetMapping("/index")
//	public String test() {
//		return "/user/homepage";
//	}
//
//	@RequestMapping("/shop")
//	public String test2() {
//		return "user/shop-grid";
//	}
////	@RequestMapping("/detailshop")
////	public String test3() {
////		return "user/shop-details";
////	}
	@RequestMapping("/cart") 
	public String test4() {
		return "user/shoping-cart";
	}
	@RequestMapping("/checkout")
	public String test5() {
		return "user/checkout";
	}
//	@RequestMapping("/detailblog")
//	public String test6() {
//		return "user/blog-details";
//	}
//
	@RequestMapping("/contact")
	public String test8() {
		return "user/contact";
	}
////	@RequestMapping("/login")
////	public String test9() {
////		return "user/login";
////	}
//	@RequestMapping("/favorite")
//	public String test10() {
//		return "user/favorite";
//	}
	@RequestMapping("/tracking")
	public String test11() {
		return "user/trackingorder";
	}
	@RequestMapping("/detailorder")
	public String test12() {
		return "user/detailorder";
	}
	@RequestMapping("/listorder")
	public String test13() {
		return "user/listorder";
	}


	
	@RequestMapping("/admin")
	public String testAdmin() {
		return "admin/index";
	}
}
