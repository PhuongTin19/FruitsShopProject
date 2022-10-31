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
import com.tin.entity.Product;
import com.tin.service.CategoryService;
import com.tin.service.ProductService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/categories")
public class CategoryRestController {

	@Autowired
	CategoryService categoryService;
	
	@GetMapping
	public List<Category> findAll() {
		return categoryService.findAll();
	}
	
	@GetMapping("{id}")
	public Category getOne(@PathVariable("id")Integer id) {
		return categoryService.findById(id);
	}
	@PostMapping
	public Category create(@RequestBody Category category) {
		return categoryService.create(category);
	}
	@PutMapping("{id}")
	public Category update(@PathVariable("id")Integer id,@RequestBody Category category) {
		return categoryService.update(category);
	}
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id")Integer id) {
		categoryService.delete(id);
	}
}