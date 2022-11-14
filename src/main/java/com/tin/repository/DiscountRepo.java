package com.tin.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tin.entity.Account;
import com.tin.entity.Brand;
import com.tin.entity.Discount;

@Repository
public interface DiscountRepo extends JpaRepository<Discount,Integer> {
	/*ADMIN*/
//	@Query(value = "select d from Discount d where d.discount_id = ?1")
//	Discount findByDiscount_id(Integer discount_id);

	@Query(value = "update discounts set discount = ?3, name = ?2, start_time = ?4, end_time = ?5, "
			+ "is_enable = ?6 where discount_id = ?1", nativeQuery = true)
	void updateDiscountById(Integer id, String name, Double Discount, Date start_time, 
			Date end_time, Boolean is_enable);
	
	@Query(value = "select d from Discount d where d.is_enable = false")
	List<Discount> findByIsEnable();
	
	@Query(value = "select d from Discount d where d.discount_id = ?1")
	Discount findByDiscoundId(int id);
	
	@Query(value = "select a from Discount a where a.name like %?1%")
	List<Discount> findByKeyword(String keyword);
	
	@Modifying
	@Query(value="update Discounts set is_enable = 1 where discount_id = ?", nativeQuery=true)
	void deleteLogical(Integer id);
	
	@Modifying
	@Query(value="update Discounts set is_enable = 0 where discount_id = ?", nativeQuery=true)
	void updateLogical(Integer id);
}
