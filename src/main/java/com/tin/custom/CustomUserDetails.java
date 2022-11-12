package com.tin.custom;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.tin.entity.Account;
import com.tin.service.AccountService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CustomUserDetails implements UserDetails {
	
    private final Account account;

    private final AccountService accountService;

    @Autowired
    public CustomUserDetails(Account account, AccountService accountService) {
        this.account = account;
        this.accountService = accountService;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(account.getRole().getName()));
    }



    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return account.getIs_enable();
    }

    public String getName() {
        return account.getUsername();
    }

    public Account getAccount(){
        return account;
    }
 

}
