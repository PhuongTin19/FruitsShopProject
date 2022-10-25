package com.tin.controller;

import java.awt.Image;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tin.entity.Blog;
import com.tin.entity.Images;
import com.tin.entity.OrderDetail;
import com.tin.entity.Product;
import com.tin.service.BlogService;
import com.tin.service.FavoriteService;
import com.tin.service.ImageService;
import com.tin.service.OrderDetailsService;
import com.tin.service.ProductService;
import com.tin.service.SessionService;

@Controller
public class HomeController {
	@Autowired
	FavoriteService favoriteService;
	@Autowired
	ProductService productService;
	@Autowired
	BlogService blogService;
	@Autowired
	ImageService imageService;
	@Autowired
	OrderDetailsService orderDetailsService;
	@Autowired
	HttpServletRequest request;
	@Autowired
	SessionService sessionService;
	
	@GetMapping("/index")
	public String list(Model model,HttpServletRequest request) {
		// load sản phẩm
		List<Product> list = productService.findProductOutstanding();
		model.addAttribute("items", list);
		
		// loed blog
		List<Blog> listblog = blogService.ListNewBlogsHomePage();
		model.addAttribute("blog", listblog);
		
		//load sản phẩm mới nhất
		List<Product> listProduct = productService.productHomepage();
		model.addAttribute("listProduct", listProduct);
		
		//load sản phẩm thích nhất
		List<Object[]> listProductFavorite = favoriteService.listFavorite();
		model.addAttribute("listProductFavorite", listProductFavorite);
		
		//Top loại hàng nổi bật
		List<Product>slideCategory = productService.slideCategory();
		model.addAttribute("slideCategory", slideCategory); 
		//lượt mua nhiều nhất
		List<Object[]>countMostBuys = productService.countMostBuys();
		model.addAttribute("countMostBuys", countMostBuys);
		return "/user/homepage";
	}
	@GetMapping("/index/product/detail/{id}/{cid}")
	public String detail(Model model, @PathVariable("id") Integer id,@PathVariable("cid") Integer cid) {
		Product item = productService.findById(id);
		model.addAttribute("item", item);
		
		List<Product> list = productService.FindByIdTop4(cid);
		model.addAttribute("listProduct",list);
		
		List<Images>findImageSupport = imageService.findImageSupport(id);
		model.addAttribute("image", findImageSupport);
		return "/user/shop-details";
	}
}