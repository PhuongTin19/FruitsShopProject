package com.tin.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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

import javassist.expr.NewArray;

@Controller
public class test {
	
	@Autowired
	static HttpSession session;
	

	//test azure
	static BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
	public static void main(String[] args) {
		boolean checkPassword = bcrypt.matches("","$2a$10$q9gbaErqW6S1BgtW8gqMfuk5RH9rzHElGUh7bbpaP8whw90bLIMJS");
		System.out.println(checkPassword);
		
	}


	
	@GetMapping("/contact")
	public String contact() {
		return "user/contact";
	}

}
