package com.tin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tin.entity.Account;
import com.tin.entity.Brand;
import com.tin.entity.Provider;

@Repository
public interface AccountRepo extends JpaRepository<Account,Integer> {
	//Tìm Email
	public Account findByEmail(String email);
  
	//Xác định mã code của account
	public Account findByVerificationCode(String code);
	//
	Account findByUsername(String username);
	//
	@Query(value="select password from accounts where username= ?1",nativeQuery = true)
	String findByPassword(String username);
	//
	@Query(value = "select a from Account a where a.is_enable = 0 and a.role.role_id = 1")
	List<Account> findAllEnable();
	//change password
	@Modifying
	@Query(value="update accounts set password = ?1 where username = ?2",nativeQuery = true)
	void updatePassword(String password,String username);
	//edit tài khoản
	@Modifying
    @Query("UPDATE Account u SET u.provider = ?2 WHERE u.username = ?1")
    void updateProviderType(String username, Provider provider);
	//
	@Modifying(clearAutomatically =true)
    @Query(value ="UPDATE Accounts SET fullname = ?1, email = ?2, phone = ?3, address= ?4 WHERE username = ?5", nativeQuery = true)
    void updateNonPass(String fullname, String email, String phone,String address, String username);

	@Modifying(clearAutomatically =true)
    @Query(value ="UPDATE Accounts SET reliability = ?1 WHERE username = ?2", nativeQuery = true)
    void updateReliability(Integer reliability,String username);
	
    @Modifying(clearAutomatically =true)
    @Query(value="UPDATE Accounts SET fullname = ?1, email = ?2,password = ?3, phone = ?4, address = ?5, image = ?6,username=?7 WHERE username = ?8", nativeQuery = true)
    void update (String fullname, String email,String password, String phone,String address, String img,String username1 ,String username2);

	@Query(value = "select a from Account a where a.account_id = ?1")
	Account findByAccountId(Integer id);
	
	@Query(value = "select a from Account a where a.phone = ?1")
	Account findByPhone(String phone);

	@Query(value = "select * from accounts a inner join roles r on a.role_id = r.role_id where r.role_id = 1", nativeQuery = true)
	List<Account> findAllByRoleAdmin();
	
	//Thống kê tổng số khách hàng
    @Query(value = "{CALL sp_getCountCustomerInDay()}", nativeQuery = true)
	Integer getCountCustomerInDay();
    
	@Query(value = "select a from Account a where a.username like %?1% and a.role.role_id = ?2")
	List<Account> findByKeyword(String keyword,Integer roleId);
	
	@Modifying
	@Query(value="update Accounts set reliability=5 where account_id=?", nativeQuery=true)
	void deleteLogical(Integer id);
	
	@Modifying
	@Query(value="update Accounts set reliability=0 where account_id=?", nativeQuery=true)
	void updateLogical(Integer id);
	
	@Query(value="SELECT top 1 orderStatus from Orders where account_id = ?1  order by orderDate desc", nativeQuery=true)
	String CheckOrderStatus(Integer id);
	//account theo role
	@Query("SELECT a from Account a where a.role.role_id = ?1")
	List<Account>findAllByRole(Integer role);
}

