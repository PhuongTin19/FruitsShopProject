package com.tin.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tin.entity.Account;
import com.tin.entity.Blog;
import com.tin.entity.Brand;

@Repository
public interface BlogRepo extends JpaRepository<Blog,Integer> {
	 
	//Blog đang hoạt động
	@Query(value = "select b from Blog b where is_enable = 0")
	Page<Blog> blogList(Pageable pageable);
	
	//Top 4 blog mới nhất 
	@Query(value="select Top 4 * from blogs where is_enable = 0  order by createdate desc", nativeQuery = true)
	List<Blog>ListNewBlogs();
	//Top 3 blog mới nhất - Trang chủ
	@Query(value="select Top 3 * from blogs where is_enable = 0 order by createdate desc", nativeQuery = true)
	List<Blog>ListNewBlogsHomePage();
	
	@Query(value = "select b from Blog b where b.blog_id = ?1 and is_enable = 0")
	Blog findByBlogId(int id);
	
	@Query(value = "select b from Blog b where b.title like %?1%")
	List<Blog> findByKeyword(String keyword);
	
	@Modifying
	@Query(value="update Blogs set is_enable = 1 where blog_id=?", nativeQuery=true)
	void deleteLogical(Integer id);
	
	@Modifying
	@Query(value="update Blogs set is_enable = 0 where blog_id=?", nativeQuery=true)
	void updateLogical(Integer id);

}
