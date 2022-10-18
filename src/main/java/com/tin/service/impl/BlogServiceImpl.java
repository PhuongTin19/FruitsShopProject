package com.tin.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tin.entity.Blog;
import com.tin.repository.BlogRepo;
import com.tin.service.BlogService;

@Service
public class BlogServiceImpl implements BlogService{

	@Autowired
	private BlogRepo blogRepo; 
	
	//Truy vấn tất cả blog
	@Override
	public List<Blog> findAll() {
		return blogRepo.findAll();
	}
	//Top 4 Blog mới nhất
	@Override
	public List<Blog> ListNewBlogs() {
		return blogRepo.ListNewBlogs();
	}
	@Override
	public Blog findById(Integer id) {
		return blogRepo.findById(id).get();
	}
	@Override
	public Page<Blog> blogList(Pageable pageble) {
		return blogRepo.findAll(pageble);
	}
	@Override
	public List<Blog> ListNewBlogsHomePage() {
		return blogRepo.ListNewBlogsHomePage();
	}
	



}
