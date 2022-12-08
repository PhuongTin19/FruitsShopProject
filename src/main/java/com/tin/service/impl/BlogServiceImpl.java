package com.tin.service.impl;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tin.entity.Account;
import com.tin.entity.Blog;
import com.tin.repository.AccountRepo;
import com.tin.repository.BlogRepo;
import com.tin.service.BlogService;

@Service
public class BlogServiceImpl implements BlogService{

	@Autowired
	private BlogRepo blogRepo; 
	
	@Autowired
	private AccountRepo accountRepo; 
	
	@Autowired
	HttpServletRequest request;
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
		return blogRepo.blogList(pageble);
	}
	@Override
	public List<Blog> ListNewBlogsHomePage() {
		return blogRepo.ListNewBlogsHomePage();
	}

	@Override
	public void save(Blog blog) {
		blogRepo.save(blog);
	}
	
	@Override
	public Page<Blog> blogPages(int page) {
		Pageable pageable = PageRequest.of(page, 5);
		return blogRepo.findAll(pageable);
	}
	
	@Override
	public List<Blog> findAllByAdmin() {
		return blogRepo.findAll();
	}
	/****************************/
	@Override
	public Blog findByBlogId(int id) {
		return blogRepo.findByBlogId(id);
	}
	@Override
	public Blog create(Blog blog) {
		Account a = accountRepo.findByUsername(request.getRemoteUser());
		blog.setAccount(a);
		return blogRepo.save(blog);
	}
	@Override
	public Blog update(Blog blog) {
		Account a = accountRepo.findByUsername(request.getRemoteUser());
		blog.setAccount(a);
		return blogRepo.save(blog);
	}
	@Override
	public void delete(int id) {
		blogRepo.deleteById(id);
	}
	
	@Override
	public List<Blog> findByKeyword(String keyword) {
		return blogRepo.findByKeyword(keyword);
	}
	@Override
	@Transactional
	public void deleteLogical(Integer id) {
		blogRepo.deleteLogical(id);
		
	}
	@Override
	@Transactional
	public void updateLogical(Integer id) {
		blogRepo.updateLogical(id);
	}
}
