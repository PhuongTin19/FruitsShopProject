package com.tin.rest.controller;

import java.util.List;

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

import com.tin.entity.Category;
import com.tin.entity.Discount;
import com.tin.service.CategoryService;
import com.tin.service.DiscountService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/discounts")
public class DiscountRestController {

	@Autowired
	DiscountService discountService;
	
	@GetMapping
	public List<Discount> findAll() {
		return discountService.findAll();
	}
	
	@GetMapping("{id}")
	public Discount getOne(@PathVariable("id")Integer id) {
		return discountService.findByDiscountId(id);
	}
	@PostMapping
	public Discount create(@RequestBody Discount disount) {
		return discountService.create(disount);
	}
	@PutMapping("{id}")
	public Discount update(@PathVariable("id")Integer id,@RequestBody Discount disount) {
		return discountService.update(disount);
	}
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id")Integer id) {
		discountService.delete(id);
	}
	@GetMapping("/keyword/{keyword}")
	public List<Discount> getMany(@PathVariable("keyword") String keyword) {
		return discountService.findByKeyword(keyword);
	}
}
