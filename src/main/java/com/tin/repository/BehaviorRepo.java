package com.tin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tin.entity.Behavior;


public interface BehaviorRepo extends JpaRepository<Behavior,Integer> {

}
