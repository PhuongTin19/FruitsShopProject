package com.tin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.JsonNode;
import com.tin.entity.Order;
import com.tin.entity.Product;

public interface OrderService {

	public Order create(JsonNode orderData) ;

	public Order findById(Integer id) ;

	public Page<Order> findByUsername(String username,Pageable pageable) ;
	
	Order findByUsernameTracking(String username);
	
	Order updateOrder(Order order);
	
	public Order findByVerificationCode(String code);
	
	List<Order> findByUsernameList(String username);
	
	Integer getCountOrderInDay();
	
	Double getRevenue();
}
