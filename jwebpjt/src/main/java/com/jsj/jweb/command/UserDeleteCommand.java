package com.jsj.jweb.command;

import java.util.Map;

import org.springframework.ui.Model;

import com.jsj.jweb.dao.UserDao;

public class UserDeleteCommand implements JCommand {

	@Override
	public void execute(Model model) {
		// TODO Auto-generated method stub
		
		Map<String, Object> map = model.asMap();
		String eMail = (String) map.get("eMail");
		
		UserDao dao = new UserDao();
		
		dao.deleteAccount(eMail);
	}
}
