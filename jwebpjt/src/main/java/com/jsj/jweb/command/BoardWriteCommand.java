package com.jsj.jweb.command;

import java.util.Map;

import org.springframework.ui.Model;

import com.jsj.jweb.dao.BoardDao;
import com.jsj.jweb.dto.BoardDto;

public class BoardWriteCommand implements JCommand {

	@Override
	public void execute(Model model) {
		// TODO Auto-generated method stub
		
		Map<String, Object> map = model.asMap();
		
		BoardDto dto = (BoardDto) map.get("dto");
		
		BoardDao dao = new BoardDao();
		dao.write(dto);
	}

}
