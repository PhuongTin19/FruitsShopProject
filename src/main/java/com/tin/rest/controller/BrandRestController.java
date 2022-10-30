package com.tin.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tin.entity.Brand;
import com.tin.entity.Category;
import com.tin.service.BrandService;
import com.tin.service.DiscountService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/brands")
public class BrandRestController {

	@Autowired
	BrandService brandService;
	
	@GetMapping
	public List<Brand> findAll() {
		return brandService.findAll();
	}
}
