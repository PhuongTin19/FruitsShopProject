package com.tin.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tin.entity.OrderDetail;
import com.tin.entity.Product;
import com.tin.repository.OrderDetailRepo;
import com.tin.service.OrderDetailsService;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService{

	@Autowired
	OrderDetailRepo orderDetailRepo;

	@Override
	public List<OrderDetail> findByDetailId(Integer id) {
		return orderDetailRepo.findByDetailId(id);
	}

	@Override
	public List<OrderDetail> findAll() {
		return orderDetailRepo.findAll();
	}

 
	
	

}
