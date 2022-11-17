package com.tin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tin.entity.Account;
import com.tin.entity.OrderDetail;

@Repository
public interface OrderDetailRepo extends JpaRepository<OrderDetail,Integer> {
	
	@Query(value = "select * from Oder_details where order_id = ?1",nativeQuery = true)
	List<OrderDetail> findByDetailId(Integer id);

}
