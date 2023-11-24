package com.example.backend.controller.mvc;

import com.example.backend.common.annotation.MVCLoggable;
import com.example.backend.common.annotation.ValidateEntity;
import com.example.backend.common.response.PageMessageStyle;
import com.example.backend.common.response.PageMode;
import com.example.backend.validators.AdminValidator;
import com.example.persistence.criteria.AdministratorCriteria;
import com.example.persistence.criteria.RoleCriteria;
import com.example.persistence.dto.AdministratorDTO;
import com.example.persistence.dto.RoleDTO;
import com.example.persistence.exception.BusinessException;
import com.example.persistence.exception.ContentNotFoundException;
import com.example.persistence.exception.DuplicatedEntryException;
import com.example.persistence.service.AdministratorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
@MVCLoggable(profile = "dev")
@RequestMapping("/sec/admins")
public class AdministratorController extends BaseMVCController {

	private static final Logger applicationLogger = LogManager.getLogger("applicationLogs." + AdministratorController.class.getName());

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AdministratorService administratorService;

	@GetMapping
	public String home() {
		return "/admin/home";
	}

	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("pageMode", PageMode.CREATE);
		model.addAttribute("adminDto", new AdministratorDTO());
		return "/admin/input";
	}

	@PostMapping("/add")
	@ValidateEntity(validator = AdminValidator.class, errorView = "/admin/input", pageMode = PageMode.CREATE)
	public String add(Model model, RedirectAttributes redirectAttributes, @ModelAttribute("adminDto") AdministratorDTO adminDto, BindingResult bindResult) throws BusinessException, DuplicatedEntryException {
		adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
		long registeredAdminId = administratorService.createNewAdminWithRoles(adminDto, adminDto.getRoleIds(), getSignInClientId());
		applicationLogger.info("New administrator has been successfully registered with ID# <{}>", registeredAdminId);
		setPageMessage(redirectAttributes, "Success", "Notification.common.Page.SuccessfullyRegistered", PageMessageStyle.SUCCESS, "Administrator");
		return "redirect:/sec/admins";
	}

	@GetMapping("/{id}/edit")
	public String edit(@PathVariable long id, Model model) throws BusinessException {
		model.addAttribute("pageMode", PageMode.EDIT);
		AdministratorCriteria criteria = new AdministratorCriteria();
		criteria.setId(id);
		criteria.setRole(new RoleCriteria());

		AdministratorDTO adminDto = administratorService.findOne(criteria, "Administrator(roles)");
		if (adminDto == null) {
			throw new ContentNotFoundException("No matching administrator found for give ID # <" + id + ">");
		}
		adminDto.setRoleIds(adminDto.getRoles().stream().map(RoleDTO::getId).collect(Collectors.toSet()));

		model.addAttribute("adminDto", adminDto);

		return "/admin/input";
	}

	@PostMapping("/{adminId}/edit")
	@ValidateEntity(validator = AdminValidator.class, errorView = "/admin/input", pageMode = PageMode.EDIT)
	public String edit(Model model, @PathVariable long adminId, RedirectAttributes redirectAttributes, @ModelAttribute("adminDto") AdministratorDTO adminDto, BindingResult bindResult) throws BusinessException, DuplicatedEntryException {
		AdministratorCriteria criteria = new AdministratorCriteria();
		criteria.setId(adminId);
		if (!administratorService.exists(criteria)) {
			throw new ContentNotFoundException("No matching administrator found for give ID # <" + adminId + ">");
		}

		adminDto.setId(adminId);
		administratorService.updateAdminWithRoles(adminDto, adminDto.getRoleIds(), getSignInClientId());
		setPageMessage(redirectAttributes, "Success", "Notification.common.Page.SuccessfullyUpdated", PageMessageStyle.SUCCESS, "Administrator");
		return "redirect:/sec/admins";
	}

	@GetMapping("/{id}")
	public String detail(@PathVariable long id, Model model) {
		model.addAttribute("pageMode", PageMode.VIEW);
		AdministratorCriteria criteria = new AdministratorCriteria();
		criteria.setId(id);
		criteria.setRole(new RoleCriteria());

		AdministratorDTO adminDto = administratorService.findOne(criteria, "Administrator(roles)");
		if (adminDto == null) {
			throw new ContentNotFoundException("No matching administrator found for give ID # <" + id + ">");
		}
		model.addAttribute("adminDto", adminDto);

		String rolNames = String.join(",", adminDto.getRoles().stream().map(RoleDTO::getName).toList());
		model.addAttribute("roleNames", rolNames);
		return "/admin/detail";
	}

	@GetMapping("/{id}/delete")
	public String delete(@PathVariable long id, RedirectAttributes redirectAttributes) {
		// can't remove byself
		if (id == getSignInClientId()) {
			setPageMessage(redirectAttributes, "Error", "Notification.Administrator.Remove.Failed", PageMessageStyle.ERROR);
			return "redirect:/sec/admins";
		}
		AdministratorCriteria criteria = new AdministratorCriteria();
		criteria.setId(id);
		if (!administratorService.exists(criteria)) {
			throw new ContentNotFoundException("No matching administrator found for give ID # <" + id + ">");
		}
		administratorService.deleteById(id);
		setPageMessage(redirectAttributes, "Success", "Notification.common.Page.SuccessfullyRemoved", PageMessageStyle.SUCCESS, "Administrator");
		return "redirect:/sec/admins";
	}

	@Override
	public void subInit(Model model) {
		setAuthorities(model, "Administrator");
	}
}
