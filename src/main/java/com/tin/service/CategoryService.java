package com.tin.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.tin.entity.Category;
import com.tin.repository.CategoryRepo;

public interface CategoryService {
		List<Category>FillterListCate(); 
		List<Category> findAll();
		

}
