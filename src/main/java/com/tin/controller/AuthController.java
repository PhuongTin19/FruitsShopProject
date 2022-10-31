package com.tin.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.hibernate.validator.constraints.Mod11Check.ProcessingDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tin.custom.CustomOAuth2User;
import com.tin.custom.FileUploadUtil;
import com.tin.custom.UserServices;
import com.tin.entity.Account;
import com.tin.entity.Provider;
import com.tin.repository.AccountRepo;
import com.tin.service.AccountService;
import com.tin.service.FavoriteService;
import com.tin.service.RoleService;

import javassist.expr.NewArray;

@Controller
public class AuthController {


	@Autowired
	UserServices userServices;

	@Autowired
	AccountService accountService;

	@Autowired
	RoleService roleService;

	@Autowired
	ServletContext app;

	private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

	@GetMapping("/security/login")
	public String loginForm(Model model) {
		model.addAttribute("userRequest", new Account());
		return "/user/login";
	} 

	@RequestMapping("/security/login/success")
	public String loginSuccess(Model model, HttpSession session, HttpServletRequest request) {
		try {
			Account account = accountService.findByUsername(request.getRemoteUser());
			session.setAttribute("currentUser", account);
			model.addAttribute("message", "Đăng nhập thành công");
		} catch (Exception e) {

		}
		return "redirect:/index";
	}
  
	@RequestMapping("/security/login/error")
	public String loginError(Model model,
			@Valid @ModelAttribute("userRequest") Account a,
			Errors error) {
			model.addAttribute("message", "Đăng nhập không thành công");
		return "/user/login";
	}

	@RequestMapping("/security/logoff/success")
	public String logoffSuccess(Model model, HttpSession session) {
		session.removeAttribute("currentUser");
		model.addAttribute("message", "Đăng xuất thành công");
		return "redirect:/security/login";
	}
//	@GetMapping("/security/login")
//	public String loginForm(Model model) {
//		model.addAttribute("userRequest", new Account());
//		return "user/login";
//	}

//	@PostMapping("/security/login")
//	public String doPostLogin(@ModelAttribute("userRequest") Account userRequest,
//			HttpSession session,Model model,HttpServletRequest request) {
//			Account accountResponse = accountService.doLogin(userRequest.getUsername(), userRequest.getPassword());
//			if(accountResponse != null) {
//				session.setAttribute("currentUser", accountResponse);
//				model.addAttribute("message","Đăng nhập thành công");
//				return "redirect:/index";
//			}else {
//				model.addAttribute("message","Đăng nhập thất bại");
//				return "/user/login";
//			}
//	}
//	// dang xuat
//	@GetMapping("/security/logoff")
//	public String logOffSuccess(Model model,HttpSession session) {
//		model.addAttribute("message","Bạn đã đăng xuất!");
//		session.removeAttribute("currentUser");
//		return "redirect:/index"; 
//	}

	// Đổi mật khẩu
	@GetMapping("/user/change-password")
	public String doGetChange(Model model, HttpServletRequest request) {
		return "/user/changepassword";
	}

	@PostMapping("/user/change-password")
	public String doPostChange(Model model, HttpServletRequest request) {
			String username = request.getParameter("username");
			String getPassword = accountService.findByPassword(username);
			String oldPassword = request.getParameter("oldPassword");
			String newPassword = request.getParameter("newPassword");
			String password = request.getParameter("password");
			if(oldPassword.isEmpty() || newPassword.isEmpty() || password.isEmpty()) {
				model.addAttribute("error", "Không để trống ô nhập");
			}else if (getPassword == null) {
				model.addAttribute("error", "Mật khẩu cũ không đúng");
			} else if (oldPassword.equals(newPassword)) {
				model.addAttribute("error", "Mật khẩu mới không được trùng mật khẩu cũ");
			} else if (!newPassword.equals(password)) {
				model.addAttribute("error", "Mật khẩu không khớp");
			} else {
				String encodedPassword = bcrypt.encode(password);// Mã hóa mật khẩu
				accountService.updatePassword(encodedPassword, username);
				model.addAttribute("message", "Đổi mật khẩu thành công");
			}
		
		return "/user/changepassword";
	}

