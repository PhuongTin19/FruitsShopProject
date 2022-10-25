package com.tin.service.impl;


import com.tin.entity.Account;
import com.tin.entity.Provider;
import com.tin.repository.AccountRepo;
import com.tin.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        return AccountRepo.save(account);
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
        	accDao.update(account.getFullname(), account.getEmail(), account.getPassword(), account.getPhone(),account.getAddress(), account.getImage(), account.getUsername());
        }
        else {
            account.setPassword(bcrypt.encode(account.getPassword()));
            accDao.update(account.getFullname(), account.getEmail(), account.getPassword(), account.getPhone(),account.getAddress(), account.getImage(), account.getUsername());
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
}