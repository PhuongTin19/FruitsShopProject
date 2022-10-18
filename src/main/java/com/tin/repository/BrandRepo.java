package com.tin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tin.entity.Account;
import com.tin.entity.Brand;

@Repository
public interface BrandRepo extends JpaRepository<Brand,Integer> {

}
