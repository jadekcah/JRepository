package com.jsj.jweb.command;

import java.util.Map;

import org.springframework.ui.Model;

import com.jsj.jweb.dao.BoardDao;
import com.jsj.jweb.dto.BoardDto;

public class BoardInfoCommand implements JCommand {

	@Override
	public void execute(Model model) {
		// TODO Auto-generated method stub
		
		Map<String, Object> map = model.asMap();
		int bId = (Integer) map.get("bId");
		String need = (String) map.get("need");
		
		BoardDao dao = new BoardDao();
		BoardDto dto = dao.getBoardProp(bId);
		
		if(dto == null) {
			model.addAttribute("need", null);
			return;
		}
		
		switch(need) {
		case "all":
			model.addAttribute("need", dto);
			break;
		default:
			model.addAttribute("need", dto.getVarByName(need));
		}
	}

}
