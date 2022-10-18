package com.tin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tin.entity.Images;
import com.tin.repository.ImageRepo;
import com.tin.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService{

	@Autowired
	ImageRepo imageRepo;
	
	@Override
	public List<Images> findImageSupport(Integer id) {
		return imageRepo.findImageSupport(id);
	}

}
