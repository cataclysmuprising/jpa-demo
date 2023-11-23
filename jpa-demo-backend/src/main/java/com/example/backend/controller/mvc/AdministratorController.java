package com.example.backend.controller.mvc;

import com.example.backend.common.annotation.MVCLoggable;
import com.example.backend.common.annotation.ValidateEntity;
import com.example.backend.common.response.PageMessageStyle;
import com.example.backend.common.response.PageMode;
import com.example.backend.validators.AdminValidator;
import com.example.persistence.dto.AdministratorDTO;
import com.example.persistence.exception.BusinessException;
import com.example.persistence.exception.DuplicatedEntryException;
import com.example.persistence.service.AdministratorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	@Override
	public void subInit(Model model) {
		setAuthorities(model, "Administrator");
	}
}
