package com.tin.rest.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tin.entity.Account;
import com.tin.entity.Behavior;
import com.tin.entity.Brand;
import com.tin.entity.Product;
import com.tin.service.AccountService;
import com.tin.service.BehaviorService;
import com.tin.service.ProductService;


@CrossOrigin("*")
@RestController
@RequestMapping("/rest/products")
public class ProductRestController {
	@Autowired
	ProductService productService;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	BehaviorService behaviorService;
	
	@GetMapping()
	public List<Product> getAll() {
		return productService.findAll();
	}
	
	@GetMapping("/enable")
	public List<Product> getAllEnable() {
		return productService.findProductEnable();
	}

	@GetMapping("{id}")
	public Product getOne(@PathVariable("id")Integer id) {
		return productService.findById(id);
	}
	@PostMapping
	public Product create(@RequestBody Product product) {
		productService.create(product);
		Account account = accountService.findByUsername(request.getRemoteUser());
		Behavior be = new Behavior();
		be.setAccount(account);
		be.setDescription(account.getUsername() + " Đã thêm mới một sản phẩm tên " + product.getName());
		behaviorService.save(be);
		return product;
	}
	@PutMapping("{id}")
	public Product update(@PathVariable("id")Integer id,@RequestBody Product product) {
		productService.update(product);
		Account account = accountService.findByUsername(request.getRemoteUser());
		Behavior be = new Behavior();
		be.setAccount(account);
		be.setDescription(account.getUsername() + " Đã cập nhật sản phẩm tên " + product.getName());
		behaviorService.save(be);
		return product;
	}
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id")Integer id) {
		productService.delete(id);
	}
	@GetMapping("/keyword/{keyword}")
	public List<Product> getMany(@PathVariable("keyword") String keyword) {
		return productService.findByKeyword(keyword);
	}
	@PutMapping("/deleteLogical/{id}") 
	public void DeleteLogical(@PathVariable("id")Integer id,@RequestBody Product product) { 
		productService.deleteLogical(id); 
		Account account = accountService.findByUsername(request.getRemoteUser());
		Behavior be = new Behavior();
		be.setAccount(account);
		be.setDescription(account.getUsername() + " Đã tắt hoạt động sản phẩm tên " + product.getName());
		behaviorService.save(be);
	}
	
	@PutMapping("/updateLogical/{id}") 
	public void updateLogical(@PathVariable("id")Integer id,@RequestBody Product product) { 
		productService.updateLogical(id); 
	}
}
