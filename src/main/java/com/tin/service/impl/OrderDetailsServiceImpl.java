package com.tin.service.impl;

import java.util.List;

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
 
	
	

}
