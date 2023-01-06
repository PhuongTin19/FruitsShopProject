package com.tin.custom;

import com.tin.entity.*;
import com.tin.repository.AccountRepo;

import com.tin.service.*;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class UserServices {
 
	@Autowired
	private AccountService accountService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderDetailsService orderDetailsService;
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserServices userServices;
	
	@Autowired
	private HttpServletRequest request;
	
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
	//Xử lý hủy đơn
	public void cancelOrder(Order order ,String siteURL) throws UnsupportedEncodingException, MessagingException{
		order.setAccount(order.getAccount());
		order.setDeliveryDate(order.getDeliveryDate());
        order.setAddress(order.getAddress());
		order.setNotes(order.getNotes());
		order.setOrderdate(order.getOrderdate());
		order.setOrderStatus("Đã hủy đơn");
		order.setPhone(order.getPhone());
		String random = RandomString.make(64);
		order.setVerificationCode(random);
		// Cập nhật độ uy tín
		Account account = accountService.findByUsername(request.getRemoteUser());
		account.setReliability(account.getReliability()+1);
		//Cập nhật số lượng vào product
		List<OrderDetail>details = orderDetailsService.findByDetailId(order.getOrder_id());
		for (int i = 0; i < details.size(); i++) {
			Product product = productService.findById(details.get(i).getProduct().getProduct_id());
			Integer newQuanity = product.getQuantity() + details.get(i).getTotalQuantity();
			productService.updateQuantity(newQuanity, details.get(i).getProduct().getProduct_id());
		}
		//
		try {
			accountService.update(account);
		} catch (Exception e) {
			e.printStackTrace();
		}
        orderService.updateOrder(order);

        if(account.getReliability() == 5) {
        	sendMailDisableAccount(order);
        }else {
        	sendMailCancelOrder(order, siteURL);
        }
        
    }

	//Mail hủy đơn ship cod
    private void sendMailCancelOrder(Order order, String siteURL) throws MessagingException, UnsupportedEncodingException{
        String toAddress = order.getAccount().getEmail();
        String fromAddress = "gfthotel12@gmail.com";
        String senderName = "Five House";
        String subject = "Xác nhận hủy đơn đã đặt";
        String content = "Thân chào <b>[[name]]</b>,<br>"
                + "Bạn đã xác nhận hủy đơn hàng bạn đã đặt vào lúc [[orderDate]]. <br>"
                + "<span style='font-weight: 600'>Tổng tiền: </span><span style='color: red; font-weight: 600'>[[TotalPrice]] đ</span><br>"
                + "Với lý do bạn đưa ra với chúng tôi là '[[notes]]' <br>"
                + "Cảm ơn bạn,<br>"
                + "Five House.<br>"
                + "<a href='http://localhost:8081'><img src='file:C:/Users/USUS/eclipse-workspace/FruitsShopProject2/src/main/resources/static/user/img/logo3.png' /></a>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
 
        content = content.replace("[[name]]", order.getAccount().getFullname());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String strDate = formatter.format(order.getOrderdate());
        content = content.replace("[[orderDate]]", strDate);
        
        content = content.replace("[[notes]]",order.getNotes());

        NumberFormat vn = NumberFormat.getInstance(new Locale("vi", "VI"));

        content = content.replace("[[TotalPrice]]", vn.format(order.getTotalPrice()));
        
        helper.setText(content, true);
 
        mailSender.send(message);

        System.out.println("Email đã được gửi");
    }

    //Mail hủy đơn tự động
    public void sendMailCancelOrderOnline(Order order) throws MessagingException, UnsupportedEncodingException{
        String toAddress = order.getAccount().getEmail();
      
        String fromAddress = "gfthotel12@gmail.com";
        String senderName = "Five House";
        String subject = "Đơn hàng đã bị hủy";
        String content = "Thân chào <b>[[name]]</b>,<br>"
                + "Rất tiếc phải thông báo cho bạn,đơn hàng bạn đặt vào lúc [[orderDate]]. <br>"
                + "<span style='font-weight: 600'>Tổng tiền: </span><span style='color: red; font-weight: 600'>[[TotalPrice]] đ</span> đã bị hủy.<br>"
                + "Với lý do bạn không tiến hành thanh toán đơn hàng theo quy định của shop<br>"
                + "Cảm ơn bạn,<br>"
                + "Five House.<br>"
                + "<a href='http://localhost:8081'><img src='file:C:/Users/USUS/eclipse-workspace/FruitsShopProject2/src/main/resources/static/user/img/logo3.png' /></a>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
 
        content = content.replace("[[name]]", order.getAccount().getFullname());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String strDate = formatter.format(order.getOrderdate());
        content = content.replace("[[orderDate]]", strDate);
        
        content = content.replace("[[notes]]",order.getNotes());

        NumberFormat vn = NumberFormat.getInstance(new Locale("vi", "VI"));

        content = content.replace("[[TotalPrice]]", vn.format(order.getTotalPrice()));

        helper.setText(content, true);
 
        mailSender.send(message);

        System.out.println("Email đã được gửi");
    }
    //Mail khôi phục đơn hàng
    public void sendMailRestoreOrder(Order order) throws MessagingException, UnsupportedEncodingException{
        String toAddress = order.getAccount().getEmail();
        String fromAddress = "gfthotel12@gmail.com";
        String senderName = "Five House";
        String subject = "Đơn hàng đã được khôi phục";
        String content = "Thân chào <b>[[name]]</b>,<br>"
                + "Đơn hàng của bạn đã được khôi phục theo yêu cầu <br>"
                + "Kiện hàng sẽ được chuyển đến bạn sớm.Nếu đã nhận được hàng và hài lòng về sản phẩm,bạn vui lòng tiến hành thanh toán<br>"
                + "Cảm ơn bạn,<br>"
                + "Five House.<br>"
                + "<a href='http://localhost:8081'><img src='file:C:/Users/USUS/eclipse-workspace/FruitsShopProject2/src/main/resources/static/user/img/logo3.png' /></a>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
 
        content = content.replace("[[name]]", order.getAccount().getFullname());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String strDate = formatter.format(order.getOrderdate());
        content = content.replace("[[orderDate]]", strDate);
        
        content = content.replace("[[notes]]",order.getNotes());

        NumberFormat vn = NumberFormat.getInstance(new Locale("vi", "VI"));

        content = content.replace("[[TotalPrice]]", vn.format(order.getTotalPrice()));

        helper.setText(content, true);
 
        mailSender.send(message);

        System.out.println("Email đã được gửi");
    }
    //Xử lý đặt hàng
  	public void purchaseOrder(Order order) throws UnsupportedEncodingException, MessagingException{
  		order.setAccount(order.getAccount());
  		order.setDeliveryDate(order.getDeliveryDate());
        order.setAddress(order.getAddress());
  		order.setNotes(order.getNotes());
  		order.setOrderdate(order.getOrderdate());
  		order.setOrderStatus("Chưa thanh toán");
  		order.setPhone(order.getPhone());
  		order.setPayment_method(order.getPayment_method());
  		String random = RandomString.make(64);
  		order.setVerificationCode(random);
  		Account a = accountService.findById(order.getAccount().getAccount_id());
		order.getAccount().setEmail(a.getEmail());
		order.getAccount().setFullname(a.getFullname());
        orderService.updateOrder(order);
        if(order.getPayment_method().getPayment_method_id() == 1) {
        	sendMailPurchaseShip(order);
        }else if(order.getPayment_method().getPayment_method_id() == 2) {
        	sendMailPurchase(order);
        }
        
      }
  	//online
    public void sendMailPurchase(Order order) throws MessagingException, UnsupportedEncodingException{
        String toAddress = order.getAccount().getEmail();
        String fromAddress = "gfthotel12@gmail.com";
        String senderName = "Five House";
        String subject = "Xác nhận đơn hàng đã đặt";
        String content = "Thân chào <b>[[name]]</b>,<br>"
                + "Bạn đã đặt đơn hàng vào lúc [[orderDate]]. <br>"
                + "<span style='font-weight: 600'>Tổng tiền: </span><span style='color: red; font-weight: 600'>[[TotalPrice]] đ</span><br>"
                + "Bạn vui lòng thanh toán trong vòng 1 giờ.Nếu không đơn hàng sẽ hủy tự động<br>"
      //          + "<h3><a href=\"[[URL]]\" target=\"_self\">XÁC NHẬN</a></h3>"
                + "Cảm ơn bạn,<br>"
                + "Five House.<br>"
                + "<a href='http://localhost:8081'><img src='file:C:/Users/USUS/eclipse-workspace/FruitsShopProject2/src/main/resources/static/user/img/logo3.png' /></a>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
 
        content = content.replace("[[name]]", order.getAccount().getFullname());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String strDate = formatter.format(order.getOrderdate());
        content = content.replace("[[orderDate]]", strDate);
        

        NumberFormat vn = NumberFormat.getInstance(new Locale("vi", "VI"));

        content = content.replace("[[TotalPrice]]", vn.format(order.getTotalPrice()));

       // String verifyURL = siteURL + "/verify?code=" + order.getVerificationCode();

        //content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
 
        mailSender.send(message);

        System.out.println("Email đã được gửi");
    }
    //Ship cod
    public void sendMailPurchaseShip(Order order) throws MessagingException, UnsupportedEncodingException{
        String toAddress = order.getAccount().getEmail();
        String fromAddress = "gfthotel12@gmail.com";
        String senderName = "Five House";
        String subject = "Xác nhận đơn hàng đã đặt";
        String content = "Thân chào <b>[[name]]</b>,<br>"
                + "Bạn đã đặt đơn hàng vào lúc [[orderDate]]. <br>"
                + "<span style='font-weight: 600'>Tổng tiền: </span><span style='color: red; font-weight: 600'>[[TotalPrice]] đ</span><br>"
                + "Đơn hàng sẽ được chuyển đến bạn sớm.Mọi thông tin liên lạc xin liên hệ đến số 0901301277<br>"
      //          + "<h3><a href=\"[[URL]]\" target=\"_self\">XÁC NHẬN</a></h3>"
                + "Cảm ơn bạn,<br>"
                + "Five House.<br>"
                + "<a href='http://localhost:8081'><img src='file:C:/Users/USUS/eclipse-workspace/FruitsShopProject2/src/main/resources/static/user/img/logo3.png' /></a>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
 
        content = content.replace("[[name]]", order.getAccount().getFullname());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String strDate = formatter.format(order.getOrderdate());
        content = content.replace("[[orderDate]]", strDate);
        

        NumberFormat vn = NumberFormat.getInstance(new Locale("vi", "VI"));

        content = content.replace("[[TotalPrice]]", vn.format(order.getTotalPrice()));

       // String verifyURL = siteURL + "/verify?code=" + order.getVerificationCode();

        //content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
 
        mailSender.send(message);

        System.out.println("Email đã được gửi");
    }
    
    //Xử lý đặt hàng thành công
    public void sendMailPurchaseSuccess(Order order) throws MessagingException, UnsupportedEncodingException{
        String toAddress = order.getAccount().getEmail();
        String fromAddress = "gfthotel12@gmail.com";
        String senderName = "Five House";
        String subject = "Xác nhận đơn hàng đã thanh toán";
        String content = "Thân chào <b>[[name]]</b>,<br>"
                + "Đơn hàng của bạn đã được thanh toán thành công<br>"
                + "<span style='font-weight: 600'>Tổng tiền: </span><span style='color: red; font-weight: 600'>[[TotalPrice]] đ</span><br>"
                + "Cảm ơn bạn đã tin tưởng sử dụng dịch vụ của shop<br>"
      //          + "<h3><a href=\"[[URL]]\" target=\"_self\">XÁC NHẬN</a></h3>"
                + "Chúc sức khỏe,<br>"
                + "Five House.<br>"
                + "<a href='http://localhost:8081'><img src='file:C:/Users/USUS/eclipse-workspace/FruitsShopProject2/src/main/resources/static/user/img/logo3.png' /></a>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
 
        content = content.replace("[[name]]", order.getAccount().getFullname());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String strDate = formatter.format(order.getOrderdate());
        content = content.replace("[[orderDate]]", strDate);
        

        NumberFormat vn = NumberFormat.getInstance(new Locale("vi", "VI"));

        content = content.replace("[[TotalPrice]]", vn.format(order.getTotalPrice()));

       // String verifyURL = siteURL + "/verify?code=" + order.getVerificationCode();

        //content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
 
        mailSender.send(message);

        System.out.println("Email đã được gửi");
    }
    
    
    //Mail vô hiệu hóa tài khoản
    public void sendMailDisableAccount(Order order) throws MessagingException, UnsupportedEncodingException{
    	String toAddress = order.getAccount().getEmail();
        String fromAddress = "gfthotel12@gmail.com";
        String senderName = "Five House";
        String subject = "Tài khoản của bạn đã bị vô hiệu hóa";
        String content = "Thân chào <b>[[name]]</b>,<br>"
        		+"Chúng tôi nhận thấy tài khoản của bạn có dấu hiệu bất thường<br>"
                + "Có vẻ tài khoản của bạn đã vi phạm các điều khoản của FiveHouse."
                + "Các hành vi vi phạm có thể bao gồm spam,hủy đơn nhiều lần,...<br>"
                + "Mọi thắc mắc xin liên hệ đến số hotline 0901301277 hoặc email:tin63711@gmail để được hỗ trợ.<br>"
                +"Cảm ơn<br>"
                + "Five House.<br>"
                + "<a href='http://localhost:8080'><img src='file:C:/Users/USUS/eclipse-workspace/FruitsShopProject2/src/main/resources/static/user/img/logo3.png' /></a>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
 
        content = content.replace("[[name]]", order.getAccount().getFullname());

        helper.setText(content, true);
 
        mailSender.send(message);

        System.out.println("Email đã được gửi");
    }

//    public boolean verifyCancelOrder(String verificationCode) {
//        Order order = orderService.findByVerificationCode(verificationCode);
//        if (order == null) {
//            System.out.println("update fail");
//            return false;
//        } else {
//        	order.setAccount(order.getAccount());
//        	order.setAddress(order.getAddress());
//    		order.setDeliveryDate(order.getDeliveryDate());
//    		order.setNotes(order.getNotes());
//    		order.setOrderdate(order.getOrderdate());
//    		order.setOrderStatus("Chờ xác nhận hủy");
//    		order.setPhone(order.getPhone());
//    		order.setShippingFee(order.getShippingFee());
//    		order.setVerificationCode(null);
//            System.out.println("update success");
//
//            return true;
//        }
//    }

}