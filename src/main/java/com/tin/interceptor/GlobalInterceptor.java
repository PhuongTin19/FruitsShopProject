package com.tin.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tin.entity.Category;
import com.tin.entity.Product;
import com.tin.service.BlogService;
import com.tin.service.CategoryService;
import com.tin.service.FavoriteService;
import com.tin.service.ProductService;

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
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
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
		request.setAttribute("cates",categoryService.findAll());
		//load sản phẩm được yêu thích
		request.setAttribute("countLike", favoriteService.countLike(request.getRemoteUser()));
		//Tên user admin
		request.setAttribute("nameAdmin",request.getRemoteUser() );

	}
}
