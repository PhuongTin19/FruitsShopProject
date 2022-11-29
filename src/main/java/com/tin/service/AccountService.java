package com.tin.service;

import com.tin.entity.Account;
import com.tin.entity.Product;
import com.tin.entity.Provider;

import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

public interface AccountService {
	
	//Cập nhật quên pass
    Account updateAccount(Account account);

    //Xác định mã code của account
    Account findByVerificationCode(String code);
    //Tìm theo phone
    Account findByPhone(String phone);
    //Tìm Email
    Account findByEmail(String email);
    //tìm theo username
    Account findByUsername(String username);
    //
    String findByPassword(String username);
    //kiểm tra usernam - đọc pass mã hóa 
    Account doLogin(String username, String password);
    //Đổi mật khẩu
    void updatePassword(String password,String username);
    //update độ uy tín
    void updateReliability(Integer reliability,String username);
    //đăng ký
    Account createAccount(Account account);
    //cập nhật tài khoản
    Account EditAccount(String username);
    //
    void updateProviderType(String username, Provider provider);
    //
    void update(Account account) throws Exception;
    //
    /*ADMIN*/
    void deleteLogical(Integer id);
    
    List<Account> findAll();
    void save(Account account);
    Account findById(Integer id);
    List<Account> findAllByRoleAdmin();
    Integer getCountCustomerInDay();
    /*--------------------------*/
    Page<Account> accountPage(int page);
    Account create(Account account) ;
    Account updateAccountAdmin(Account account) ;
	List<Account> findByKeyword(String keyword,Integer roleId);
	void updateLogical(Integer id);
	List<Account> findAllEnable();
	String CheckOrderStatus(Integer id);
	List<Account>findAllByRole(Integer role);
}


