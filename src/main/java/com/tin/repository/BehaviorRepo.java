package com.tin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tin.entity.Behavior;


public interface BehaviorRepo extends JpaRepository<Behavior,Integer> {
	@Query(value ="Select * from behavior order by createDate desc" ,nativeQuery=true)
	Page<Behavior>findAll(Pageable page);
}
