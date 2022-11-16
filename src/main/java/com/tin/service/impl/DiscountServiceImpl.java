package com.tin.service.impl;

import java.sql.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.transaction.Transactional;

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
	
//	@Override
//	public Discount findByDiscount_id(Integer discountId) {
//		return discountRepo.findByDiscount_id(discountId);
//	}
	
	@Override
	public void updateDiscountById(Integer id, String name, Double Discount, Date start_time, Date end_time,
			Boolean is_enable) {
		discountRepo.updateDiscountById(id, name, Discount, start_time, end_time, is_enable);
	}
	
	@Override
	public List<Discount> findByIsEnable() {
		return discountRepo.findByIsEnable();
	}

	/***********************************************/
	@Override
	public Discount findByDiscountId(int id) {
		return discountRepo.findByDiscoundId(id);
	}

	@Override
	public Discount create(Discount discount) {
		return discountRepo.save(discount);
	}

	@Override
	public Discount update(Discount discount) {
		return discountRepo.save(discount);
	}

	@Override
	public void delete(int id) {
		discountRepo.deleteById(id);
	}
	
	@Override
	public List<Discount> findByKeyword(String keyword) {
		return discountRepo.findByKeyword(keyword);
	}

	@Override
	@Transactional
	public void deleteLogical(Integer id) {
		discountRepo.deleteLogical(id);
	}

	@Override
	@Transactional
	public void updateLogical(Integer id) {
		discountRepo.updateLogical(id);
	}
}
