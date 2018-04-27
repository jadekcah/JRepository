package com.jsj.jweb.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jsj.jweb.command.BoardContentCommand;
import com.jsj.jweb.command.BoardDeleteCommand;
import com.jsj.jweb.command.BoardInfoCommand;
import com.jsj.jweb.command.BoardListCommand;
import com.jsj.jweb.command.BoardModifyCommand;
import com.jsj.jweb.command.BoardReplyCommand;
import com.jsj.jweb.command.BoardReplyViewCommand;
import com.jsj.jweb.command.BoardWriteCommand;
import com.jsj.jweb.command.JCommand;
import com.jsj.jweb.command.UserInfoCommand;
import com.jsj.jweb.dto.BoardDto;

@Controller
public class BoardController {
	private JCommand command;
	
	@RequestMapping("/board/list_elements")
	public @ResponseBody ArrayList<BoardDto> listElements(Model model){
		System.out.println("list_elements");

		command = new BoardListCommand();
		command.execute(model);
		
		Map<String, Object> map = model.asMap();
		@SuppressWarnings("unchecked")
		ArrayList<BoardDto> dtos = (ArrayList<BoardDto>) map.get("list");
		
		return dtos;
	}
	
	@RequestMapping("/board/list")
	public String list(){
		System.out.println("list");
		
		return "/board/list_view";
	}
	
	@RequestMapping("/board/write_view")
	public String write_view(Model model, Principal principal){
		System.out.println("write_view");
		
		model.addAttribute("loggedIn", principal.getName());
		model.addAttribute("need", "nickname");
		
		command = new UserInfoCommand();
		command.execute(model);
		
		return "/board/write_view";
	}
	
	@RequestMapping(value = "/board/write", method = RequestMethod.POST)
	public @ResponseBody boolean write(@RequestBody BoardDto dto, Model model, Principal principal){
		System.out.println("write");
		
		dto.seteMail(principal.getName());
		
		model.addAttribute("dto", dto);
		command = new BoardWriteCommand();
		command.execute(model);
		
		System.out.println(dto.geteMail());
		
		return true;
	}
	
	@RequestMapping("/board/content_view")
	public String content_view(@RequestParam("bId") String bId, Model model){
		System.out.println("content_view");
		
		System.out.println(bId);
		
		model.addAttribute("bId", bId);
		command = new BoardContentCommand();
		command.execute(model);
		
		return "/board/content_view";
	}
	
	@RequestMapping(value = "/board/modify", method = RequestMethod.POST)
	public @ResponseBody boolean modify(@RequestBody BoardDto dto, Model model, Principal principal){
		System.out.println("modify");
		
		model.addAttribute("bId", dto.getbId());
		model.addAttribute("need", "eMail");
		command = new BoardInfoCommand();
		command.execute(model);
		
		Map<String, Object> map = model.asMap();
		String bEMail = (String) map.get("need");
		String userEMail = principal.getName();
		
		// 자신의 글이 아닐 경우
		if(!userEMail.equals("jwebadmin") && (bEMail == null || !bEMail.equals(userEMail))) {
			return false;
		}
		
		model.addAttribute("dto", dto);
		command = new BoardModifyCommand();
		command.execute(model);
		
		return true;
	}
	
	@RequestMapping("/board/reply_view")
	public String reply_view(@RequestParam("bId") String strId, Model model, Principal principal){
		System.out.println("reply_view");
		
		model.addAttribute("loggedIn", principal.getName());
		model.addAttribute("need", "nickname");
		command = new UserInfoCommand();
		command.execute(model);
		
		model.addAttribute("strId", strId);
		command = new BoardReplyViewCommand();
		command.execute(model);
		
		return "/board/reply_view";
	}
	
	@RequestMapping("/board/reply")
	public @ResponseBody boolean reply(@RequestBody BoardDto dto, Model model, Principal principal){
		System.out.println("reply");
		
		dto.seteMail(principal.getName());
		
		model.addAttribute("dto", dto);
		command = new BoardReplyCommand();
		command.execute(model);
		
		return true;
	}
	
	@RequestMapping("/board/delete")
	public @ResponseBody boolean delete(@RequestParam("bId") String strId, Model model, Principal principal){
		System.out.println("delete");
		
		model.addAttribute("bId", Integer.parseInt(strId));
		model.addAttribute("need", "eMail");
		command = new BoardInfoCommand();
		command.execute(model);
		
		Map<String, Object> map = model.asMap();
		String bEMail = (String) map.get("need");
		String userEMail = principal.getName();
		
		// 자신의 글이 아닐 경우
		if(!userEMail.equals("jwebadmin") && (bEMail == null || !bEMail.equals(userEMail))) {
			return false;
		}
		
		model.addAttribute("strId", strId);
		command = new BoardDeleteCommand();
		command.execute(model);
		
		return true;
	}
}
