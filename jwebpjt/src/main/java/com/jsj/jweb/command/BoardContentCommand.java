package com.jsj.jweb.command;

import java.util.Map;

import org.springframework.ui.Model;

import com.jsj.jweb.dao.BoardDao;
import com.jsj.jweb.dto.BoardDto;

public class BoardContentCommand implements JCommand {

	@Override
	public void execute(Model model) {
		// TODO Auto-generated method stub
		Map<String, Object> map = model.asMap();
		String strId = (String) map.get("bId");
		

		BoardDao dao = new BoardDao();
		BoardDto dto = dao.contentView(strId);
		
		dao.upHit(strId);
		
		model.addAttribute("content_view", dto);
	}

}
