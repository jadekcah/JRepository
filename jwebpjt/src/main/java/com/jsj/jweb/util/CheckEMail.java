package com.jsj.jweb.util;

import com.jsj.jweb.dao.UserDao;
import com.jsj.jweb.dto.UserDto;

public class CheckEMail {
	private String eMail;
	private boolean usable = false;
	private boolean social = false;
	
	
	public CheckEMail() {
		
	}
	
	public CheckEMail(String eMail) {
		this.eMail = eMail;
	}
	

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public boolean isUsable() {
		return usable;
	}
	
	public boolean isSocial() {
		return social;
	}

	public void check() {
		// �̸��� ���� üũ
		if(eMail.indexOf("@") < 0) return;
		
		// �̹� �����ϴ��� ����
		UserDao dao = new UserDao();
		UserDto dto;
		
		dto = dao.getUserProp(eMail);
		
		if(dto != null) {
			social = dto.isSocial();
			return;
		}
		
		usable = true;
	}
}
