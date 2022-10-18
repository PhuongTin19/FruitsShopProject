package com.tin.service;

import com.tin.entity.Role;

public interface RoleService {
	
	Role findByRoleName(String roleName);
}
