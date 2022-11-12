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

import com.tin.entity.Blog;
import com.tin.entity.Brand;
import com.tin.entity.Product;
import com.tin.service.BrandService;

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
	@GetMapping("{id}")
	public Brand getOne(@PathVariable("id")Integer id) {
		return brandService.findByBrandId(id);
	}
	@PostMapping
	public Brand create(@RequestBody Brand brand) {
		return brandService.create(brand);
	}
	@PutMapping("{id}")
	public Brand update(@PathVariable("id")Integer id,@RequestBody Brand brand) {
		return brandService.update(brand);
	}
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id")Integer id) {
		brandService.delete(id);
	}
	@GetMapping("/keyword/{keyword}")
	public List<Brand> getMany(@PathVariable("keyword") String keyword) {
		return brandService.findByKeyword(keyword);
	}
	@PutMapping("{id}")
	public void DeleteLogical(@PathVariable("id")Integer id,@RequestBody Brand brand) {
		brandService.deleteLogical(id);
	}
}
