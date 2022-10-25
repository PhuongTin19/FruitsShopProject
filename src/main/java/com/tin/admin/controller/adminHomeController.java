package com.tin.admin.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
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
import com.tin.entity.Discount;
import com.tin.entityDto.CategoryDto;
import com.tin.service.CategoryService;
import com.tin.service.DiscountService;
 
@Controller
public class adminHomeController {
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private DiscountService discountService;
	
	@RequestMapping(value = "/admin-index", method = RequestMethod.GET)
	public String adminIndex(Model model) {
		model.addAttribute("title", "Admin Index");
		return "admin/index";
	}
	
	
	
	@GetMapping("/admin-discounts")
	public String adminDiscounts(Model model) {
		List<Discount> discounts = discountService.findAll();
		model.addAttribute("size", discounts.size());
		model.addAttribute("message","Discount List is empty!!!");
		model.addAttribute("discounts", discounts);
		model.addAttribute("title", "Manage Discounts");
		return "admin/Discount/discounts";
	}
	
	@GetMapping("/add-discount")
	public String goFormAddDiscount(Model model) {
		model.addAttribute("title", "Add Discount");
		return "admin/Discount/add-discount";
	}
	
	@RequestMapping(value = "/add-new-discount", method = RequestMethod.POST)
	public String addNewDiscount(@ModelAttribute("discount") Discount discount,
									Model model,
									RedirectAttributes attributes) {
			try {
			Discount discountSave = new Discount();			
			discountSave.setName(discount.getName());
			discountSave.setIs_enable(discount.getIs_enable());
			discountSave.setStart_time(discount.getStart_time());
			discountSave.setEnd_time(discount.getEnd_time());
			discountSave.setDiscount(discount.getDiscount());
			
			List<Discount> discounts = discountService.findAll();
			for(Discount discountLoop : discounts) {
				if(discountLoop.getName().equalsIgnoreCase(discountSave.getName())) {
					attributes.addFlashAttribute("error", "Your new discount is duplicate name "
							+ "with another discount!");
					return "redirect:/admin-discounts";
				}
			}
			if (discountSave.getStart_time().after(discountSave.getEnd_time())) {
				model.addAttribute("error", "Your start time must early than end time!");
				model.addAttribute("discount", discountSave);
				return "admin/Discount/add-discount";
			}
			if (discountSave.getDiscount() < 0 || discountSave.getDiscount() > 100) {
				model.addAttribute("error", "Your discount must between 0 to 100 percent!");
				model.addAttribute("discount", discountSave);
				return "admin/Discount/edit-discount";
			}
			discountService.save(discountSave);
			attributes.addFlashAttribute("success", "Added Successfully !");
			return "redirect:/admin-discounts";
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("error", "Can't add new discount!");
			return "redirect:/admin-disounts";
		}
	}
	
	@GetMapping(value="/edit-discount/{id}")
	public String goFormEditDiscount(@PathVariable("id") Integer id,
										Model model,
										RedirectAttributes attributes) {
		try {
			Discount discount = discountService.findByDiscount_id(id);
			model.addAttribute("discount", discount);
			return "admin/Discount/edit-discount";
		} catch (Exception e) {
			attributes.addFlashAttribute("error", "Have some wrong, please try again!");
			return "redirect:/admin-discounts";
		}	
	}
	
	@PostMapping("/save-update-discount/{id}")
	public String saveUpdateDiscount(@PathVariable("id") Integer id,
									 @ModelAttribute("discount") Discount discount,
										RedirectAttributes attributes,
										Model model) {
		try {
			Discount discountSave = new Discount();
			discountSave.setDiscount(discount.getDiscount());
			discountSave.setName(discount.getName());
			discountSave.setStart_time(discount.getStart_time());
			discountSave.setEnd_time(discount.getEnd_time());
			discountSave.setDiscount_id(id);
			discountSave.setIs_enable(discount.getIs_enable());
			
			List<Discount> discounts = discountService.findAll();
			for (Discount discount2 : discounts) {
				if(discount2.getName().equalsIgnoreCase(discountSave.getName()) 
						&& (discount2.getDiscount_id() != discountSave.getDiscount_id())) {
					model.addAttribute("error", "Your discount's name is duplicate with another discount!");
					model.addAttribute("discount", discountSave);
					return "admin/Discount/edit-discount";
				}
			}
			
//			Discount oldDiscount = discountService.findByDiscount_id(id);
//			if (oldDiscount.getStart_time().after(discountSave.getStart_time())) {
//				model.addAttribute("error", "Your start time must later than old start time!");
//				model.addAttribute("discount", discountSave);
//				return "admin/edit-discount";
//			}
			
			if (discountSave.getStart_time().before(discountSave.getEnd_time())) {
				model.addAttribute("error", "Your start time must early than end time!");
				model.addAttribute("discount", discountSave);
				return "admin/Discount/edit-discount";
			}
			if (discountSave.getDiscount() < 0 || discountSave.getDiscount() > 100) {
				model.addAttribute("error", "Your discount must between 0 to 100 percent!");
				model.addAttribute("discount", discountSave);
				return "admin/Discount/edit-discount";
			}
			
//			discountService.updateDiscountById(id, discountSave.getName(), discountSave.getDiscount(), 
//					discountSave.getStart_time() , discountSave.getEnd_time(), discountSave.getIs_enable());
			discountService.save(discountSave);
			
			attributes.addFlashAttribute("success", "Updated successfully!");
			return "redirect:/admin-discounts";
			
		} catch (Exception e) {
			return "redirect:/admin-discounts";
		}
		
	}
}
