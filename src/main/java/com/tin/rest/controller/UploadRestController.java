package com.tin.rest.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tin.entity.Brand;
import com.tin.service.UploadService;
  

@CrossOrigin("*")
@RestController
public class UploadRestController {
	@Autowired
	UploadService uploadService;
	
	@PostMapping("/rest/upload/{folder}")
	public JsonNode upload(@PathParam("file") MultipartFile file, @PathVariable("folder") String folder) throws IOException {
		String uploadDir = "/photos/products/" ;
		String fileName = file.getOriginalFilename();
		Path uploadPath = Paths.get(uploadDir);

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		try {
			InputStream inputStream = file.getInputStream();
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//
		File savedFile = uploadService.save(file, folder);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		
		node.put("name", savedFile.getName());
		node.put("size", savedFile.length());
		
		
		return node;
	}
	@PostMapping("/rest/upload/photos/{folder}")
	public JsonNode upload2(@PathParam("file") MultipartFile file, @PathVariable("folder") String folder) throws IOException {
		String uploadDir = "photos/" ;
		String fileName = file.getOriginalFilename();
		Path uploadPath = Paths.get(uploadDir);

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		try {
			InputStream inputStream = file.getInputStream();
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//
		File savedFile = uploadService.saveImageAccount(file, folder);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		
		node.put("name", savedFile.getName());
		node.put("size", savedFile.length());
		
		
		return node;
	}
	
	@PostMapping("/rest/upload/blog/{folder}")
	public JsonNode upload3(@PathParam("file") MultipartFile file, @PathVariable("folder") String folder) throws IOException {
		String uploadDir = "/photos/blogs/" ;
		String fileName = file.getOriginalFilename();
		Path uploadPath = Paths.get(uploadDir);

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		try {
			InputStream inputStream = file.getInputStream();
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//
		File savedFile = uploadService.saveImageBlog(file, folder);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		
		node.put("name", savedFile.getName());
		node.put("size", savedFile.length());
		
		
		return node;
	}
}