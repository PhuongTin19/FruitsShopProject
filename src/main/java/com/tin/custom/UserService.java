package com.tin.custom;

import com.tin.entity.Account;
import com.tin.entity.Provider;
import com.tin.entity.Role;
import com.tin.repository.AccountRepo;
import com.tin.service.AccountService;
import com.tin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;

@Service
@Transactional
public class UserService {
    @Autowired
    private AccountService accountService;

    public void updateAuthenticationType(String username, String oauth2ClientName) {
        Provider authType = Provider.valueOf(oauth2ClientName.toUpperCase());
        accountService.updateProviderType(username, authType);
    }
}
