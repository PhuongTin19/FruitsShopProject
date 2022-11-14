package com.tin.service.impl;

import java.util.List;

import javax.transaction.Transactional;

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
	/********************/
	@Override
	public Category save(Category category) {
			return categoryRepo.save(category);
	}
	
	@Override
	public Category findById(Integer id) {
		return categoryRepo.findCategoryById(id);
	}

	@Override
	public Category create(Category category) {
		return categoryRepo.save(category);
	}
	
	@Override
	public Category update(Category category) {
		return categoryRepo.save(category);
	}
	
	@Override
	public void delete(Integer id) {
		categoryRepo.deleteById(id);
	}
	
	@Override
	public List<Category> findByKeyword(String keyword) {
		return categoryRepo.findByKeyword(keyword);
	}

	@Override
	@Transactional
	public void deleteLogical(Integer id) {
		categoryRepo.deleteLogical(id);
	}

	@Override
	@Transactional
	public void updateLogical(Integer id) {
		categoryRepo.updateLogical(id);
	}
	
}
