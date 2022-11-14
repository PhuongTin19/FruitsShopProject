package com.tin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tin.entity.Account;
import com.tin.entity.Brand;
import com.tin.entity.Category;
import com.tin.entity.Product;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Integer> {

	@Query(value= "select * from categories where is_enable = 0", nativeQuery = true)
	List<Category>FillterListCate(); 
	
	/*ADMIN*/
	
	@Query(value = "select c from Category c where c.is_enable = false")
	List<Category> findAllByIsEnable();
	
	@Query(value = "select c from Category c where c.category_id = ?1")
	Category findCategoryById(int id);
	
	@Query(value = "update categories  set name = ?2, is_enable = ?3 where category_id = ?1", nativeQuery = true)
	Category updateCategory(int id, String name, Boolean is_enable);
	
	@Query(value = "select c from Category c where c.name like %?1%")
	List<Category> findByKeyword(String keyword);
	
	@Modifying
	@Query(value="update Categories set is_enable = 1 where category_id=?", nativeQuery=true)
	void deleteLogical(Integer id);
	
	@Modifying
	@Query(value="update Categories set is_enable = 0 where category_id=?", nativeQuery=true)
	void updateLogical(Integer id);
}
