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
	Brand findByBrandId(Integer id);
}
