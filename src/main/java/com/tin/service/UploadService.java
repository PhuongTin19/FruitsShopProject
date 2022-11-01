package com.tin.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
	public File save(MultipartFile file, String folder) ;
	
	public File saveImageAccount(MultipartFile file, String folder) ;
	
	public File saveImageBlog(MultipartFile file, String folder) ;
}