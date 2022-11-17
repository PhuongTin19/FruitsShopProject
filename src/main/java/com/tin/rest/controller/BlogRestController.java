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
import com.tin.service.AccountService;
import com.tin.service.BehaviorService;
import com.tin.service.BlogService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/blogs")
public class BlogRestController {
	@Autowired
	BlogService blogService;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	BehaviorService behaviorService;
	
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
		blogService.create(blog);
		Account account = accountService.findByUsername(request.getRemoteUser());
		Behavior be = new Behavior();
		be.setAccount(account);
		be.setDescription(account.getUsername() + " Đã tạo mới blog." );
		behaviorService.save(be);
		return blog;
	}
	
	@PutMapping("{id}")
	public Blog update(@PathVariable("id")Integer id,@RequestBody Blog blog) {
		blogService.update(blog);
		Account account = accountService.findByUsername(request.getRemoteUser());
		Behavior be = new Behavior();
		be.setAccount(account);
		be.setDescription(account.getUsername() + " Đã cập nhật blog có id " + blog.getBlog_id());
		behaviorService.save(be);
		return blog;
	}
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id")Integer id) {
		blogService.delete(id);
	}
	@GetMapping("/keyword/{keyword}")
	public List<Blog> getMany(@PathVariable("keyword") String keyword) {
		return blogService.findByKeyword(keyword);
	}
	
	 @PutMapping("/deleteLogical/{id}") 
	 public void DeleteLogical(@PathVariable("id")Integer id,@RequestBody Blog blog) { 
		 blogService.deleteLogical(id);
		 Account account = accountService.findByUsername(request.getRemoteUser());
		 Behavior be = new Behavior();
		 be.setAccount(account);
		 be.setDescription(account.getUsername() + " Đã tắt hoạt động của blog có id " + blog.getBlog_id());
		 behaviorService.save(be);
	 }
	 
	 @PutMapping("/updateLogical/{id}") 
	 public void updateLogical(@PathVariable("id")Integer id,@RequestBody Blog blog) { 
		 blogService.updateLogical(id);
	 }
}
