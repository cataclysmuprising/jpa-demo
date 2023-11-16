package com.example.backend.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
	@GetMapping
	public String home() {
		return "redirect:/sec/dashboard";
	}

	@GetMapping("/sec/dashboard")
	public String dashboardPage(Model model) {
		return "dashboard";
	}
}
