package com.tin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tin.entity.Behavior;

public interface BehaviorService {
	Page<Behavior>findAll(int page, int size);
	
	Behavior save(Behavior behavior);
}
