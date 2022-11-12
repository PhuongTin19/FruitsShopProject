package com.tin.service;

import com.tin.entity.Role;

import java.util.List;

public interface RoleService {
	
	Role findByRoleName(String roleName);
	
	void deleteLogical(Integer id);

	List<Role> findAll();
}
