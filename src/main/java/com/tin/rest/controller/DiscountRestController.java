package com.tin.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
}
