package com.tin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tin.entity.Account;
import com.tin.entity.Blog;
import com.tin.entity.Brand;

@Repository
public interface BlogRepo extends JpaRepository<Blog,Integer> {
	 
	//Top 4 blog mới nhất 
	@Query(value="select Top 4 * from blogs order by createdate desc", nativeQuery = true)
	List<Blog>ListNewBlogs();
	//Top 3 blog mới nhất - Trang chủ
	@Query(value="select Top 3 * from blogs order by createdate desc", nativeQuery = true)
	List<Blog>ListNewBlogsHomePage();
	
	@Query(value = "select b from Blog b where b.blog_id = ?1")
	Blog findByBlogId(int id);
	
	@Query(value = "select b from Blog b where b.title like %?1%")
	List<Blog> findByKeyword(String keyword);
	

}
