package com.tin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tin.entity.Brand;

@Service
public interface BrandService {

	/*ADMIN*/

	List<Brand> findAllByIsEnable();
	List<Brand> findAll();
	void save(Brand brand);
	//Brand findByBrandId(Integer id);
	Brand findByBrandId(int id);
	Brand create(Brand brand);
	Brand update(Brand brand);
	void delete(int id);
	List<Brand> findByKeyword(String keyword);
	void deleteLogical(Integer id);
	void updateLogical(Integer id);
}
