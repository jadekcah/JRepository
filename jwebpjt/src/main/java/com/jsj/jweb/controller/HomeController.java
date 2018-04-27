package com.jsj.jweb.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jsj.jweb.command.JCommand;
import com.jsj.jweb.command.UserInfoCommand;
import com.jsj.jweb.util.Constant;

@Controller
public class HomeController {
	private JCommand command;
	private JdbcTemplate jdbcTemplate;
	
	@Autowired	//jdbc template ¼³Á¤
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		Constant.jdbcTemplate = this.jdbcTemplate;
	}
	
	@RequestMapping("/")
	public String home(Model model, Principal principal, HttpSession session){
		System.out.println("in Home!");
		if(principal != null) {
			System.out.println(principal.getName());
			model.addAttribute("loggedIn", principal.getName());
			model.addAttribute("need", "all");
			//SecurityContext sc = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
			
			//System.out.println(sc != null ? sc.getAuthentication() : "none" );
			
			command = new UserInfoCommand();
			command.execute(model);
		}
		
		return "home";
	}
}
