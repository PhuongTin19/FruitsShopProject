package com.tin.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tin.entity.Category;
import com.tin.service.CategoryService;
import com.tin.service.DiscountService;

@Controller
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private DiscountService discountService;

	
	@RequestMapping(value = "/admin-categories", method = RequestMethod.GET)
	public String adminCategories(Model model) {
		
		List<Category> categories = categoryService.adminFindAll();
		model.addAttribute("categories", categories);
		model.addAttribute("size", categories.size());
		model.addAttribute("message","Categories List is empty!!!");
		model.addAttribute("title", "Manage Categories");
		return "admin/categories";
	}
	
	@RequestMapping(value = "/add-category")
	public String goFormAddCategory(Model model) {
		model.addAttribute("title", "Add Category");
		return "admin/add-category";
	}
	
	@PostMapping(value = "/add-new-category")
	public String saveNewCategory(@RequestParam("name") String name, 
									Model model, 
									RedirectAttributes attributes) {
		
		try {
			Category categorySave = new Category();
			categorySave.setName(name);
			categorySave.setIs_enable(true);
			
			/*Check duplicate category*/
			List<Category> categories = categoryService.findAll();
			for (Category category : categories) {
				if (categorySave.getName().equalsIgnoreCase(category.getName())) {
					attributes.addFlashAttribute("error", "Duplicate category, add failed.");
					return "redirect:/admin-categories";
				}
			}
			
			categoryService.save(categorySave);
			attributes.addFlashAttribute("success", "Added Successfully");

			return "redirect:/admin-categories";
		} catch(Exception e) {
			attributes.addFlashAttribute("fail", "Server has error.");
			return "redirect:/admin-categories";
		}

	}
	
	@GetMapping("/edit-category/{id}")
	public String goFormEditCategory(@PathVariable("id") int id,
										Model model) {
		try {
			Category category = categoryService.findCategoryById(id);
			model.addAttribute("title", "Edit Category");
			model.addAttribute("category", category);
			return "admin/edit-category";
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@PostMapping(value = "/save-edit-category/{id}")
	public String saveEditCategory(@ModelAttribute("category") Category category,
									@PathVariable("id") int id,
									RedirectAttributes attributes) {
		
		try {
			Category categorySave = new Category();
			categorySave.setCategory_id(id);
			categorySave.setName(category.getName());
			categorySave.setIs_enable(category.getIs_enable());
			
			List<Category> categories = categoryService.findAll();
			for(Category category1 : categories) {
				if ((category1.getName().equalsIgnoreCase(categorySave.getName())) 
						&& (category1.getCategory_id() != categorySave.getCategory_id())) {
					
					attributes.addFlashAttribute("error", "Can't update because duplicate name another category!");
					return "redirect:/admin-categories";
				}
			}
			categoryService.updateCategory(categorySave.getCategory_id(), categorySave.getName(), categorySave.getIs_enable());
			attributes.addFlashAttribute("success", "update success!");
			return "redirect:/admin-categories";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
