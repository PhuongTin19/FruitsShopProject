package com.tin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tin.entity.Brand;
import com.tin.repository.BrandRepo;
import com.tin.service.BrandService;

@Service
public class BrandServiceImpl implements BrandService{
	@Autowired
	private BrandRepo brandRepo; 

	/*ADMIN*/
	@Override
	public List<Brand> findAllByIsEnable() {
		return brandRepo.findAllByIsEnable();
	}
	
	@Override
	public List<Brand> findAll() {
		return brandRepo.findAll();
	}
	
	@Override
	public void save(Brand brand) {
		brandRepo.save(brand);
	}
	
	@Override
	public Brand findByBrandId(Integer id) {
		return brandRepo.findByBrandId(id);
	}
}
