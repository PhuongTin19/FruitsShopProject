package com.tin.service;

import java.util.List;

import com.tin.entity.Images;

public interface ImageService {

	List<Images>findImageSupport(Integer id);
	
	List<Images> findAll();
	
	Images findByImageId(int id);
	
	Images create(Images image);
	
	Images update(Images image);
	
	void delete(int id);
	
	List<Images> findByKeyword(String keyword);
	
	void deleteLogical(Integer id);
}
