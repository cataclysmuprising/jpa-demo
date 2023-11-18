package com.example.backend.controller.mvc;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController extends BaseMVCController {
	@GetMapping
	public String home() {
		return "redirect:/sec/dashboard";
	}

	@GetMapping("/sec/dashboard")
	public String dashboardPage(Model model, HttpServletRequest request) {
		return "dashboard";
	}

	@Override
	public void subInit(Model model) {
		setAuthorities(model, "Dashboard");
	}
}
