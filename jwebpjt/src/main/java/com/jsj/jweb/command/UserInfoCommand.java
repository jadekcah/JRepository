package com.jsj.jweb.command;

import java.util.Map;

import org.springframework.ui.Model;

import com.jsj.jweb.dao.UserDao;
import com.jsj.jweb.dto.UserDto;

public class UserInfoCommand implements JCommand {

	@Override
	public void execute(Model model) {
		// TODO Auto-generated method stub
		
		Map<String, Object> map = model.asMap();
		String username = (String) map.get("loggedIn");
		String need = (String) map.get("need");
		
		UserDao dao = new UserDao();
		UserDto dto = dao.getUserProp(username);
		
		//System.out.println("loggedIn " + username + ", need " + need + ", dto : " + dto);
		
		if(dto == null) {
			model.addAttribute("need", null);
			return;
		}
		
		dto.setPw(null);
		
		switch(need) {
		case "all":
			model.addAttribute("need", dto);
			break;
		default:
			model.addAttribute("need", dto.getVarByName(need));
		}
	}

}
