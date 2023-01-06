package com.tin.service.impl;


import com.tin.entity.Account;
import com.tin.entity.Provider;
import com.tin.repository.AccountRepo;
import com.tin.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepo AccountRepo;
    private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    @Autowired
	AccountRepo accDao;
    
    @Autowired
    public AccountServiceImpl(AccountRepo AccountRepo) {
        this.AccountRepo = AccountRepo;
    }
    
    //Cập nhật Tài Khoản
    @Override
    public Account updateAccount(Account account) {
    		Account a = accDao.findById(account.getAccount_id()).get();
        	boolean checkPassword = account.getPassword().equals(a.getPassword());
        	System.out.println("check:"+checkPassword);
        	account.setProvider(a.getProvider());
        	account.setReliability(a.getReliability());
        	if(checkPassword) {
        		account.setPassword(a.getPassword());
        		return AccountRepo.save(account);
        	}else {
        		 account.setPassword(bcrypt.encode(account.getPassword()));
        		 return AccountRepo.save(account);
        	}  
    }
     
    //Xác định mã code của account
    @Override
    public Account findByVerificationCode(String code) {
        return AccountRepo.findByVerificationCode(code);
    }

    //Tìm Email
    @Override
    public Account findByEmail(String email) {
        return AccountRepo.findByEmail(email);
    }
    //
    @Override
	public Account findByUsername(String username) {
		return accDao.findByUsername(username);
	}

	@Override
	public Account doLogin(String username, String password) {
		Account account = accDao.findByUsername(username);
		if (account != null) {//tài khoản có tồn tại
			boolean checkPassword = bcrypt.matches(password, account.getPassword());
			boolean checkPassword2 = password.matches(account.getPassword());
			return checkPassword|checkPassword2 ? account : null;//
		} else {
			return null;
		}
	}

	@Override
	public String findByPassword(String username) {
		return accDao.findByPassword(username);
	}

	@Override
	@Transactional
	public void updatePassword(String password, String username) {
		accDao.updatePassword(password, username);
	}

	@Override
	public Account createAccount(Account account) {
		return accDao.save(account);
	}

	@Override
	public Account EditAccount(String username) {
		return accDao.saveAndFlush(null);
	}

	@Override
	public void updateProviderType(String username, Provider provider) {
		accDao.updateProviderType(username, provider);
		
	}

	@Override
	@Transactional
	public void update(Account account) throws Exception {
		if (ObjectUtils.isEmpty(account)) {
            throw new Exception("Tài khoản không thể trống");
        }
        //check update non password or update full
        if (ObjectUtils.isEmpty(account.getPassword())) {
        	accDao.updateNonPass(account.getFullname(), account.getEmail(), account.getPhone(),account.getAddress(), account.getUsername());
        }
        else if(account.getPassword().length() != 0 || account.getPassword() != null){
        	accDao.update(account.getFullname(), account.getEmail(), account.getPassword(), account.getPhone(),account.getAddress(), account.getImage(),account.getUsername() ,account.getUsername());
        }
        else {
            account.setPassword(bcrypt.encode(account.getPassword()));
            accDao.update(account.getFullname(), account.getEmail(), account.getPassword(), account.getPhone(),account.getAddress(), account.getImage(), account.getUsername(), account.getUsername());
        }
	}

	@Override
	public List<Account> findAll() {
		return accDao.findAll();
	}

	@Override
	public void save(Account account) {
		accDao.save(account);
	}

	@Override
	public Account findById(Integer id) {
		return accDao.findByAccountId(id);
	}

	@Override
	public List<Account> findAllByRoleAdmin() {
		return accDao.findAllByRoleAdmin();
	}

	@Override
	public Integer getCountCustomerInDay() {
		return accDao.getCountCustomerInDay();
	}
	
	@Override
	public Page<Account> accountPage(int page) {
		Pageable pageable = PageRequest.of(page, 5);
		return accDao.findAll(pageable);
	}

	@Override
	public Account create(Account account) {
		try {
			account.setReliability(0);
			account.setProvider(Provider.DATABASE);
			return accDao.save(account);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	@Transactional
	public void deleteLogical(Integer id) {
		accDao.deleteLogical(id);
	}

	@Override
	public Account updateAccountAdmin(Account account) {
		return accDao.save(account);
	}
	
	@Override
	public List<Account> findByKeyword(String keyword,Integer roleId) {
		return accDao.findByKeyword(keyword,roleId);
	}

	@Override
	@Transactional
	public void updateReliability(Integer reliability, String username) {
		accDao.updateReliability(reliability, username);
	}

	@Override
	@Transactional
	public void updateLogical(Integer id) {
		accDao.updateLogical(id);
	}

	@Override
	public List<Account> findAllEnable() {
		return accDao.findAllEnable();
	}

	@Override
	public String CheckOrderStatus(Integer id) {
		return accDao.CheckOrderStatus(id);
	}

	@Override
	public Account findByPhone(String phone) {
		return accDao.findByPhone(phone);
	}

	@Override
	public List<Account> findAllByRole(Integer role) {
		return accDao.findAllByRole(role);
	}
}
