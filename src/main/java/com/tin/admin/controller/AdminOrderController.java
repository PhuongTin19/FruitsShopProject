package com.tin.admin.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.security.Principal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tin.custom.CustomOAuth2User;
import com.tin.custom.UserServices;
import com.tin.entity.Account;
import com.tin.entity.Behavior;
import com.tin.entity.Order;
import com.tin.entity.OrderDetail;
import com.tin.entity.Product;
import com.tin.repository.OrderRepo;
import com.tin.service.AccountService;
import com.tin.service.OrderDetailsService;
import com.tin.service.OrderService;
import com.tin.service.ProductService;
import com.tin.service.BehaviorService;

import net.minidev.json.writer.BeansMapper.Bean;
 
@Controller
public class AdminOrderController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderDetailsService orderDetailsService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	OrderRepo orderRepo;
	
	@Autowired
	UserServices userServices;
	
	@Autowired
	BehaviorService behaviorService;
	 
	@RequestMapping("/admin-order")
	public String adminOrder(Model model, HttpServletRequest request, Authentication authentication, Principal principal ,
			@RequestParam(name="page",defaultValue = "1") int page) throws ParseException {		
		userServices.getUserName(request, authentication);
		//Danh sách đơn hàng đã đặt
		String day1 = request.getParameter("day"); 
		String end1 = request.getParameter("end");
		model.addAttribute("day",day1);
		model.addAttribute("end",end1);
		System.out.println(day1);
		System.out.println(end1);
		Page<Order> orderList ;
		if(day1 == null || end1 == null) {
			orderList = orderService.findByOrder(Date.valueOf("2022-01-01"),Date.valueOf("2022-12-31"),page-1,10);
			model.addAttribute("orders", orderList.getContent());
			model.addAttribute("totalPage", orderList.getTotalPages());
			model.addAttribute("totalElement", orderList.getTotalElements());
			model.addAttribute("currentPage", page);
		}else {
			orderList = orderService.findByOrder(Date.valueOf(day1),Date.valueOf(end1),page-1,10);
			model.addAttribute("orders", orderList.getContent());
			model.addAttribute("totalPage", orderList.getTotalPages());
			model.addAttribute("totalElement", orderList.getTotalElements());
			model.addAttribute("currentPage", page);
		}
//		tìm theo mã
		try {
			Integer oid = Integer.parseInt(request.getParameter("oid"));
//			Pageable pageable = PageRequest.of(page.orElse(0),9);
			Page<Order> orderList1 = orderService.findByOrderID(oid, page-1,10);
			model.addAttribute("orders", orderList1);
			model.addAttribute("totalElement", orderList1.getTotalElements());
			if(orderList1 == null) {
				model.addAttribute("orders", orderList);
			}else if (!orderRepo.existsById(oid)) {
				model.addAttribute("noOrder","Không có đơn hàng bạn tìm");
			}else if (oid == null) {
				model.addAttribute("orders", orderList);
			}
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return "admin/Order/Order";
	}
	
	@RequestMapping("/admin-behavior")
	public String adminOrderBehavior(Model model, HttpServletRequest request, Authentication authentication,
			@RequestParam(name="page",defaultValue = "1") int page) throws ParseException {		
		userServices.getUserName(request, authentication);
		//Danh sách đơn hàng đã đặt
//		String day1 = request.getParameter("day"); 
//		String end1 = request.getParameter("end");
//		model.addAttribute("day",day1);
//		model.addAttribute("end",end1);
//		System.out.println(day1);
//		System.out.println(end1);
		Page<Behavior> behaviorList = behaviorService.findAll(page-1,10);
		model.addAttribute("behavior", behaviorList.getContent());
		model.addAttribute("totalPage", behaviorList.getTotalPages());
		model.addAttribute("currentPage", page);
//		if(day1 == null || end1 == null) {
//			behaviorList = orderService.findByOrder(Date.valueOf("2022-01-01"),Date.valueOf("2022-12-31"),page-1,10);
//			model.addAttribute("orders", orderList.getContent());
//			model.addAttribute("totalPage", orderList.getTotalPages());
//			model.addAttribute("currentPage", page);
//		}else {
//			orderList = orderService.findByOrder(Date.valueOf(day1),Date.valueOf(end1),page-1,10);
//			model.addAttribute("orders", orderList.getContent());
//			model.addAttribute("totalPage", orderList.getTotalPages());
//			model.addAttribute("currentPage", page);
//		}
		return "admin/Behavior/Behavior";
	}
	
	@RequestMapping("/admin-detailOrder/{id}")
	public String adminDetailOrder(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("order", orderService.findById(id));
		return "admin/Order/DetailOrder";
	}
	//Xác nhận đơn hàng từ admin
	@RequestMapping("/admin-verifyOrder/{id}")
	public String adminVerifyOrder(@PathVariable("id") Integer id, Model model,Principal p,
			HttpServletRequest request, Authentication authentication,
			@RequestParam(name="page",defaultValue = "1") int page) {
		Order order = orderService.findById(id);
		order.setOrderStatus("Hoàn thành");
		orderService.updateOrder(order);
		// 
		Account account = accountService.findByUsername(p.getName());
		Behavior be = new Behavior();
		be.setAccount(account);
		be.setDescription(request.getRemoteUser() + " đã xác nhận đơn hàng " + order.getOrder_id());
		behaviorService.save(be);
		//
userServices.getUserName(request, authentication);
		//Danh sách đơn hàng đã đặt
		Page<Order> orderList = orderService.findByOrder(page-1,10);
		model.addAttribute("orders", orderList.getContent());
		model.addAttribute("totalPage", orderList.getTotalPages());
		model.addAttribute("currentPage", page);
		try {
			userServices.sendMailPurchaseSuccess(order);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/admin-order";
	}
	//Khôi phục đơn hàng
	@RequestMapping("/admin-restoreOrder/{id}")
	public String adminRestoreOrder(@PathVariable("id") Integer id, Model model,
			HttpServletRequest request, Authentication authentication,
			@RequestParam(name="page",defaultValue = "1") int page) {
		Order order = orderService.findById(id);
		order.setOrderStatus("Chưa thanh toán");
		// Cập nhật độ uy tín
		Account account = accountService.findByUsername(request.getRemoteUser());
		account.setReliability(account.getReliability()-1);
		//Cập nhật số lượng vào product
		List<OrderDetail>details = orderDetailsService.findByDetailId(order.getOrder_id());
		for (int i = 0; i < details.size(); i++) {
			Product product = productService.findById(details.get(i).getProduct().getProduct_id());
			Integer newQuanity = product.getQuantity() - details.get(i).getTotalQuantity();
			productService.updateQuantity(newQuanity, details.get(i).getProduct().getProduct_id());
		}
		//
		try {
			accountService.update(account);
			orderService.updateOrder(order);
			Behavior be = new Behavior();
			be.setAccount(account);
			be.setDescription(request.getRemoteUser() + " đã khôi phục đơn hàng " + order.getOrder_id());
			behaviorService.save(be);
			userServices.getUserName(request, authentication);
			userServices.sendMailRestoreOrder(order);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Danh sách đơn hàng đã đặt
		Page<Order> orderList = orderService.findByOrder(page-1,10);
		model.addAttribute("orders", orderList.getContent());
		model.addAttribute("totalPage", orderList.getTotalPages());
		model.addAttribute("currentPage", page);
		return "admin/Order/Order";
	}
	//Hủy đơn từ admin
	@RequestMapping("/admin-cancelOrder/{id}")
	public String adminCancelOrder(@PathVariable("id") Integer id, Model model,
			HttpServletRequest request, Authentication authentication,
			@RequestParam(name="page",defaultValue = "1") int page) {
		Order order = orderService.findById(id);
		order.setOrderStatus("Đã hủy đơn");
		// Cập nhật độ uy tín
		Account account = accountService.findByUsername(request.getRemoteUser());
		account.setReliability(account.getReliability()+1);
		//Cập nhật số lượng vào product
		List<OrderDetail>details = orderDetailsService.findByDetailId(order.getOrder_id());
		for (int i = 0; i < details.size(); i++) {
			Product product = productService.findById(details.get(i).getProduct().getProduct_id());
Integer newQuanity = product.getQuantity() + details.get(i).getTotalQuantity();
			productService.updateQuantity(newQuanity, details.get(i).getProduct().getProduct_id());
		}
		//
		try {
			accountService.update(account);
			orderService.updateOrder(order);
			Behavior be = new Behavior();
			be.setAccount(account);
			be.setDescription(request.getRemoteUser() + " đã hủy đơn hàng " + order.getOrder_id());
			behaviorService.save(be);
			userServices.getUserName(request, authentication);
			userServices.sendMailCancelOrderOnline(order);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Danh sách đơn hàng đã đặt
		Page<Order> orderList = orderService.findByOrder(page-1,10);
		model.addAttribute("orders", orderList.getContent());
		model.addAttribute("totalPage", orderList.getTotalPages());
		model.addAttribute("currentPage", page);
		return "admin/Order/Order";
	}
	
	//Lọc order theo trạng thái
	//chưa thanh toán
	@RequestMapping("/admin-order/unpaid")
	public String doGetFindAllByOrderUnpaid(Model model,HttpServletRequest request,Authentication authentication,
			 @RequestParam(name="page",defaultValue = "1") int page) {
		userServices.getUserName(request, authentication);
		Page<Order> orderList = orderService.findByOrderStatus("Chưa thanh toán",page-1,10);
		model.addAttribute("orders", orderList.getContent());
		model.addAttribute("totalPage", orderList.getTotalPages());
		model.addAttribute("currentPage", page);
		return "admin/Order/Order";
	}
	//Đã hủy đơn
	@RequestMapping("/admin-order/cancel")
	public String doGetFindAllByOrderCancel(Model model,HttpServletRequest request,Authentication authentication,
			 @RequestParam(name="page",defaultValue = "1") int page) {
		userServices.getUserName(request, authentication);
		Page<Order> orderList = orderService.findByOrderStatus("Đã hủy đơn",page-1,10);
		model.addAttribute("orders", orderList.getContent());
		model.addAttribute("totalPage", orderList.getTotalPages());
		model.addAttribute("currentPage", page);
		return "admin/Order/Order";
	}
	//Hoàn thành
	@RequestMapping("/admin-order/success")
	public String doGetFindAllByOrderSuccess(Model model,HttpServletRequest request,Authentication authentication,
			 @RequestParam(name="page",defaultValue = "1") int page) {
		userServices.getUserName(request, authentication);
		Page<Order> orderList = orderService.findByOrderStatus("Hoàn thành",page-1,10);
		model.addAttribute("orders", orderList.getContent());
		model.addAttribute("totalPage", orderList.getTotalPages());
		model.addAttribute("currentPage", page);
		return "admin/Order/Order";
	}
	
	
	//Chi tiết trạng thái
	@RequestMapping("/detailReceipt")
	public String detailReceipt(Model model, HttpServletRequest request, Authentication authentication,
			@RequestParam(name="page",defaultValue = "1") int page) throws ParseException {		
		userServices.getUserName(request, authentication);
		List<Object[]>orderList = orderService.detailReceipt();
model.addAttribute("ordersAll", orderList);
		List<Object[]>orderListStatus1 = orderService.detailReceiptStatus("Chưa thanh toán");
		model.addAttribute("orderListStatus1", orderListStatus1);
		List<Object[]>orderListStatus2 = orderService.detailReceiptStatus("Hoàn thành");
		model.addAttribute("orderListStatus2", orderListStatus2);
		List<Object[]>orderListStatus3 = orderService.detailReceiptStatus("Đã hủy đơn");
		model.addAttribute("orderListStatus3", orderListStatus3);
//		model.addAttribute("totalPage", orderList.getTotalPages());
//		model.addAttribute("currentPage", page);
		return "admin/Order/DetailReceipt";
	}

}