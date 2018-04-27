package com.jsj.jweb.command;

import java.util.Map;

import org.springframework.ui.Model;

import com.jsj.jweb.dao.BoardDao;
import com.jsj.jweb.dto.BoardDto;

public class BoardModifyCommand implements JCommand {

	@Override
	public void execute(Model model) {
		// TODO Auto-generated method stub
		
		Map<String, Object> map = model.asMap();
		BoardDto dto = (BoardDto) map.get("dto");
		
		BoardDao dao = new BoardDao();
		
		/*dto.setbId(Integer.parseInt(request.getParameter("bId")));
		dto.setbTitle(request.getParameter("bTitle"));
		dto.setbContent(request.getParameter("bContent"));*/
		//dto.setbDate(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())));
		
		dao.modify(dto);
	}

}
