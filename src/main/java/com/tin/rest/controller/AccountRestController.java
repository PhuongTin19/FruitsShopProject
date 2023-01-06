package com.tin.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tin.entity.Account;
import com.tin.entity.Behavior;
import com.tin.service.AccountService;
import com.tin.service.BehaviorService;


@CrossOrigin("*")
@RestController
@RequestMapping("/rest/accounts")
public class AccountRestController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	BehaviorService behaviorService;
	
	@GetMapping()
	public List<Account> getAll() {
		return accountService.findAllByRole(2);
	}
	
	@GetMapping("/staff")
	public List<Account> getAllStaff() {
		return accountService.findAllByRole(1);
	}
	   
	@GetMapping("/admin")  
	public List<Account> getAllAdmin() {
		return accountService.findAllByRole(3);
	}
	
	@GetMapping("/enable")
	public List<Account> getAllEnable() {
		return accountService.findAllEnable();
	}
	
	@GetMapping("{id}")
	public Account getOne(@PathVariable("id")Integer id) {
		return accountService.findById(id);
	}
	@PostMapping
	public Account create(@RequestBody Account account) {
		try {
			BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
			account.setPassword(bcrypt.encode(account.getPassword()));
			accountService.create(account);
			Account accountRecord = accountService.findByUsername(request.getRemoteUser());
			Behavior behavior =  new Behavior();
			behavior.setAccount(accountRecord);
			behavior.setDescription(accountRecord.getUsername() + " Đã tạo tài khoảng " + account.getUsername());
			behaviorService.save(behavior);
			return account;
		} catch (Exception e) {
			return null;
		}
	}
	@PutMapping("{id}")
	public Account update(@PathVariable("id")Integer id,@RequestBody Account account) {
		Account accountRecord = accountService.findByUsername(request.getRemoteUser());
		accountService.updateAccount(account);
		Behavior behavior = new Behavior();
		behavior.setAccount(accountRecord);
		behavior.setDescription(accountRecord.getUsername() + " Đã cập nhật user " + account.getUsername());
		behaviorService.save(behavior);
		return account;
	}
	@PutMapping("/deleteLogical/{id}")
	public void delete(@PathVariable("id")Integer id,@RequestBody Account account) {
			String orderStatus = accountService.CheckOrderStatus(id);
//			if(orderStatus.equals("Chưa thanh toán")) {
//				System.out.println("Người dùng đang sử dụng dịch vụ");
//				throw new RuntimeException();
//			}else {
				accountService.deleteLogical(id);
				Account accountRecord = accountService.findByUsername(request.getRemoteUser());
				Behavior behavior = new Behavior();
				behavior.setAccount(accountRecord);
				behavior.setDescription(accountRecord.getUsername() + " Đã tắt trạng thái hoạt động user " 
						+ account.getUsername());
				behaviorService.save(behavior);
//			}
	}
	@PutMapping("/updateLogical/{id}")
	public void updateLogical(@PathVariable("id")Integer id,@RequestBody Account account) {
		accountService.updateLogical(id);
	}
	@GetMapping("/keyword/{keyword}/{roleId}")
	public List<Account> getMany(@PathVariable("keyword") String keyword,@PathVariable("roleId") Integer roleId) {
		return accountService.findByKeyword(keyword,roleId);
	}
}
