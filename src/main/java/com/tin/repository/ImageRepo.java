package com.tin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tin.entity.Images;

@Repository
public interface ImageRepo extends JpaRepository<Images,Integer> {

	@Query(value = "select * from images where product_id = ?1" ,nativeQuery=true)
	List<Images>findImageSupport(Integer id);
}
