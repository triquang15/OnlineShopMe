package com.triquang;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.triquang.category.CategoryService;
import com.triquang.common.entity.Category;

@Controller
public class MainController {
	
	@Autowired
	private CategoryService categoryService;

	@GetMapping("")
	public String viewHomePage(Model model) {
		List<Category> listCategories = categoryService.listNoChildrentCategories();
		model.addAttribute("listCategories", listCategories);
		
		return "index";
	}

}
