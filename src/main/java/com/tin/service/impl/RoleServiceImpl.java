package com.tin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tin.entity.Role;
import com.tin.repository.RoleRepo;
import com.tin.service.RoleService;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	RoleRepo roleRepo;
	
	@Override
	public Role findByRoleName(String roleName) {
		return roleRepo.findByRoleName(roleName);
	}

	@Override
	public List<Role> findAll() {
		return roleRepo.findAll();
	}

}
