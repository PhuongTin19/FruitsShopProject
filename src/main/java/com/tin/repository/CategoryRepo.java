package com.tin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tin.entity.Account;
import com.tin.entity.Category;
import com.tin.entity.Product;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Integer> {

	@Query(value= "select * from categories where is_enable = 1", nativeQuery = true)
	List<Category>FillterListCate(); 
}
