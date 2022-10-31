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

import com.tin.entity.Images;
import com.tin.service.ImageService;


@CrossOrigin("*")
@RestController
@RequestMapping("/rest/images")
public class ImageRestController {
	@Autowired
	ImageService imageService;
	
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
		return imageService.create(image);
	}
	@PutMapping("{id}")
	public Images update(@PathVariable("id")Integer id,@RequestBody Images image) {
		return imageService.update(image);
	}
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id")Integer id) {
		imageService.delete(id);
	}
}
