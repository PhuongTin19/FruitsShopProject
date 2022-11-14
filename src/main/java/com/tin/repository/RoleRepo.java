package com.tin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tin.entity.Account;
import com.tin.entity.Images;
import com.tin.entity.Role;

import java.util.List;

@Repository
public interface RoleRepo extends JpaRepository<Role,Integer> {
	
	@Query("SELECT u FROM Role u WHERE u.name = ?1")
    Role findByRoleName(String roleName);

}
