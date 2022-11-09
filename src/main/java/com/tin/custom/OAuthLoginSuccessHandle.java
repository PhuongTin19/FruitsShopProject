package com.tin.custom;


import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


import com.tin.entity.Account;
import com.tin.entity.Provider;
import com.tin.service.AccountService;
import com.tin.service.RoleService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class OAuthLoginSuccessHandle extends SavedRequestAwareAuthenticationSuccessHandler {

	 private final UserService userService;

	    private final AccountService accountService;

	    private final RoleService roleService;

	    private final JavaMailSender mailSender;

//	    private BCryptPasswordEncoder passwordEncoder;

	    @Autowired
	    HttpSession session;
	    
	    public BCryptPasswordEncoder getPasswordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	    @Autowired
	    public OAuthLoginSuccessHandle(UserService userService, AccountService accountService, RoleService roleService, JavaMailSender mailSender) {
	        this.userService = userService;
	        this.accountService = accountService;
	        this.roleService = roleService;
	        this.mailSender = mailSender;
	    }


	    @Override
	    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
	                                        Authentication authentication) throws ServletException, IOException {
	        CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();

//	        String random = Double.toString(Math.round(Math.random() * 9 + 1));

	        String randomPass = RandomString.make(8);

	        String encodePass = getPasswordEncoder().encode(randomPass);


	        String oauth2ClientName = oauth2User.getOauth2ClientName();
	        String email = oauth2User.getEmail();
	        String username = oauth2User.getName();
	        String picture = oauth2User.getPicture();
	        String fullName = oauth2User.getFullName();
	        String address = "Chưa cập nhật";
	        String phone = "0000000000";
	        

	        Account accountByEmail = accountService.findByEmail(email);
	        session.setAttribute("currentUser", accountByEmail );
	        if (accountByEmail != null) {
	            System.out.println("Tài khoản người dùng đã tồn tại trong cơ sở dữ liệu.");
	        }
	        else {
	            System.out.println("Người dùng mới đã được thêm vào.");
	            Account newAccount = new Account();

	            if(oauth2ClientName.equals("Facebook")){
	                newAccount.setImage("user.png");
	            }
	            else {
	                newAccount.setImage(picture);
	            }
	            newAccount.setFullname(fullName);
	            newAccount.setUsername(email);
	            newAccount.setPassword(encodePass);
	            newAccount.setEmail(email);
	            newAccount.setAddress("");
	            newAccount.setPhone("");
	            newAccount.setRole(roleService.findByRoleName("USER"));
	            newAccount.setVerificationCode("");
	            newAccount.setProvider(Provider.valueOf(oauth2ClientName.toUpperCase()));
	            newAccount.setIs_enable(true);
	            newAccount.setAddress(address);
	            newAccount.setPhone(phone);
	            newAccount.setReliability(0);
	            accountService.createAccount(newAccount);
	            session.setAttribute("currentUser", newAccount);
	            System.out.println(session.getAttribute("currentUser"));
	            userService.updateAuthenticationType(username, oauth2ClientName);
	        }

	        super.onAuthenticationSuccess(request, response, authentication);
	    }


}
