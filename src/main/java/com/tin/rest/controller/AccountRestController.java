package com.tin.rest.controller;

import java.util.List;

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
import com.tin.service.AccountService;


@CrossOrigin("*")
@RestController
@RequestMapping("/rest/accounts")
public class AccountRestController {
	
	@Autowired
	AccountService accountService;
	
	@GetMapping()
	public List<Account> getAll() {
		return accountService.findAll();
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
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		account.setPassword(bcrypt.encode(account.getPassword()));
		return accountService.create(account);
	}
	@PutMapping("{id}")
	public Account update(@PathVariable("id")Integer id,@RequestBody Account account) {
		return accountService.updateAccount(account);
	}
	@PutMapping("/deleteLogical/{id}")
	public void delete(@PathVariable("id")Integer id,@RequestBody Account account) {
		accountService.deleteLogical(id);
	}
	@PutMapping("/updateLogical/{id}")
	public void updateLogical(@PathVariable("id")Integer id,@RequestBody Account account) {
		accountService.updateLogical(id);
	}
	@GetMapping("/keyword/{keyword}")
	public List<Account> getMany(@PathVariable("keyword") String keyword) {
		return accountService.findByKeyword(keyword);
	}
}
