package com.tin.controller;

import com.tin.custom.UserServices;
import com.tin.custom.Utility;
import com.tin.entity.Account;
import com.tin.service.AccountService;
//import com.nicetravel.service.BookingService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.awt.print.Book;
import java.io.UnsupportedEncodingException;

@Controller
public class SecurityController {

    private UserServices userServices;
    
    private AccountService accountService;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    //Gửi mail
    @Autowired
    private JavaMailSender mailSender;

    
    @Autowired
    public SecurityController(UserServices userServices, AccountService accountService) {
        this.userServices = userServices;
        this.accountService = accountService;
    }

    //Chuyển đến form forgot
    @GetMapping("/forgot_password")
    public String showForgotPasswordForm(Model model) {
    	model.addAttribute("kt","false");
        return "/user/forgotpw";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
    	//Lấy tham số email của người dùng nhập vào
        String email = request.getParameter("email");
        //random token mới
        String token = RandomString.make(64);

        try {
        	//cập nhật token => update account
            userServices.updateResetPasswordToken(token, email);
            //Nếu đúng token => chuyển sang form thay đổi mật khẩu
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "Email đã được gửi. Vui lòng kiểm tra hộp thư.");

        } catch (UsernameNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Đã xảy ra lỗi khi gửi email");
        }

        return "/user/forgotpw";
    }
    //Gửi mail
    public void sendEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("tinnpps18888@fpt.edu.vn", "FiveHouse Support");
        helper.setTo(recipientEmail);

        String subject = "Đặt lại mật khẩu của bạn";

        String content = "<p>Xin chào,</p>"
                + "<p>Bạn đã yêu cầu đặt lại mật khẩu của mình..</p>"
                + "<p>Nhấp vào liên kết bên dưới để thay đổi mật khẩu của bạn:</p>"
                + "<h3><a href=\"" + link + "\">Thay đổi mật khẩu</a></h3>"
                + "<p><strong>Lưu ý: </strong>Bỏ qua email này nếu bạn nhớ mật khẩu của mình hoặc bạn chưa thực hiện yêu cầu.</p>"
                + "Cảm ơn bạn,<br>"
                + "Five House.";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
    	Account account = accountService.findByVerificationCode(token);
        model.addAttribute("token", token);
        if (account == null) {
            model.addAttribute("message", "Mã Token không hợp lệ, vui lòng thử lại");
            return "/user/forgotpw";
        }
        return "/user/reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) throws Exception {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        Account account = accountService.findByVerificationCode(token);
        model.addAttribute("title", "Đặt lại mật khẩu của bạn");
        if (account == null) {
            model.addAttribute("message", "Mã Token không hợp lệ, vui lòng thử lại.");
            return "/user/forgotpw";
        }else{
            userServices.updatePassword(account, password);
            model.addAttribute("message", "Bạn đã thay đổi mật khẩu thành công.");
            model.addAttribute("kt","true");
        }
        return "/user/forgotpw"; 
    }

}
