package com.tin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tin.entity.Account;
import com.tin.entity.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order,Integer> {

	
	@Query("SELECT o FROM Order o WHERE o.account.username=?1 Order by o.order_id DESC")
	Page<Order> findByUsername(String username,Pageable pageable);
	
	@Query("SELECT o FROM Order o WHERE o.account.username=?1 Order by o.order_id DESC")
	List<Order> findByUsernameTracking(String username);
	
	//Tổng lượt mua hàng
//	@Query(value="select count(*) from Orders",nativeQuery=true)
//	Integer countCustomer();
}
