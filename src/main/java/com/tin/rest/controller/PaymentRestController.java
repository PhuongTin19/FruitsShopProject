package com.tin.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tin.entity.Account;
import com.tin.entity.Favorite;
import com.tin.entity.Payment_Methods;
import com.tin.service.FavoriteService;
import com.tin.service.PaymentService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/payment")
public class PaymentRestController {
	@Autowired
	PaymentService paymentService;
	
	@PutMapping("{id}")
	public void DeleteLogical(@PathVariable("id")Integer id,@RequestBody Payment_Methods payment_method) {
		paymentService.deleteLogical(id);
	}

}
