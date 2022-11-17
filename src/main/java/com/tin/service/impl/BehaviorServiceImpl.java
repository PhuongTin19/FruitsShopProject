package com.tin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tin.entity.Behavior;
import com.tin.repository.BehaviorRepo;
import com.tin.service.BehaviorService;

@Service
public class BehaviorServiceImpl implements BehaviorService{
	
	@Autowired
	private BehaviorRepo behaviorRepo;
	
	@Override
	public Page<Behavior> findAll(int page, int size) {
		return behaviorRepo.findAll(PageRequest.of(page, size));
	}

	@Override
	public Behavior save(Behavior behavior) {
		return behaviorRepo.save(behavior);
	}

}
