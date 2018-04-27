package com.jsj.jweb.command;

import java.util.Map;

import org.springframework.ui.Model;

import com.jsj.jweb.dao.BoardDao;

public class BoardDeleteCommand implements JCommand {

	@Override
	public void execute(Model model) {
		// TODO Auto-generated method stub
		
		Map<String, Object> map = model.asMap();
		String strId = (String) map.get("strId");
		
		BoardDao dao = new BoardDao();
		dao.delete(strId);
	}

}
