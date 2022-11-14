package com.tin.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Path.ReturnValueNode;

import org.apache.logging.log4j.status.StatusConsoleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tin.entity.Account;
import com.tin.service.AccountService;

@Controller
public class test {
	
	@Autowired
	static HttpSession session;
	

	//test azure
	private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
	public static void main(String[] args) {
		boolean checkPassword = bcrypt.matches("456","$2a$10$Sn2G.oihgZwO4K6PbH6ezOMQaa5SyURHGuqztbx3zyeBNk7pLROGW");
		System.out.println(checkPassword);
	}
	
	@GetMapping("/contact")
	public String contact() {
		return "user/contact";
	}

}
