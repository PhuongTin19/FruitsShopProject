package com.tin.service.impl;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tin.service.UploadService;


@Service
public class UploadServiceImpl implements UploadService{
	@Autowired
	ServletContext app;

	public File save(MultipartFile file, String folder) {
		File dir = new File(app.getRealPath("/photos/products/" + folder ));
		if(!dir.exists()) {
			dir.mkdirs();
		}
		String s = file.getOriginalFilename();
		System.out.println(s);
		try {
			File savedFile = new File(dir, s);
			file.transferTo(savedFile);
			System.out.println(savedFile.getAbsolutePath());
			return savedFile;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public File saveImageAccount(MultipartFile file, String folder) {
		File dir = new File(app.getRealPath("/photos/" + folder ));
		if(!dir.exists()) {
			dir.mkdirs();
		}
		String s = file.getOriginalFilename();
		System.out.println(s);
		try {
			File savedFile = new File(dir, s);
			file.transferTo(savedFile);
			System.out.println(savedFile.getAbsolutePath());
			return savedFile;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public File saveImageBlog(MultipartFile file, String folder) {
		File dir = new File(app.getRealPath("/photos/blogs/" + folder ));
		if(!dir.exists()) {
			dir.mkdirs();
		}
		String s = file.getOriginalFilename();
		System.out.println(s);
		try {
			File savedFile = new File(dir, s);
			file.transferTo(savedFile);
			System.out.println(savedFile.getAbsolutePath());
			return savedFile;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


}