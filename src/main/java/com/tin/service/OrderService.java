package com.tin.service;

import java.sql.Date;
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
	
	Page<Order> findByOrderID(Integer oid, int page, int size);
	
	Page<Order> findByOrderID(Integer oid, Pageable page);
	
	Page<Order>findByOrder(Date startDate,Date endDate,int page, int size);
	
	Page<Order>findByOrder(int page, int size);
	
	Page<Order>findByOrderStatus(String status,int page, int size);
	
	String[][] getTotalPriceLast6Months();
	
	String[][] getTotalPriceFromTo(String from, String to);
	
	String[][] statsOrderStatus();
	
	String[][] statsRevenueProductsByCates();
	
	String[][] getProductsByCatesFromTo(String from, String to);
	
	List<Object[]> detailReceipt();
	
	List<Object[]> detailReceiptStatus(String orderStatus);

	List<Order> findAll();
}