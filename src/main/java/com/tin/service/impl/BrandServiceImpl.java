package com.tin.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tin.entity.Brand;
import com.tin.repository.BrandRepo;
import com.tin.service.BrandService;

@Service
public class BrandServiceImpl implements BrandService{
	@Autowired
	private BrandRepo brandRepo; 

//	/*ADMIN*/
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
	public Brand findByBrandId(int id) {
		return brandRepo.findByBrandId(id);
	}

	@Override
	public Brand create(Brand brand) {
		return brandRepo.save(brand);
	}

	@Override
	public Brand update(Brand brand) {
		return brandRepo.save(brand);
	}

	@Override
	public void delete(int id) {
		brandRepo.deleteById(id);;
		
	}

	@Override
	public List<Brand> findByKeyword(String keyword) {
		return brandRepo.findByKeyword(keyword);
	}

	@Override
	@Transactional
	public void deleteLogical(Integer id) {
		brandRepo.deleteLogical(id);
	}

	@Override
	@Transactional
	public void updateLogical(Integer id) {
		brandRepo.updateLogical(id);
	}
	
//	@Override
//	public Brand findByBrandId(Integer id) {
//		return brandRepo.findByBrandId(id);
//	}
	
	
}
