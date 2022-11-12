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

import com.tin.entity.Account;
import com.tin.entity.Blog;
import com.tin.entity.Brand;
import com.tin.service.BlogService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/blogs")
public class BlogRestController {
	@Autowired
	BlogService blogService;
	
	@GetMapping()
	public List<Blog> getAll() {
		return blogService.findAll();
	}
	@GetMapping("{id}")
	public Blog getOne(@PathVariable("id")Integer id) {
		return blogService.findByBlogId(id);
	}
	@PostMapping
	public Blog create(@RequestBody Blog blog) {
		return blogService.create(blog);
	}
	@PutMapping("{id}")
	public Blog update(@PathVariable("id")Integer id,@RequestBody Blog blog) {
		return blogService.update(blog);
	}
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id")Integer id) {
		blogService.delete(id);
	}
	@GetMapping("/keyword/{keyword}")
	public List<Blog> getMany(@PathVariable("keyword") String keyword) {
		return blogService.findByKeyword(keyword);
	}
	
	@PutMapping("{id}")
	public void DeleteLogical(@PathVariable("id")Integer id,@RequestBody Blog blog) {
		blogService.deleteLogical(id);
	}
}
