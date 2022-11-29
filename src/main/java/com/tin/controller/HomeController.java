package com.tin.controller;

import java.awt.Image;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tin.entity.Account;
import com.tin.entity.Blog;
import com.tin.entity.Discount;
import com.tin.entity.Favorite;
import com.tin.entity.Images;
import com.tin.entity.OrderDetail;
import com.tin.entity.Product;
import com.tin.service.AccountService;
import com.tin.service.BlogService;
import com.tin.service.DiscountService;
import com.tin.service.FavoriteService;
import com.tin.service.ImageService;
import com.tin.service.OrderDetailsService;
import com.tin.service.OrderService;
import com.tin.service.ProductService;
import com.tin.service.ReportService;
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
	@Autowired
	AccountService accountService;
	@Autowired
	OrderService orderService;
	@Autowired
	ReportService reportService;
	@Autowired
	DiscountService discountService;
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
		//disable tự động discount hết hạn 
		try {
			disableDiscountAuto();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
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
	
	public void disableDiscountAuto() {
		try {
			long millis=System.currentTimeMillis();   
			List<Discount>discounts = discountService.findAll();
			java.sql.Date date=new java.sql.Date(millis); 
			TimerTask timerTask = new TimerTask() {
	            @Override
	            public void run() {
	            	for (int i = 0; i < discounts.size(); i++) {
	            		if(discounts.get(i).getEnd_time().before(date)) {
	                		discountService.deleteLogical(discounts.get(i).getDiscount_id());
	                	}else if(discounts.get(i).getStart_time().after(date)) {
	                		discountService.deleteLogical(discounts.get(i).getDiscount_id());
	                	}else if(discounts.get(i).getStart_time().before(date)) {
	                		discountService.updateLogical(discounts.get(i).getDiscount_id());
	                	}
					} 
	            }
	        };
	        long delay = 100000L;
	        Timer timer = new Timer("Timer");
	        timer.schedule(timerTask, 0, delay); 
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	
}
