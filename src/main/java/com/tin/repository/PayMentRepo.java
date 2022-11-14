package com.tin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tin.entity.Account;
import com.tin.entity.Payment_Methods;

@Repository
public interface PayMentRepo extends JpaRepository<Payment_Methods,Integer> {
	
}
