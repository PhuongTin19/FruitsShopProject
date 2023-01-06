package com.tin.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tin.entity.Account;
import com.tin.entity.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {

	@Query("SELECT o FROM Order o WHERE o.account.username=?1 Order by o.order_id DESC")
	Page<Order> findByUsername(String username, Pageable pageable);

	@Query("SELECT o FROM Order o WHERE o.account.username=?1 Order by o.order_id DESC")
	List<Order> findByUsernameList(String username);
	
	@Query("select o from Order o where order_id=?1")
	Page<Order> findByOrderID(Integer oid, Pageable page);
	// Tất cả đơn đặt hàng
	@Query("SELECT o FROM Order o WHERE o.orderdate between ?1 and ?2 Order by o.orderdate DESC")
	Page<Order> findByOrder(Date startDate, Date endDate, Pageable page);

	@Query("SELECT o FROM Order o  Order by o.orderdate DESC")
	Page<Order> findByOrder(Pageable page);

	// đơn đặt theo trạng thái
	@Query("SELECT o FROM Order o where o.orderStatus = ?1 Order by o.orderdate DESC")
	Page<Order> findByOrderStatus(String status, Pageable page);

	@Query("SELECT o FROM Order o WHERE o.account.username=?1")
	Order findByUsernameTracking(String username);

	@Query("SELECT u FROM Order u WHERE u.verificationCode = ?1")
	public Order findByVerificationCode(String code);

	// Thống kê tổng lượt order
	@Query(value = "{CALL sp_getCountOrderInDay()}", nativeQuery = true)
	Integer getCountOrderInDay();

	// Thống kê tổng doanh thu
	@Query(value = "{CALL sp_getRevenue()}", nativeQuery = true)
	Double getRevenue();
	
	//Thống kê doanh thu biểu đồ
	@Query(value = "{CALL sp_getTotalPricePerMonth(:month, :year)}", nativeQuery = true)
	String getTotalPricePerMonth(@Param("month") String month, @Param("year") String year);
	//Thống kê theo tình trạng đơn hàng
	@Query(value = "{CALL sp_statsOrderStatus()}", nativeQuery = true)
	String[][] statsOrderStatus();
	//Thống kê doanh thu theo loại
	@Query(value = "{CALL sp_statsRevenueProductsByCates()}", nativeQuery = true)
	String[][] statsRevenueProductsByCates();
	//Lọc doanh thu tổng 
	@Query(value = "SELECT orderDate, SUM(od.totalPrice) \r\n"
			+ "FROM orders o inner join Oder_details od on o.order_id = od.order_id\r\n"
			+ "WHERE o.orderdate between ?1 and ?2\r\n"
			+ "group by orderdate",nativeQuery = true)
	String[][] getTotalPriceFromTo(String from, String to);
	//Lọc doanh thu loại
	@Query(value = "select c.name,sum(od.totalPrice) from Categories c\r\n"
			+ "			inner join Products p \r\n"
			+ "			on c.category_id = p.category_id\r\n"
			+ "			inner join Oder_details od \r\n"
+ "			on od.product_id = p.product_id\r\n"
			+ "			inner join Orders o\r\n"
			+ "			on od.order_id = o.order_id\r\n"
			+ "			where o.orderStatus = 'Hoàn thành' and o.orderDate between ?1 and ?2\r\n"
			+ "			group by c.name",nativeQuery = true)
	String[][] getProductsByCatesFromTo(String from, String to);
	//Tổng hóa đơn
	@Query("select o.account.username,COUNT(o.orderStatus) from Order o\r\n"
			+ "	group by o.account.username,o.account.account_id \r\n"
			+ "	order by o.account.account_id ASC")
	List<Object[]> detailReceipt();
	//Tổng số hóa đơn theo trạng thái
	@Query("select o.account.username,COUNT(o.orderStatus) from Order o\r\n "
			+ "where o.orderStatus = ?1"
			+ "	group by o.account.username,o.account.account_id \r\n"
			+ "	order by o.account.account_id ASC")
	List<Object[]> detailReceiptStatus(String orderStatus);
	// Tổng lượt mua hàng
//	@Query(value="select count(*) from Orders",nativeQuery=true)
//	Integer countCustomer();
}