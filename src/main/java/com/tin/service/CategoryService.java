package com.tin.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.tin.entity.Category;
import com.tin.repository.CategoryRepo;

public interface CategoryService {
	
	List<Category>FillterListCate(); 
	List<Category> findAll();
	/* ADMIN */
	List<Category> adminFindAll();
	List<Category> findAllByIsEnable();
	Category save(Category category);
	Category findCategoryById(int id);
	void updateCategory(int id, String name, Boolean is_enable);

}
