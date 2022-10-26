package com.tin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tin.entity.Report;
import com.tin.repository.ProductRepo;
import com.tin.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService{

	@Autowired
	ProductRepo productRepo;

	@Override
	public List<Report> getInventoryByCategory() {
		return productRepo.getInventoryByCategory();
	}

}
