package com.tin.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Path.ReturnValueNode;

import org.apache.logging.log4j.status.StatusConsoleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tin.entity.Account;
import com.tin.entity.Provider;
import com.tin.repository.AccountRepo;
import com.tin.service.AccountService;

import javassist.expr.NewArray;

@Controller
public class test {
	
	@Autowired
	static HttpSession session;
	

	//test azure
	static BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
	public static void main(String[] args) {
//		String passwordString = bcrypt.encode("123");
//		System.out.println(passwordString);
	}

	 @GetMapping("/st") public String change() {
		 return "user/invoice"; 
	}
	
	@GetMapping("/contact")
	public String contact() {
		return "user/contact";
	}

}
