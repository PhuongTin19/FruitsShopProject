package com.tin.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tin.entity.OrderDetail;
import com.tin.entity.Product;
import com.tin.custom.UserServices;
import com.tin.entity.Account;
import com.tin.entity.Order;
import com.tin.repository.OrderDetailRepo;
import com.tin.repository.OrderRepo;
import com.tin.repository.ProductRepo;
import com.tin.service.AccountService;
import com.tin.service.OrderService;

import ch.qos.logback.core.joran.conditional.IfAction;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	OrderRepo orderRepo;

	@Autowired
	OrderDetailRepo orderDetailRepo;

	@Autowired
	ProductRepo productRepo;

	@Autowired
	ProductRepo productService;

	@Autowired
	UserServices userServices;
	
	@Autowired
	AccountService accountService;
	
	@Override
	public Order create(JsonNode orderData)  {
		ObjectMapper mapper = new ObjectMapper();
		Order order = mapper.convertValue(orderData, Order.class);
		orderRepo.save(order);
		TypeReference<List<OrderDetail>> type = new TypeReference<List<OrderDetail>>() {
		};
		List<OrderDetail> details = mapper.convertValue(orderData.get("orderDetails"), type).stream()
				.peek(d -> d.setOrder(order)).collect(Collectors.toList());
		orderDetailRepo.saveAll(details);
		// Cập nhật số lượng mới
		for (int i = 0; i < orderData.get("orderDetails").size(); i++) {
			Product product = productService.findById(details.get(i).getProduct().getProduct_id()).get();
			Integer newQuanity = product.getQuantity() - details.get(i).getTotalQuantity();
			productService.updateQuantity(newQuanity, details.get(i).getProduct().getProduct_id());
		}
		//
		System.out.println(order.getOrder_id());
		Calendar now = Calendar.getInstance();
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				Order order2 = orderRepo.findById(order.getOrder_id()).get();
				if (!order2.getOrderStatus().equalsIgnoreCase("Hoàn thành") && 
						order2.getPayment_method().getPayment_method_id() == 2) {
					System.out.println("Đang chạy timer if");
					order.setOrderStatus("Đã hủy đơn");
					updateOrder(order);
					try {
						userServices.sendMailCancelOrderOnline(order);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("Đang chạy timer else");
				}
			}
		}, 120000);
		//
		try {
			userServices.purchaseOrder(order);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return order;
		

	}

	
	
	
	
	@Override
	public Order findById(Integer id) {
		return orderRepo.findById(id).get();
	}

	@Override
	public Page<Order> findByUsername(String username, Pageable pageable) {
		return orderRepo.findByUsername(username, pageable);
	}

	@Override
	public Order findByUsernameTracking(String username) {
		return orderRepo.findByUsernameTracking(username);
	}

	@Override
	public Order updateOrder(Order order) {
		order.setAccount(order.getAccount());
		order.setAddress(order.getAddress());
		order.setDeliveryDate(order.getDeliveryDate());
		order.setNotes(order.getNotes());
		order.setOrderdate(order.getOrderdate());
		order.setOrderStatus(order.getOrderStatus());
		order.setPhone(order.getPhone());
		order.setShippingFee(order.getShippingFee());
		return orderRepo.save(order);
	}

	@Override
	public Order findByVerificationCode(String code) {
		return orderRepo.findByVerificationCode(code);
	}

	@Override
	public List<Order> findByUsernameList(String username) {
		return orderRepo.findByUsernameList(username);
	}

	@Override
	public Integer getCountOrderInDay() {
		return orderRepo.getCountOrderInDay();
	}

	@Override
	public Double getRevenue() {
		return orderRepo.getRevenue();
	}

	@Override
	public void cancelOrderAuto(Integer id) {
		Order order = orderRepo.findById(id).get();
		Calendar now = Calendar.getInstance();
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				if (!order.getOrderStatus().equalsIgnoreCase("Hoàn thành")) {
					System.out.println("Đang chạy timer if");
					order.setOrderStatus("Đã hủy đơn");
					updateOrder(order);
				} else {
					System.out.println("Đang chạy timer else");
				}
			}
		}, 60000);
		
	}

}
