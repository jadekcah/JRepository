package com.jsj.jweb.command;

import java.util.Map;

import org.springframework.ui.Model;

import com.jsj.jweb.dao.BoardDao;
import com.jsj.jweb.dto.BoardDto;

public class BoardReplyViewCommand implements JCommand {

	@Override
	public void execute(Model model) {
		// TODO Auto-generated method stub

		Map<String, Object> map = model.asMap();
		String strId = (String) map.get("strId");
		String nickname = (String) map.get("need");

		BoardDao dao = new BoardDao();
		BoardDto dto = dao.contentView(strId);
		
		dto.setNickname(nickname);

		model.addAttribute("reply_view", dto);
	}

}
