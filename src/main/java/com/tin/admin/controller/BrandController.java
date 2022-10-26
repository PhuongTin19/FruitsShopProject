package com.tin.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tin.entity.Brand;
import com.tin.service.BrandService;

@Controller
public class BrandController {
	@Autowired
	private BrandService brandService;
	
	@GetMapping(value = "/admin-brands")
	public String brandIndex(Model model) {
		List<Brand> brands = brandService.findAll();
		model.addAttribute("brandsList", brands);
		model.addAttribute("size", brands.size());
		model.addAttribute("title", "Manage Brands");
		model.addAttribute("message", "List brands is empty!");
		return "admin/Brand/brands";
	}
	
	@GetMapping(value = "/add-brand")
	public String formAddBrand(Model model) {
		model.addAttribute("title", "Add Brand");
		return "admin/Brand/add-brand";
	}
	
	@PostMapping(value = "/add-brand")
	public String doAddBrand(@ModelAttribute("brand") Brand brandPr,
			Model model,
			RedirectAttributes attributes) {
		try {
			Brand brand = new Brand();
			brand.setName(brandPr.getName());
			brand.setIs_enable(brandPr.getIs_enable());
			if (brand.getName().isEmpty()) {
				attributes.addFlashAttribute("error", "Must have brand name!");
				return "redirect:/add-brand";
			}
			List<Brand> brands = brandService.findAll();
			for (Brand brand1 : brands) {
				if (brand1.getName().equalsIgnoreCase(brand.getName())
						&& (brand.getBrand_id() != brand1.getBrand_id())) {
					model.addAttribute("error","Your brand name has duplicate with " +
							"another brand name!");
					model.addAttribute("brand", brand);
					return "admin/Brand/add-brand";
				}
			}
			brandService.save(brand);
			return "redirect:/admin-brands";
		} catch (Exception e) {
			return "redirect:/admin-brands";
		}
	}
	
	@GetMapping(value = "/update-brand/{id}")
	public String formUpdateBrand(@PathVariable("id") Integer id, Model model) {
		Brand brand = brandService.findByBrandId(id);
		model.addAttribute("brand", brand);
		return "admin/Brand/edit-brand";
	}
	
	@PostMapping(value = "/update-brand/{id}")
	public String doUpdateBrand(@PathVariable("id") Integer id, 
								Model model,
								@ModelAttribute("brand") Brand brandPr,
								RedirectAttributes attributes) {
		try {
			Brand brand = new Brand();
			brand.setBrand_id(id);
			brand.setName(brandPr.getName());
			brand.setIs_enable(brandPr.getIs_enable());
			
			List<Brand> brands = brandService.findAll();
			for(Brand brandLoop : brands) {
				if(brandLoop.getName().equalsIgnoreCase(brand.getName())
						&& brandLoop.getBrand_id() != brand.getBrand_id()) {
					model.addAttribute("brand", brand);
					model.addAttribute("error", "Your new brand name is duplicate another brand name");
					return "admin/Brand/edit-brand";
				}
			}
			brandService.save(brand);
			return "redirect:/admin-brands";
		} catch (Exception e) {
			return null;
		}
	}
	
}
