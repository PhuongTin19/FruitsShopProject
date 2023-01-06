package com.tin.interceptor;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tin.entity.Account;
import com.tin.entity.Category;
import com.tin.entity.Favorite;
import com.tin.entity.Product;
import com.tin.repository.ProductRepo;
import com.tin.service.AccountService;
import com.tin.service.BlogService;
import com.tin.service.CategoryService;
import com.tin.service.DiscountService;
import com.tin.service.FavoriteService;
import com.tin.service.OrderService;
import com.tin.service.ProductService;
import com.tin.service.ReportService;

@Component
public class GlobalInterceptor implements HandlerInterceptor{
	
	@Autowired
	BlogService blogService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private FavoriteService favoriteService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private ReportService reportService;
	
	@Autowired
	private DiscountService discountService;
	
	@Autowired
	HttpSession httpSession;
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//info user
		Account account = accountService.findByUsername(request.getRemoteUser());
		request.setAttribute("account", account);
		//blog mới - Trang blog
		request.setAttribute("newBlog", blogService.ListNewBlogs());
		//sản phẩm mới nhất - Trang blog
		request.setAttribute("listProduct", productService.findByNewProduct());
		//lọc loại hàng - Trang Khuyến Mãi
		request.setAttribute("listCate", categoryService.FillterListCate());
		//Hiện thị trái cây mới nhất - Trang Khuyến Mãi
		request.setAttribute("listFruits", productService.findAllProduct(1));
		//Hiển thị rau xanh mới nhất - Trang Khuyến Mãi
		request.setAttribute("listVegetables", productService.findAllProduct(2));
		//Hiển thị đậu mới nhất - Trang Khuyến Mãi
		request.setAttribute("listBean", productService.findAllProduct(3));
		//
		request.setAttribute("cates",categoryService.FillterListCate());
		//load sản phẩm được yêu thích
		request.setAttribute("countLike", favoriteService.countLike(request.getRemoteUser()));
		//load sản phẩm được yêu thích gg/fb
		if(!ObjectUtils.isEmpty(httpSession.getAttribute("currentUser"))) {
			Account a = (Account)httpSession.getAttribute("currentUser");
			request.setAttribute("countLikeFB_GG", favoriteService.countLike(a.getUsername()));
		}
		//Tên user admin
		request.setAttribute("nameAdmin",request.getRemoteUser() );
		//Thống kê tổng lượt mua hàng
		request.setAttribute("countOrder", orderService.getCountOrderInDay());
		//Thống kê tổng doanh thu
		request.setAttribute("revenue", orderService.getRevenue());
		//Thống tổng số khách hàng
		request.setAttribute("countCustomer", accountService.getCountCustomerInDay());
		//Thống kê hàng tồn kho
		request.setAttribute("Inventory", reportService.getInventoryByCategory());
		//Thống kê lượt yêu thích sản phẩm
		request.setAttribute("statsFavorite", favoriteService.statsFavorite());
	}
	
}
