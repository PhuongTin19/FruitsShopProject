package com.tin.custom;

import com.tin.entity.*;
import com.tin.repository.AccountRepo;
//import com.nicetravel.repository.RoleRepository;
//import com.nicetravel.security.auth.CustomOAuth2User;
import com.tin.service.*;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class UserServices {

	@Autowired
	private AccountService accountService;
//
	public BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	//cập nhật token => update account
	public void updateResetPasswordToken(String token, String email) throws UsernameNotFoundException {
		//Tìm kiếm email
		Account account = accountService.findByEmail(email);
		//Nếu email khác null(có tồn tại trong DB)
		if (account != null) {
			//Thêm token mới
			account.setVerificationCode(token);
			//Cập nhật lại thông tin tài khoản
			accountService.updateAccount(account);
		} else {
			throw new UsernameNotFoundException("Không tìm thấy tài khoản nào có email: " + email);
		}
	}

	public void updatePassword(Account account, String newPassword) throws Exception {
		String encodedPassword = passwordEncoder.encode(newPassword);
		account.setPassword(encodedPassword);
		account.setVerificationCode(null);
		accountService.updateAccount(account);
	}
	
	public String getUserName(HttpServletRequest request, Authentication authentication) {
        Account account = accountService.findByUsername(request.getRemoteUser());

        String username = null;

        if (account == null) {
            CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
            Account accountOauth = accountService.findByEmail(oauth2User.getEmail());
            username = accountOauth.getUsername();
        } else {
            username = account.getUsername();
        }
        return username;
    }

}