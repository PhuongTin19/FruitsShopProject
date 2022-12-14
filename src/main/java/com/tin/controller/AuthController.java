package com.tin.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;

import org.hibernate.validator.constraints.Mod11Check.ProcessingDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import lombok.experimental.var;
 
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
	
	private final PasswordEncoder passwordEncoder;


	@Autowired
	public AuthController(AccountService accountService, UserServices userServices, PasswordEncoder passwordEncoder) {
		this.accountService = accountService;
		this.userServices = userServices;
		this.passwordEncoder = passwordEncoder;
	}

	private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

	@GetMapping("/security/login")
	public String loginForm(Model model,HttpServletRequest request) {
		model.addAttribute("userRequest", new Account());		
		return "/user/login";
	} 
	
	@RequestMapping("/security/login/success")
	public String loginSuccess(Model model, HttpSession session, HttpServletRequest request) {
		try {
			Account account = accountService.findByUsername(request.getRemoteUser());
			session.setAttribute("currentUser", account);
			model.addAttribute("message", "????ng nh???p th??nh c??ng");
		} catch (Exception e) {

		}
		return "redirect:/index";
	}
	@RequestMapping("/security/login/error")
	public String loginError(Model model,HttpServletRequest request,
			@Valid @ModelAttribute("userRequest") Account a,
			Errors error) {
		model.addAttribute("message", "????ng nh???p kh??ng th??nh c??ng");
		return "/user/login";
	}

	@RequestMapping("/security/logoff/success")
	public String logoffSuccess(Model model, HttpSession session) {
	//	session.removeAttribute("currentUser");
		model.addAttribute("message", "????ng xu???t th??nh c??ng");
		return "redirect:/security/login";
	}


	// ?????i m???t kh???u
	@GetMapping("/user/change-password")
	public String doGetChange(Model model, HttpServletRequest request) {
		return "/user/changepassword";
	}

	@PostMapping("/user/change-password")
	public String doPostChange(Model model, HttpServletRequest request, Authentication authentication) {
			String oldPassword = request.getParameter("oldPassword");
			String newPassword = request.getParameter("newPassword");
			String password = request.getParameter("password");
			Account account = accountService.findByUsername(request.getRemoteUser()); // remote
			String username = null;

			if (account == null){
				CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
				Account accountOauth = accountService.findByEmail(oauth2User.getEmail());
				username = accountOauth.getUsername();
			}
			else {
				username = account.getUsername();
			}
			Account acc = accountService.findByUsername(username);
			System.out.println(acc.getPassword());
			if (!passwordEncoder.matches(oldPassword, acc.getPassword())) {
				model.addAttribute("errorOld", "M???t kh???u c?? kh??ng ????ng");
			} else if (oldPassword.equals(newPassword)) {
				model.addAttribute("error", "M???t kh???u m???i kh??ng ???????c tr??ng m???t kh???u c??");
			} else if (!newPassword.equals(password)) {
				model.addAttribute("errorConfirm", "M???t kh???u kh??ng kh???p");
			} else {
				String encodedPassword = bcrypt.encode(password);// M?? h??a m???t kh???u
				accountService.updatePassword(encodedPassword, username);
				model.addAttribute("message", "?????i m???t kh???u th??nh c??ng");
			}
		
		return "/user/changepassword";
	}

	// ????ng k??
	@GetMapping("/user/register")
	public String doGetRegister(Model model) {
		model.addAttribute("account", new Account());
		return "/user/register";
	}

	@PostMapping("/user/register")
	public String doPostRegister(Model model, HttpServletRequest request, RedirectAttributes r,
				@Valid @ModelAttribute("account") Account account,
				Errors error)
			throws UnsupportedEncodingException, MessagingException {
		Account accountsByUsername = accountService.findByUsername(account.getUsername());
		Account accountsByEmail = accountService.findByEmail(account.getEmail());
		Account accountsByPhone = accountService.findByPhone(account.getPhone());
		String confirmPassword = request.getParameter("confirm");
		if(accountsByUsername != null || accountsByEmail != null || accountsByPhone != null ||
				!confirmPassword.matches(account.getPassword())) {
			if (accountsByUsername != null) {
				model.addAttribute("usernameValid", "T??i kho???n ???? t???n t???i");
			}if(accountsByEmail != null) {
				model.addAttribute("emailValid", "Email ???? t???n t???i");
			}if(accountsByPhone != null) {
				model.addAttribute("phoneValid", "S??? ??i???n tho???i n??y ???? t???n t???i");
			}if (!confirmPassword.matches(account.getPassword())) {
				model.addAttribute("error", "M???t Kh???u kh??ng tr??ng kh???p");
			}
			return null;
		}else {
			try {
				String encodedPassword = bcrypt.encode(account.getPassword());
				account.setPassword(encodedPassword);
				account.setRole(roleService.findByRoleName("User"));
				account.setImage("user.png");
				account.setIs_enable(false);
				account.setReliability(0);
				account.setProvider(Provider.DATABASE);
				accountService.createAccount(account);
				model.addAttribute("message", "????ng k?? th??nh c??ng");
				return "/user/register";
			} catch (Exception e) {
				model.addAttribute("error", "???? x???y ra l???i khi ????ng k?? t??i kho???n");
				e.printStackTrace();
				return "/user/register";
			}
		}
	}

	// Th??ng tin t??i kho???n
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

	// c???p nh???t t??i kho???n
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
		List<Account>account2 = accountService.findAll();
		for (int i = 0; i < account2.size(); i++) {
			if(userRequest.getEmail().equals(account2.get(i).getEmail()) && userRequest.getAccount_id().equals(account2.get(i).getAccount_id())==false) {
				model.addAttribute("emailValid", "Email ???? t???n t???i");
				userRequest.setImage(account.getImage());
				return "/user/EditInformationCustomer";
			}if(userRequest.getPhone().equals(account2.get(i).getPhone()) && userRequest.getAccount_id().equals(account2.get(i).getAccount_id())==false) {
				model.addAttribute("phoneValid", "S??? ??i???n tho???i ???? t???n t???i");
				userRequest.setImage(account.getImage());
				return "/user/EditInformationCustomer";
			}if(userRequest.getUsername().equals(account2.get(i).getUsername()) && userRequest.getAccount_id().equals(account2.get(i).getAccount_id())==false) {
				model.addAttribute("usernameValid", "Username ???? t???n t???i");
				userRequest.setImage(account.getImage());
				return "/user/EditInformationCustomer";
			}
		}
		if(userRequest.getUsername().equals("") || userRequest.getEmail().equals("") || userRequest.getFullname().equals("")
				|| userRequest.getPhone().equals("") || userRequest.getAddress().equals("") || userRequest.getPhone().length() != 10) {
			if(userRequest.getEmail().equals("") ) {
				model.addAttribute("emailValid", "Email kh??ng ???????c ????? tr???ng");
			}if(userRequest.getFullname().equals("")) {
				model.addAttribute("fullnameValid", "H??? t??n kh??ng ???????c ????? tr???ng");
			}if(userRequest.getPhone().equals("")) {
				model.addAttribute("phoneValid", "S??? ??i???n tho???i kh??ng ???????c ????? tr???ng");
			}if(userRequest.getAddress().equals("")) {
				model.addAttribute("addressValid", "?????a ch??? kh??ng ???????c ????? tr???ng");
			}if(userRequest.getPhone().length() != 10) {
				model.addAttribute("phoneValid", "S??? ??i???n tho???i ph???i l?? 10 s???");
			}
			userRequest.setImage(account.getImage());
			return "/user/EditInformationCustomer";
		}else {
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
				//FileUploadUtil.saveFile(uploadDir, fileName, image);
				return "redirect:/user/infor";
			} catch (Exception e) {
				e.printStackTrace();
				return "/user/EditInformationCustomer";
			}
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
