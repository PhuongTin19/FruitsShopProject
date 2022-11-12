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

	@Override
	public List<Images> findAll() {
		return imageRepo.findAll();
	}

	@Override
	public Images findByImageId(int id) {
		return imageRepo.findByImageId(id);
	}

	@Override
	public Images create(Images image) {
		return imageRepo.save(image);
	}

	@Override
	public Images update(Images image) {
		return imageRepo.save(image);
	}

	@Override
	public void delete(int id) {
		imageRepo.deleteById(id);
	}
	
	@Override
	public List<Images> findByKeyword(String keyword) {
		return imageRepo.findByKeyword(keyword);
	}

	@Override
	public void deleteLogical(Integer id) {
		imageRepo.deleteLogical(id);
	}

}
