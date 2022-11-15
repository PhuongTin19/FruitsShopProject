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
//		boolean checkPassword = bcrypt.matches("456","$2a$10$Sn2G.oihgZwO4K6PbH6ezOMQaa5SyURHGuqztbx3zyeBNk7pLROGW");
//		System.out.println(checkPassword);
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf3.format(new Date());
		TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println(sdf3.format(date));
            }
        };
        long delay = 1000L;
        Timer timer = new Timer("Timer");
        timer.schedule(timerTask, 0, delay);
	}


	
	@GetMapping("/contact")
	public String contact() {
		return "user/contact";
	}

}
