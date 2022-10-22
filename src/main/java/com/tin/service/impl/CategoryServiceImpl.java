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
	
	/* ADMIN */
	@Override
	public List<Category> adminFindAll() {
		return categoryRepo.findAll();
	}
	
	@Override
	public List<Category> findAllByIsEnable() {
		return categoryRepo.findAllByIsEnable();
	}
	
	@Override
	public Category save(Category category) {
		try {
			Category categorySave = new Category(null, null);
			categorySave.setName(category.getName());
			categorySave.setIs_enable(category.getIs_enable());
			return categoryRepo.save(categorySave);	
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Category findCategoryById(int id) {
		try {
			Category category = categoryRepo.findCategoryById(id);
			return category;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void updateCategory(int id, String name, Boolean is_enable) {
		try {
			Category category = new Category(id, name, is_enable);
			categoryRepo.updateCategory(id, name, is_enable);
			//return category;
		} catch (Exception e) {
			e.printStackTrace();
			//return null;
		}
	}
}
