package com.tin.admin.controller;

import com.tin.entity.Account;
import com.tin.entity.Provider;
import com.tin.entity.Role;
import com.tin.service.AccountService;
import com.tin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private RoleService roleService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @GetMapping(value = "/admin-accounts")
    public String adminAccounts(Model model) {
        List<Account> accounts = accountService.findAll();
        model.addAttribute("title", "Admin Accounts");
        model.addAttribute("size", accounts.size());
        model.addAttribute("accounts", accounts);
        model.addAttribute("message", "Accounts list is empty!");
        return "admin/Account/accounts";
    }

    @GetMapping(value = "/add-account")
    public String addAccountForm(Model model) {
        List<Role> roles = roleService.findAll();
        model.addAttribute("title", "Add Account");
        model.addAttribute("roles", roles);
        return "admin/Account/add-account";
    }

    @PostMapping("/add-new-account")
    public String addAccount(@ModelAttribute("account") Account account, @RequestParam("imageFile") MultipartFile image,
                             Model model, RedirectAttributes attributes) {
        Account account1 = new Account();
        account1.setUsername(account.getUsername());
        account1.setPassword(passwordEncoder.encode(account.getPassword()));
        account1.setFullname(account.getFullname());
        account1.setAddress(account.getAddress());
        account1.setPhone(account.getPhone());
        account1.setEmail(account.getEmail());
        account1.setRole(account.getRole());
        account1.setIs_enable(account.getIs_enable());

        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
        if (fileName.equals("") || fileName.length() == 0 || fileName == null) {
            account1.setImage("user.png");
        } else {
            account1.setImage(fileName);
        }

        account1.setProvider(Provider.DATABASE);
        account1.setVerificationCode(null);

        List<Account> accounts = accountService.findAll();
        for (Account account2 : accounts) {
            if (account2.getUsername().equalsIgnoreCase(account1.getUsername()) &&
                    (account2.getAccount_id()) != account1.getAccount_id()) {
                List<Role> roles = roleService.findAll();
                model.addAttribute("account", account1);
                model.addAttribute("error", "Your username had duplicate with another " +
                        "username!");
                model.addAttribute("roles", roles);
                return "admin/Account/add-account";
            }
        }

        accountService.save(account1);

        String uploadFolder = "D:\\fruisShop1\\photos\\tinnp";

        try {
            Files.copy(image.getInputStream(), Paths.get(uploadFolder + File.separator, image.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        attributes.addFlashAttribute("success", "added successfully");
        return "redirect:/admin-accounts";
    }
}
