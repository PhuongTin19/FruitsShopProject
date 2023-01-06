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
import com.tin.entity.Images;
import com.tin.service.AccountService;
import com.tin.service.BehaviorService;
import com.tin.service.ImageService;


@CrossOrigin("*")
@RestController
@RequestMapping("/rest/images")
public class ImageRestController {
	@Autowired
	ImageService imageService;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	BehaviorService behaviorService;
	
	@GetMapping()
	public List<Images> getAll() {
		return imageService.findAll();
	}
	@GetMapping("{id}")
	public Images getOne(@PathVariable("id")Integer id) {
		return imageService.findByImageId(id);
	}
	@PostMapping
	public Images create(@RequestBody Images image) {
		imageService.create(image);
		Account account = accountService.findByUsername(request.getRemoteUser());
		Behavior be = new Behavior();
		be.setAccount(account);
		be.setDescription(account.getUsername() + " Đã thêm mới một hình ảnh tên " + image.getName());
		behaviorService.save(be);
		return image;
	}
	@PutMapping("{id}")
	public Images update(@PathVariable("id")Integer id,@RequestBody Images image) {
		imageService.update(image);
		Account account = accountService.findByUsername(request.getRemoteUser());
		Behavior be = new Behavior();
		be.setAccount(account);
		be.setDescription(account.getUsername() + " Đã sửa một hình ảnh tên " + image.getName());
		behaviorService.save(be);
		return image;
	}
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id")Integer id) {
		imageService.delete(id);
	}
	@GetMapping("/keyword/{keyword}")
	public List<Images> getMany(@PathVariable("keyword") String keyword) {
		return imageService.findByKeyword(keyword);
	}
	
	 @PutMapping("/deleteLogical/{id}") 
	 public void DeleteLogical(@PathVariable("id")Integer id,@RequestBody Images image) { 
		 imageService.deleteLogical(id); 
		 Account account = accountService.findByUsername(request.getRemoteUser()); 
		 Behavior be = new Behavior();
		 be.setAccount(account);
		 be.setDescription(account.getUsername() + " Đã xóa một hình ảnh tên " + image.getName());
		 behaviorService.save(be);
	}
}
