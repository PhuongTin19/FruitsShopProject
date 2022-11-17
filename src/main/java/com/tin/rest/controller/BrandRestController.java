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
import com.tin.entity.Blog;
import com.tin.entity.Brand;
import com.tin.entity.Product;
import com.tin.service.AccountService;
import com.tin.service.BehaviorService;
import com.tin.service.BrandService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/brands")
public class BrandRestController {

	@Autowired
	BrandService brandService;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	BehaviorService behaviorService;
	
	@GetMapping
	public List<Brand> findAll() {
		return brandService.findAll();
	}
	
	@GetMapping("/enable")
	public List<Brand> findAllEnable() {
		return brandService.findAllByIsEnable();
	}
	
	@GetMapping("{id}")
	public Brand getOne(@PathVariable("id")Integer id) {
		return brandService.findByBrandId(id);
	}
	@PostMapping
	public Brand create(@RequestBody Brand brand) {
		brandService.create(brand);
		Account account = accountService.findByUsername(request.getRemoteUser());
		Behavior be = new Behavior();
		be.setAccount(account);
		be.setDescription(account.getUsername() + " Đã tạo mới một nhãn hàng tên " + brand.getName());
		behaviorService.save(be);
		return brand;
	}
	@PutMapping("{id}")
	public Brand update(@PathVariable("id")Integer id,@RequestBody Brand brand) {
		brandService.update(brand);
		Account account = accountService.findByUsername(request.getRemoteUser());
		Behavior be = new Behavior();
		be.setAccount(account);
		be.setDescription(account.getUsername() + " Đã cập nhật nhãn hàng " + brand.getName());
		behaviorService.save(be);
		return brand;
	}
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id")Integer id) {
		brandService.delete(id);
	}
	@GetMapping("/keyword/{keyword}")
	public List<Brand> getMany(@PathVariable("keyword") String keyword) {
		return brandService.findByKeyword(keyword);
	}
	 @PutMapping("/deleteLogical/{id}") 
	 public void DeleteLogical(@PathVariable("id")Integer id,@RequestBody Brand brand) { 
		 brandService.deleteLogical(id); 
		 Account account = accountService.findByUsername(request.getRemoteUser());
		 Behavior be = new Behavior();
		 be.setAccount(account);
		 be.setDescription(account.getUsername() + " Đã tắt trang thái hoạt động của nhãn hàng " + brand.getName());
		 behaviorService.save(be);
	}
	 @PutMapping("/updateLogical/{id}") 
	 public void updateLogical(@PathVariable("id")Integer id,@RequestBody Brand brand) { 
		 brandService.updateLogical(id); 
	}

}
