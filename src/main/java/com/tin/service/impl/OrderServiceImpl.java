package com.tin.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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
import com.tin.service.ReCaptchaValidationService;

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
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	private ReCaptchaValidationService validator;
	
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
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				Order order2 = orderRepo.findById(order.getOrder_id()).get();
				if (!order2.getOrderStatus().equalsIgnoreCase("Hoàn thành") && 
						order2.getPayment_method().getPayment_method_id() == 2) {
					System.out.println("Đang chạy timer if");
					order.setOrderStatus("Đã hủy đơn");
					//Cập nhật số lượng vào product
					for (int i = 0; i < orderData.get("orderDetails").size(); i++) {
						Product product = productService.findById(details.get(i).getProduct().getProduct_id()).get();
						Integer newQuanity = product.getQuantity() + details.get(i).getTotalQuantity();
						productService.updateQuantity(newQuanity, details.get(i).getProduct().getProduct_id());
					}
					//Tăng độ uy tín
					Account account = accountService.findByUsername(order2.getAccount().getUsername());
					accountService.updateReliability(account.getReliability()+1, order2.getAccount().getUsername());
					//
					updateOrder(order);
					try {
						System.out.print(account.getReliability());							
						if(account.getReliability()==4) {
							userServices.sendMailDisableAccount(order);
						}else {
							userServices.sendMailCancelOrderOnline(order);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("Đang chạy timer else");
				}
			}
		}, 60000);
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
	public Page<Order> findByOrder(int page, int size) {
		return orderRepo.findByOrder(PageRequest.of(page, size));
	}

	@Override
	public Page<Order> findByOrder(Date startDate,Date endDate,int page, int size) {
		return orderRepo.findByOrder(startDate,endDate,PageRequest.of(page, size));
	}



	@Override
	public Page<Order> findByOrderStatus(String status,int page, int size) {
		return orderRepo.findByOrderStatus(status,PageRequest.of(page, size));
	}
	
	@Override
	public String[][] getTotalPriceLast6Months() {
		String[][] result = new String[2][6];
		YearMonth currentTimes = YearMonth.now();
		for (int i = 0; i < 6; i++) {
			String month = currentTimes.minusMonths(i).getMonthValue() + "";
			String year = currentTimes.minusMonths(i).getYear() + "";
			result[0][5-i] = month + "-" + year;
			result[1][5-i] = orderRepo.getTotalPricePerMonth(month, year);
		}
 		return result;
	}

	@Override
	public String[][] getTotalPriceFromTo(String from, String to) {
		String[][] result1 = orderRepo.getTotalPriceFromTo(from, to);
		String[][] result = new String[2][result1.length];	
		for(int i =0 ; i<result1.length; i++) {
			result[0][result1.length- 1 - i] = result1[result1.length- 1 - i][0];
			result[1][result1.length- 1 - i] = result1[result1.length- 1 - i][1];
		}
		return result; 
	}

	@Override
	public String[][] statsOrderStatus() {
		String[][] result1 = orderRepo.statsOrderStatus();
		String[][] result = new String[2][result1.length];	
		for(int i =0 ; i<result1.length; i++) {
			result[0][result1.length- 1 - i] = result1[result1.length- 1 - i][0];
			result[1][result1.length- 1 - i] = result1[result1.length- 1 - i][1];
		}
		return result; 
	}

	@Override
	public String[][] statsRevenueProductsByCates() {
		String[][] result1 = orderRepo.statsRevenueProductsByCates();
		String[][] result = new String[2][result1.length];	
		for(int i =0 ; i<result1.length; i++) {
			result[0][result1.length- 1 - i] = result1[result1.length- 1 - i][0];
			result[1][result1.length- 1 - i] = result1[result1.length- 1 - i][1];
		}
		return result; 
	}

	@Override
	public String[][] getProductsByCatesFromTo(String from, String to) {
		String[][] result1 = orderRepo.getProductsByCatesFromTo(from, to);
		String[][] result = new String[2][result1.length];	
		for(int i =0 ; i<result1.length; i++) {
			result[0][result1.length- 1 - i] = result1[result1.length- 1 - i][0];
			result[1][result1.length- 1 - i] = result1[result1.length- 1 - i][1];
		}
		return result; 
	}

	@Override
	public List<Object[]> detailReceipt() {
		return orderRepo.detailReceipt();
	}

	@Override
	public List<Object[]> detailReceiptStatus(String orderStatus) {
		return orderRepo.detailReceiptStatus(orderStatus);
	}
	@Override
	public List<Order> findAll() {
		return orderRepo.findAll(Sort.by("orderdate").descending());
	}
	
	@Override
	public Page<Order> findByOrderID(Integer oid, Pageable page) {
		return orderRepo.findByOrderID(oid, page);
	}

	@Override
	public Page<Order> findByOrderID(Integer oid, int page, int size) {
		return orderRepo.findByOrderID(oid,PageRequest.of(page, size));
	}
}
