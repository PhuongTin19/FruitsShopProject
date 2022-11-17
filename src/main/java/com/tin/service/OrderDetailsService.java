package com.tin.service;

import java.util.List;
import java.util.Optional;

import com.tin.entity.OrderDetail;
import com.tin.entity.Product;

public interface OrderDetailsService {
	
	List<OrderDetail>findByDetailId(Integer id);
		
	List<OrderDetail> findAll();
	
}
