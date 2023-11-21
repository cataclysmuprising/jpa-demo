package com.example.backend.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sec/admins")
public class AdminController extends BaseMVCController {
	@GetMapping
	public String home() {
		return "/admin/home";
	}

	@Override
	public void subInit(Model model) {
		setAuthorities(model, "Administrator");
	}
}
