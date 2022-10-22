package com.tin.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tin.entity.Brand;
import com.tin.entity.Category;
import com.tin.entity.Discount;
import com.tin.entity.Product;
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
	
	
	@GetMapping("/admin-products")
	public String adminProducts(Model model) {
		List<Product> products = productService.findAll();
		model.addAttribute("size", products.size());
		model.addAttribute("message","Products list is empty!!!");
		model.addAttribute("products", products);
		model.addAttribute("title", "Manage Products");
		return "admin/products";
	}
	
	@GetMapping("/add-product")
	public String goFormAddProduct(Model model) {
		try {
			List<Category> categories = categoryService.findAllByIsEnable();
			List<Brand> brands = brandService.findAllByIsEnable();
			List<Discount> discounts = discountService.findByIsEnable();
			model.addAttribute("categories", categories);
			model.addAttribute("brands", brands);
			model.addAttribute("discounts", discounts);
			return "admin/add-product";
		} catch (Exception e) {
			return null;
		}
	}
	
	@PostMapping(value = "/add-new-product")
	public String addNewProduct(@ModelAttribute("product") Product product, Model model, 
			RedirectAttributes attributes) {
		
		Product productSave = new Product();
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
		
		return "admin/edit-product";
	}
	
	@PostMapping(value = "/update-product/{id}")
	public String doUpdateProduct(@PathVariable("id") Integer id, 
			@ModelAttribute("product") Product product, 
			Model model,
			RedirectAttributes attributes) {
		try {
			Product productSave = new Product();
			productSave.setProduct_id(id);
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
			
			return "redirect:/admin-products";
		} catch (Exception e) {
			return "redirect:/admin-products";
		}
	}
	

}
