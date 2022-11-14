package com.tin.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tin.entity.Blog;

public interface BlogService {
	//Truy vấn tất cả blog
	List<Blog>findAll();
	//Top 4 Blog mới nhất
	List<Blog>ListNewBlogs();
	//chi tiết blog
	Blog findById(Integer id);
	//Phân trang
	Page<Blog> blogList(Pageable pageble);
	//Top 3 Blog mới nhất
	List<Blog>ListNewBlogsHomePage();
	void save(Blog blog);
	Page<Blog> blogPages(int page);
	//
	void deleteLogical(Integer id);
	void updateLogical(Integer id);
	List<Blog> findAllByAdmin();
	/************************************/
	
	Blog findByBlogId(int id);
	Blog create(Blog blog);
	Blog update(Blog blog);
	void delete(int id);
	List<Blog> findByKeyword(String keyword);
	
}
