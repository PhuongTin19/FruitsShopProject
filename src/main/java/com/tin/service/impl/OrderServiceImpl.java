package com.tin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

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
import com.tin.entity.Order;
import com.tin.repository.OrderDetailRepo;
import com.tin.repository.OrderRepo;
import com.tin.repository.ProductRepo;
import com.tin.service.OrderService;

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

	@Override
	public Order create(JsonNode orderData) {
		ObjectMapper mapper = new ObjectMapper();

		Order order = mapper.convertValue(orderData, Order.class);
		orderRepo.save(order);

		TypeReference<List<OrderDetail>> type = new TypeReference<List<OrderDetail>>() {
		};
		List<OrderDetail> details = mapper.convertValue(orderData.get("orderDetails"), type).stream()
				.peek(d -> d.setOrder(order)).collect(Collectors.toList());
		orderDetailRepo.saveAll(details);
		//Cập nhật số lượng mới
		for (int i = 0; i < orderData.get("orderDetails").size(); i++) {
			Product product = productService.findById(details.get(i).getProduct().getProduct_id()).get();
			Integer newQuanity = product.getQuantity() - details.get(i).getTotalQuantity();
			productService.updateQuantity(newQuanity, details.get(i).getProduct().getProduct_id());
		}
		return order;
	}

	@Override
	public Order findById(Integer id) {
		return orderRepo.findById(id).get();
	}

	@Override
	public Page<Order> findByUsername(String username,Pageable pageable) {
		return orderRepo.findByUsername(username,pageable);
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

}