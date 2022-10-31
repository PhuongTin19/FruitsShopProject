package com.tin.service;

import java.sql.Date;
import java.util.List;

import com.tin.entity.Discount;

public interface DiscountService {
	/*ADMIN*/
	List<Discount> findAll();
	
	void save(Discount discount);
	
//	Discount findByDiscount_id(Integer discountId);
	
	void updateDiscountById(Integer id, String name, Double Discount, Date start_time, 
			Date end_time, Boolean is_enable);
	
	List<Discount> findByIsEnable();
	
	/****************************/
	Discount findByDiscountId(int id);
	Discount create(Discount discount);
	Discount update(Discount discount);
	void delete(int id);
	
}
