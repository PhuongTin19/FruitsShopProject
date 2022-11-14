package com.tin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tin.entity.Discount;
import com.tin.entity.Images;

@Repository
public interface ImageRepo extends JpaRepository<Images,Integer> {

	@Query(value = "select * from images where product_id = ?1" ,nativeQuery=true)
	List<Images>findImageSupport(Integer id);
	
	@Query(value = "select i from Images i where i.image_id = ?1")
	Images findByImageId(int id);
	
	@Query(value = "select i from Images i where i.name like %?1%")
	List<Images> findByKeyword(String keyword);
	
	@Query(value="update Images set is_enable = 1 where image_id=?", nativeQuery=true)
	void deleteLogical(Integer id);
}