	// Đăng ký
	@GetMapping("/user/register")
	public String doGetRegister(Model model) {
		model.addAttribute("account", new Account());
		return "/user/register";
	}

	@PostMapping("/user/register")
	public String doPostRegister(Model model, HttpServletRequest request,
				@Valid @ModelAttribute("account") Account account,
				Errors error)
			throws UnsupportedEncodingException, MessagingException {
		Account accountsByUsername = accountService.findByUsername(account.getUsername());
		String confirmPassword = request.getParameter("confirm");
		try {
			if (accountsByUsername != null) {
				model.addAttribute("error", "Tài khoản đã tồn tại");
				return null;
			} else if (!confirmPassword.matches(account.getPassword())) {
				model.addAttribute("error", "Mật Khẩu không trùng khớp");
			} else {
				String encodedPassword = bcrypt.encode(account.getPassword());
				account.setPassword(encodedPassword);
				account.setRole(roleService.findByRoleName("User"));
				account.setImage("user.png");
				account.setIs_enable(true);
				account.setProvider(Provider.DATABASE);
				accountService.createAccount(account);
				model.addAttribute("message", "Đăng ký thành công");
			}
		} catch (Exception e) {
			model.addAttribute("error", "Đã xảy ra lỗi khi đăng ký tài khoản");
		}
		return "/user/register";
	}

	// Thông tin tài khoản
	@GetMapping("/user/infor")
	public String doGetEdit(Model model, HttpServletRequest request, Authentication authentication) {
		Account account = accountService.findByUsername(request.getRemoteUser()); // remote
		String username = null;
		if (account == null) {
			CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
			Account accountOauth = accountService.findByEmail(oauth2User.getEmail());
			username = accountOauth.getUsername();
		} else {
			username = account.getUsername();
		}

		model.addAttribute("account", accountService.findByUsername(username));
		return "/user/informationCustomer";
	}

	// cập nhật tài khoản
	@GetMapping("/user/detailInfor")
	public String doGetDetailEdit(HttpServletRequest request, Model model, Authentication authentication) {
		String username = userServices.getUserName(request, authentication);
		Account userRequest = accountService.findByUsername(username);
		model.addAttribute("userRequest", userRequest);
		return "/user/EditInformationCustomer";
	}
 
	@PostMapping("/user/detailInfor")
	public String doPostDetailEdit(Model model,@Valid @ModelAttribute("userRequest") Account userRequest,Errors error,
			@RequestParam("fileImage") MultipartFile image, HttpServletRequest request, Authentication authentication)
			throws IOException {

		String username = userServices.getUserName(request, authentication);

		Account account = accountService.findByUsername(username);
		String password = account.getPassword();
		try {
			String fileName = StringUtils.cleanPath(image.getOriginalFilename());
			if (fileName.equals("") || fileName.length() == 0 || fileName == null) {
				System.out.println("accountImg: " + account.getImage());
				account.setImage(account.getImage());
				account.setPassword(password);
			} else {
				account.setImage(fileName);
			}
			accountService.update(account);
			accountService.update(userRequest);

			String uploadDir = "photos\\" ;
			
			Path uploadPath = Paths.get(uploadDir);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			try (InputStream inputStream = image.getInputStream()) {
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				
			}
//			FileUploadUtil.saveFile(uploadDir, fileName, image);
			return "redirect:/user/infor";
		} catch (Exception e) {
			e.printStackTrace();
			return "/user/EditInformationCustomer";
		}
		
	}

	@GetMapping("/")
	public String success(OAuth2AuthenticationToken oauth) {
//		String username = oauth.getPrincipal().getAttribute("name");
//		System.out.println(username);
//        UserDetails user = User.withUsername(username).password("").roles("USER").build();
//
//        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//
//        SecurityContextHolder.getContext().setAuthentication(auth);
		System.out.println("hello success1");
		return "redirect:/index"; 
	}
//	
//	@GetMapping("/oauth2/login/success")
//    public String success2(OAuth2AuthenticationToken oauth){
//        String fullname = oauth.getPrincipal().getAttribute("name");
//
//        UserDetails user = User.withUsername(fullname).password("").roles("USER").build();
//
//        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//
//        SecurityContextHolder.getContext().setAuthentication(auth);
//        System.out.println("hello success2");
//        return "redirect:/security/login/success";
//    }
}
