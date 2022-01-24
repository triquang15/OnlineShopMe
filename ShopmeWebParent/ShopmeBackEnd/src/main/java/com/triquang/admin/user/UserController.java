package com.triquang.admin.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.triquang.admin.FileUploadUtil;
import com.triquang.admin.user.export.UserCsvExport;
import com.triquang.admin.user.export.UserExcelExporter;
import com.triquang.admin.user.export.UserPdfExporter;
import com.triquang.common.entity.Role;
import com.triquang.common.entity.User;

@Controller
public class UserController {

	@Autowired
	private UserService service;

	@GetMapping("/users")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "firstName", "asc", null);
	}

	@GetMapping("users/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {
		System.out.println("Sort Field: " + sortField);
		System.out.println("Sort Order: " + sortDir);
		Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<User> listUsers = page.getContent();

		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
		long endCount = startCount + UserService.USERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);

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
	public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile file)
			throws IOException {
		if (!file.isEmpty()) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = service.save(user);
			String uploadDir = "user-photos/" + savedUser.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, file);
		} else {
			if (user.getPhotos().isEmpty())
				user.setPhotos(null);
			service.save(user);
		}

		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");

		return getRedirectURLtoAffectedUser(user);

	}

	private String getRedirectURLtoAffectedUser(User user) {
		String firstPartOfEmail = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
	}

	@GetMapping("/user/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			User user = service.get(id);
			List<Role> listRoles = service.listRole();
			model.addAttribute("user", user);
			model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
			model.addAttribute("listRoles", listRoles);

			return "update_user";
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

	@GetMapping("/users/export/csv")
	public void exportToCsv(HttpServletResponse response) throws IOException {

		List<User> listUsers = service.listAll();
		UserCsvExport exporter = new UserCsvExport();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);
		
	}
	
	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		
		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(listUsers, response);
		
	}

}
