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
import com.tin.entity.Category;
import com.tin.entity.Discount;
import com.tin.service.AccountService;
import com.tin.service.BehaviorService;
import com.tin.service.CategoryService;
import com.tin.service.DiscountService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/discounts")
public class DiscountRestController {

	@Autowired
	DiscountService discountService;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	BehaviorService behaviorService;
	
	@GetMapping
	public List<Discount> findAll() {
		return discountService.findAll();
	}
	
	@GetMapping("/enable")
	public List<Discount> findAllEnable() {
		return discountService.findByIsEnable();
	}
	
	@GetMapping("{id}")
	public Discount getOne(@PathVariable("id")Integer id) {
		return discountService.findByDiscountId(id);
	}
	@PostMapping
	public Discount create(@RequestBody Discount discount) {
		discountService.create(discount);
		Account account = accountService.findByUsername(request.getRemoteUser());
		Behavior be = new Behavior();
		be.setAccount(account);
		be.setDescription(account.getUsername() + " Đã tạo mới một khuyến mãi tên " + discount.getName());
		behaviorService.save(be);
		return discount;
	}
	@PutMapping("{id}")
	public Discount update(@PathVariable("id")Integer id,@RequestBody Discount discount) {
		discountService.update(discount);
		Account account = accountService.findByUsername(request.getRemoteUser());
		Behavior be = new Behavior();
		be.setAccount(account);
		be.setDescription(account.getUsername() + " Đã cập nhật khuyến mãi tên " + discount.getName());
		behaviorService.save(be);
		return discount;
	}
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id")Integer id) {
		discountService.delete(id);
	}
	@GetMapping("/keyword/{keyword}")
	public List<Discount> getMany(@PathVariable("keyword") String keyword) {
		return discountService.findByKeyword(keyword);
	}
	@PutMapping("/deleteLogical/{id}") 
	public void DeleteLogical(@PathVariable("id")Integer id,@RequestBody Discount discount) { 
		discountService.deleteLogical(id); 
		Account account = accountService.findByUsername(request.getRemoteUser());
		Behavior be = new Behavior();
		be.setAccount(account);
		be.setDescription(account.getUsername() + " Đã tắt khuyến mãi tên " + discount.getName());
		behaviorService.save(be);
	}
	@PutMapping("/updateLogical/{id}") 
	public void updateLogical(@PathVariable("id")Integer id,@RequestBody Discount discount) { 
		discountService.updateLogical(id); 
	}
}
