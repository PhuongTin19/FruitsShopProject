package com.tin.admin.controller;

import com.tin.entity.Account;
import com.tin.entity.Blog;
import com.tin.service.AccountService;
import com.tin.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
public class BlogController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/admin-blogs/{pageNumber}")
    public String blogIndex(@PathVariable("pageNumber") int currentPage, Model model) {
    	if(currentPage < 1) currentPage = 1;
    	Page<Blog> page = blogService.blogPages(currentPage - 1);
    	int totalPages = page.getTotalPages();
    	if(currentPage > totalPages) {
    		currentPage = totalPages;
    		page = blogService.blogPages(currentPage - 1); 
    	}
    	long totalItems = page.getTotalElements();
    	List<Blog> blogs = page.getContent();
    	
        model.addAttribute("title", "Manage Blogs");
        model.addAttribute("blogs", blogs);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        
        return "admin/Blog/blogs";
    }

    @GetMapping("/add-blog")
    public String addBlogForm(Model model) {
        List<Account> accounts = accountService.findAllByRoleAdmin();
        model.addAttribute("accounts", accounts);
        model.addAttribute("title", "Add new Blog");
        return "admin/Blog/add-blog";
    }

    @PostMapping("/add-blog")
    public String addBlog(@RequestParam("imageFile") MultipartFile image,
                          @ModelAttribute("account") Blog blog,
                          Model model, RedirectAttributes attributes) {
        Blog blog1 = new Blog(null, null, null, null);
        List<Blog> blogs = blogService.findAll();
        blog1.setTitle(blog.getTitle());
        blog1.setAccount(blog.getAccount());
        blog1.setDescription(blog.getDescription());
        String filename = image.getOriginalFilename();

//        for (Blog blog2 : blogs) {
//            if (blog2.getTitle().equals(blog.getTitle())) {
//                attributes.addFlashAttribute("error", "duplicate title with another blog!");
//                return "redirect:/add-blog";
//            }
//        }
        if (filename.equals("")) {
            blog1.setImage("blog-1.jpg");
            blogService.save(blog1);
            attributes.addFlashAttribute("success", "added successfully with no image!");
            return "redirect:/admin-blogs";
        } else {
            String uploadDirectory = "D:\\fruisShopProject\\src\\main\\resources\\static\\user\\img\\blog";
            blog1.setImage(filename);
            blogService.save(blog1);

            try {
                Files.copy(image.getInputStream(), Paths.get(uploadDirectory + File.separator, image.getOriginalFilename()),
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        attributes.addFlashAttribute("success", "added successfully!");
        return "redirect:/admin-blogs";

    }

    @GetMapping("/update-blog/{id}")
    public String updateBlogForm(@PathVariable("id") Integer id,
                                 Model model) {
        Blog blog = blogService.findById(id);
        List<Account> accounts = accountService.findAllByRoleAdmin();
        model.addAttribute("blog", blog);
        model.addAttribute("accounts", accounts);
        model.addAttribute("title", "Update Blog");
        return "admin/Blog/update-blog";
    }

    @PostMapping("/update-blog/{id}")
    public String updateBlog(@RequestParam("imageFile") MultipartFile image,
                             @ModelAttribute("blog") Blog blog,
                             @PathVariable("id") Integer id, Model model, RedirectAttributes attributes) {

        Blog blog1 = new Blog();
        blog1.setBlog_id(id);
        blog1.setTitle(blog.getTitle());
        blog1.setAccount(blog.getAccount());
        blog1.setDescription(blog.getDescription());
        Blog blog2 = blogService.findById(id);
        blog1.setCreatedate(blog2.getCreatedate());

        List<Blog> blogs = blogService.findAll();
        for (Blog blog3 : blogs) {
            if (blog3.getTitle().equalsIgnoreCase(blog1.getTitle()) &&
                    blog3.getBlog_id() != blog1.getBlog_id()) {
                model.addAttribute("error", "duplicate title with another blog!");
                model.addAttribute("blog", blog1);
                return "admin/Blog/update-blog";
            }
        }

        String fileName = image.getOriginalFilename();

        if (fileName.equals("")) {
            blog1.setImage(blog2.getImage());
            blogService.save(blog1);
            attributes.addFlashAttribute("success", "Update Successdully!");
            return "redirect:/admin-blogs";
        }

        blog1.setImage(fileName);
        blogService.save(blog1);
        try {
            String uploadDirectory = "D:\\fruisShop1\\src\\main\\resources\\static\\user\\img\\blog";
            Files.copy(image.getInputStream(), Paths.get(uploadDirectory + File.separator, fileName),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        attributes.addFlashAttribute("success", "Update Successdully!");
        return "redirect:/admin-blogs";
    }
}
