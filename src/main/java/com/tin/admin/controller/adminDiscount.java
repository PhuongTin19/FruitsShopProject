package com.tin.admin.controller;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tin.entity.Discount;
import com.tin.service.DiscountService;

@Controller
public class adminDiscount {

	@Autowired
	DiscountService discountService;
	
	@RequestMapping("/admin/discounts")
	public String adminDiscount(Model model) {
		try {
			disableDiscountAuto(); 
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return "admin/Discount/discounts";
	}
	
	public void disableDiscountAuto() {
		try {
			long millis=System.currentTimeMillis();   
			List<Discount>discounts = discountService.findAll();
			java.sql.Date date=new java.sql.Date(millis); 
			TimerTask timerTask = new TimerTask() {
	            @Override
	            public void run() {
	            	for (int i = 0; i < discounts.size(); i++) {
	            		//ngày kết thúc = ngày hiện tại thì disable
	            		if(discounts.get(i).getEnd_time().before(date)) {
	                		discountService.deleteLogical(discounts.get(i).getDiscount_id());
	                	}else if(discounts.get(i).getStart_time().after(date)) {
	                		discountService.deleteLogical(discounts.get(i).getDiscount_id());
	                	}else if(discounts.get(i).getStart_time().before(date)) {
	                		discountService.updateLogical(discounts.get(i).getDiscount_id());
	                	}
					} 
	            }
	        };
	        long delay = 100000L;
	        Timer timer = new Timer("Timer");
	        timer.schedule(timerTask, 0, delay);
		} catch (Exception e) {
			System.out.println("");
		} 
	}
}
