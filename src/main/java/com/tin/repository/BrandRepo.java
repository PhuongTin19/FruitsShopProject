package com.tin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tin.entity.Account;
import com.tin.entity.Brand;

@Repository
public interface BrandRepo extends JpaRepository<Brand,Integer> {
	/*ADMIN*/

	@Query(value = "select b from Brand b where b.is_enable = false")
	List<Brand> findAllByIsEnable();
	
	@Query(value = "select b from Brand b where b.brand_id = ?1")
	Brand findByBrandId(Integer id);
	
	@Query(value = "select b from Brand b where b.name like %?1%")
	List<Brand> findByKeyword(String keyword);
}
