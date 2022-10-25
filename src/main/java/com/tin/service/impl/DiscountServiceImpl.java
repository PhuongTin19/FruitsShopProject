package com.tin.service.impl;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tin.entity.Discount;
import com.tin.repository.DiscountRepo;
import com.tin.service.DiscountService;

@Service
public class DiscountServiceImpl implements DiscountService{
	@Autowired
	private DiscountRepo discountRepo;
	
	/*ADMIN*/
	@Override
	public List<Discount> findAll() {
		return discountRepo.findAll();
	}
	
	@Override
	public void save(Discount discount) {
		discountRepo.save(discount);
	}
	
	@Override
	public Discount findByDiscount_id(Integer discountId) {
		return discountRepo.findByDiscount_id(discountId);
	}
	
	@Override
	public void updateDiscountById(Integer id, String name, Double Discount, Date start_time, Date end_time,
			Boolean is_enable) {
		discountRepo.updateDiscountById(id, name, Discount, start_time, end_time, is_enable);
	}
	
	@Override
	public List<Discount> findByIsEnable() {
		return discountRepo.findByIsEnable();
	}
}