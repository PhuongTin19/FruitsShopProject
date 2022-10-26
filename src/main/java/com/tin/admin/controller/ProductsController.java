package com.tin.admin.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tin.custom.UserServices;
import com.tin.entity.Account;
import com.tin.entity.Brand;
import com.tin.entity.Category;
import com.tin.entity.Discount;
import com.tin.entity.Product;
import com.tin.service.AccountService;
import com.tin.service.BrandService;
import com.tin.service.CategoryService;
import com.tin.service.DiscountService;
import com.tin.service.ProductService;

@Controller
public class ProductsController {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private DiscountService discountService;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	ServletContext app;
	
	@GetMapping("/admin-products")
	public String adminProducts(Model model) {
		List<Product> products = productService.findAll();
		model.addAttribute("size", products.size());
		model.addAttribute("message","Products list is empty!!!");
		model.addAttribute("products", products);
		model.addAttribute("title", "Manage Products");
		Product productRequest = new Product();
		model.addAttribute("productRequest", productRequest);
		return "admin/Product/products";
	}
	
	@GetMapping("/add-product")
	public String goFormAddProduct(HttpServletRequest request, Model model, Authentication authentication) {
		try {
			List<Category> categories = categoryService.findAllByIsEnable();
			List<Brand> brands = brandService.findAllByIsEnable();
			List<Discount> discounts = discountService.findByIsEnable();
			model.addAttribute("categories", categories);
			model.addAttribute("brands", brands);
			model.addAttribute("discounts", discounts);
			return "admin/Product/add-product";
		} catch (Exception e) {
			return null;
		}
	}
	
	@PostMapping(value = "/add-new-product")
	public String addNewProduct(@ModelAttribute("product") Product product, Model model, 
			RedirectAttributes attributes,@ModelAttribute("productRequest") Product productRequest,
			@RequestParam("imageFile") MultipartFile image) throws IllegalStateException, IOException {
		System.out.println(product.getName());
		Product productSave = new Product();
		productSave.setName(product.getName());
		
		List<Product> products = productService.findAll();
		for (Product product2 : products) {
			if(product2.getName().equalsIgnoreCase(productSave.getName()) &&
					(product2.getProduct_id() != productSave.getProduct_id())) {
				model.addAttribute("error", "Your product has duplivate name with another product!");
				model.addAttribute("productRequest", productRequest);
				return "admin/Product/add-product";
			}
		}
		
		
		
		
		
		
		
		productSave.setPrice(product.getPrice());

		productSave.setDescription(product.getDescription());
		productSave.setBrand(product.getBrand());
		productSave.setCategory(product.getCategory());
		productSave.setDiscount(product.getDiscount());
		productSave.setIs_enable(product.getIs_enable());
		productSave.setWeight(product.getWeight());
		productSave.setQuantity(product.getQuantity());
		String fileName = StringUtils.cleanPath(image.getOriginalFilename());
		if (fileName.equals("") || fileName.length() == 0 || fileName == null) {
			productSave.setImage("user.png");
		} else {
			productSave.setImage(fileName);
		}		
		productService.save(productSave);
		
		String uploadDir = "C:\\Users\\USUS\\eclipse-workspace\\FruitsShopProject\\src\\main\\resources\\static\\user\\img\\product\\";
		
		Path uploadPath = Paths.get(uploadDir);
		System.out.println(uploadPath);
//		if (!Files.exists(uploadPath)) {
//			Files.createDirectories(uploadPath);
//		}
		try (InputStream inputStream = image.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			
		}

		return "redirect:/admin-products";
	}
	
	@GetMapping("/update-product/{id}")
	public String updateProduct(@PathVariable("id") Integer id, Model model) {
		List<Discount> discounts = discountService.findByIsEnable();
		List<Category> categories = categoryService.findAllByIsEnable();
		List<Brand> brands = brandService.findAllByIsEnable();
		
		Product product = productService.findByProductId(id);
		model.addAttribute("title", "Update Product");
		model.addAttribute("discounts", discounts);
		model.addAttribute("categories", categories);
		model.addAttribute("brands", brands);
		model.addAttribute("product", product);
		
		return "admin/Product/edit-product";
	}
	
	@PostMapping(value = "/update-product/{id}")
	public String doUpdateProduct(@PathVariable("id") Integer id, 
			@ModelAttribute("product") Product product, 
			Model model,@RequestParam("imageFile") MultipartFile image,
			RedirectAttributes attributes) {
		try {
			Product productSave = new Product();
			//Validate
			String fileName = StringUtils.cleanPath(image.getOriginalFilename());
			Product product2 = productService.findById(id);
			if (fileName.equals("") || fileName.length() == 0 || fileName == null) {
				productSave.setImage(product2.getImage());
			}else if(fileName.equalsIgnoreCase(product2.getImage())) {
				System.out.println("Trùng hình");
			}else {
				productSave.setImage(fileName);
			}	
			
			
			//Set giá trị mới
			productSave.setProduct_id(id);
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			productSave.setCreatedate(timestamp);
			productSave.setName(product.getName());
			productSave.setPrice(product.getPrice());
			productSave.setDescription(product.getDescription());
			productSave.setBrand(product.getBrand());
			productSave.setCategory(product.getCategory());
			productSave.setDiscount(product.getDiscount());
			productSave.setIs_enable(product.getIs_enable());
			productSave.setWeight(product.getWeight());
			productSave.setQuantity(product.getQuantity());
			productService.save(productSave);
		
			
			//Lưu hình vào thư mục
			String uploadDir = "C:\\Users\\USUS\\eclipse-workspace\\FruitsShopProject\\src\\main\\resources\\static\\user\\img\\product\\";
			
			Path uploadPath = Paths.get(uploadDir);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			try (InputStream inputStream = image.getInputStream()) {
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return "redirect:/admin-products";
		} catch (Exception e) {
			return "redirect:/admin-products";
		}
	}
	

}
