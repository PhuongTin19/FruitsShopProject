package com.tin.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tin.entity.Account;
import com.tin.entity.Blog;
import com.tin.entity.Favorite;
import com.tin.entity.Product;
import com.tin.repository.ProductRepo;
import com.tin.service.AccountService;
import com.tin.service.BlogService;
import com.tin.service.CategoryService;
import com.tin.service.FavoriteService;
import com.tin.service.ProductService;
import com.tin.service.SessionService;

@Controller
public class PageController {

	@Autowired
	private BlogService blogService;

	@Autowired
	private ProductService productService;
	 
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private FavoriteService favoriteService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	SessionService sessionService;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	HttpServletRequest request;

	// trang blog
	@GetMapping("/user/blog")
	public String doGetBlog(Model model, @RequestParam("p") Optional<Integer> p) {
		Pageable pageable = PageRequest.of(p.orElse(0), 6);
		Page<Blog> blogList = blogService.blogList(pageable);
		model.addAttribute("blogs", blogList);
		return "/user/blog";
	}

	// trang blog chi tiết
	@GetMapping("/user/blog/{id}")
	public String doGetDetailBlog(Model model, @PathVariable("id") Integer id) {
		Blog itemBlog = blogService.findById(id);
		model.addAttribute("detailBlog", itemBlog);
		return "/user/blog-details";
	}

	// trang mặt hàng
	@Autowired
	ProductRepo productRepo;
	@RequestMapping("/user/product")
	public String doGetDiscount(Model model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("keywords") Optional<String> kw) {
		String kwords = kw.orElse(sessionService.get("keywords",""));
		
//		session.set("keywords", kwords);
		Pageable pageable = PageRequest.of(page.orElse(0),9);
		Page<Product> productDiscountList = productService.findAllByNameLike("%"+kwords+"%", pageable);
		model.addAttribute("discountList", productDiscountList);
		if(productDiscountList.isEmpty()) {
			model.addAttribute("noKW","Không có sản phẩm bạn tìm"); 
		}
		return "/user/shop-grid";
	}

	// Lọc theo loại
	@GetMapping("/user/product/{cateId}")
	public String doPostDiscountFilterByCate(Model model, @RequestParam("page") Optional<Integer> page,
			@PathVariable("cateId") Integer cateId) {
		Pageable pageable = PageRequest.of(page.orElse(0), 9);
		Page<Product> listProductCate = productService.filterByCate(cateId, pageable);
		model.addAttribute("discountList", listProductCate);
		return "/user/shop-grid";
	}

	// Lọc theo giá
	@PostMapping("/user/discount/fillterByPrice")
	public String doPostDiscountFilterByPrice(Model model, @RequestParam("page") Optional<Integer> page) {
		Pageable pageable = PageRequest.of(page.orElse(0), 10);
		String min = request.getParameter("min");
		String max = request.getParameter("max");
		// Xóa kí tự đô la
		min = min.replace("đ", "");
		max = max.replace("đ", "");
		Page<Product> fillterByPrice = productService.filterByPrice(Double.parseDouble(max), Double.parseDouble(min),pageable);
		model.addAttribute("discountList", fillterByPrice);
		return "/user/shop-grid";
	}

	// trang yêu thích	
//	@GetMapping("/user/favorite")
//	public String doGetFavorite(Model model,
//			@RequestParam("id") Integer id) {
//		List<Object[]>listFavorite = favoriteService.findAllFavorite(id);
//		model.addAttribute("favorite", listFavorite);
//		return "/user/favorite";
//	}
	
	@GetMapping("/user/favorite")
	public String doGetFavorite(Model model,HttpServletRequest request) {
		Account account = accountService.findByUsername(request.getRemoteUser());
		if(account!=null) {
			List<Object[]>listFavorite = favoriteService.findAllFavorite(account.getAccount_id());
			model.addAttribute("favorite", listFavorite);
		}else {
			Account account2 = (Account)session.getAttribute("currentUser");
			List<Object[]>listFavorite = favoriteService.findAllFavorite(account2.getAccount_id());
			model.addAttribute("favorite", listFavorite);
		}
		return "/user/favorite";
	}
	
	//Like
	@GetMapping("/user/favorite/like")
	public String doGetLike(Model model,@RequestParam("pid") Integer pid,
			@RequestParam("id") Integer id) {
		List<Favorite>favorites = favoriteService.CheckExistProducts(id);
		System.out.println("pid:"+pid);
		System.out.println("fs:"+favorites.size());
		if(favorites.size() == 0) {
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			favoriteService.LikeProducts(id,pid,timestamp);
		}
		for (int i = 0; i < favorites.size(); i++) {
			System.out.println(pid); 
			System.out.println("list:"+favorites.get(i).getProduct().getProduct_id());
			if(pid == favorites.get(i).getProduct().getProduct_id()) {
				favoriteService.deleteFavorites(id,pid);
			}else {
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				favoriteService.LikeProducts(id,pid,timestamp);
			} 
		}
		return "redirect:/index";
	}

	 
	//Unlike
	@GetMapping("/user/favorite/unlike/{pid}")
	public String doGetUnLike(Model model,@PathVariable("pid") Integer pid,
			@RequestParam("id") Integer id) {
		favoriteService.deleteFavorites(id,pid);
		return "redirect:/user/favorite";
	}
}
