package com.triquang.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.triquang.common.entity.Role;
import com.triquang.common.entity.User;

@Controller
public class UserController {

	@Autowired
	private UserService service;

	@GetMapping("/users")
	public String listAll(Model model) {
		List<User> listUsers = service.listAll();
		model.addAttribute("listUsers", listUsers);

		return "user";
	}

	@GetMapping("users/new")
	public String newUser(Model model) {
		List<Role> listRoles = service.listRole();
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");

		return "user_form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes) {
//		System.out.println(user);
		service.save(user);
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");

		return "redirect:/users";

	}

	@GetMapping("/user/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			User user = service.get(id);
			List<Role> listRoles = service.listRole();
			model.addAttribute("user", user);
			model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
			model.addAttribute("listRoles", listRoles);

			return "user_form";
		} catch (UserNotFoundException ex) {

			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}

	}

	@GetMapping("/user/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message", "The user Id " + id + " has been deleted successfully");
		} catch (UserNotFoundException ex) {

			redirectAttributes.addFlashAttribute("message", ex.getMessage());

		}
		return "redirect:/users";

	}

	@GetMapping("/user/{id}/enabled/{status}")
	public String updatedUserEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {

		service.updateUserEnableStatus(id, enabled);
		
		String status = enabled ? "enabled" : "disabled";
		String message = "This user Id " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/users";

	}

}
