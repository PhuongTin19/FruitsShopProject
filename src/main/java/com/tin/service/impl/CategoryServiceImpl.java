package com.tin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tin.entity.Category;
import com.tin.repository.CategoryRepo;
import com.tin.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	CategoryRepo categoryRepo;

	@Override
	public List<Category> findAll() {
		return categoryRepo.findAll();
	}
	
	@Override
	public List<Category> FillterListCate() {
		return categoryRepo.FillterListCate();
	}
}
