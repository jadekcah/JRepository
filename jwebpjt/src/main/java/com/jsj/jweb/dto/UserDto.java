package com.jsj.jweb.dto;

import java.util.regex.Pattern;

import com.jsj.jweb.util.CheckEMail;

public class UserDto {
	private int userNum;
	private String eMail;
	private String pw;
	private String auth = "ROLE_USER";
	private String name;
	private String nickname;
	private boolean social;
	
	private boolean socialChanged = false;
	
	public UserDto() {
		
	}
	
	public UserDto(int userNum, String eMail, String pw, String auth, String name, String nickname, boolean social) {
		this.userNum = userNum;
		this.eMail = eMail;
		this.pw = pw;
		this.auth = auth;
		this.name = name;
		this.nickname = nickname;
		this.social = social;
	}
	
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
	public String geteMail() {
		return eMail;
	}
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public boolean isSocial() {
		return social;
	}
	public void setSocial(boolean social) {
		this.socialChanged = true;
		this.social = social;
	}
	
	
	public String[] getUpdateFields() {
		String updateField = "";
		if(socialChanged) updateField += ",social";
		if(pw != null && !pw.equals("")) updateField += ",pw";
		if(auth != null && !auth.equals("")) updateField += ",auth";
		if(name != null && !name.equals("")) updateField += ",name";
		if(nickname != null && !nickname.equals("")) updateField += ",nickname";
		
		if(updateField.startsWith(",")) updateField = updateField.substring(1);
		
		System.out.println("Update Fields : " + updateField);
		
		return updateField.split(",");
	}

	
	public Object getVarByName(String vname) {
		
		switch(vname) {
		case "eMail":
			return eMail;
		case "pw":
			return pw;
		case "auth":
			return auth;
		case "name":
			return name;
		case "nickname":
			return nickname;
		case "social":
			return social;
		default:
			return null;
		}
	}

	//for sign up
	public boolean verify() {
		// �ʼ� �Է� �ʵ� ������ false
		if(eMail == null || pw == null) return false;
		
		// ���� ����
		eMail = eMail.replaceAll(" ", "");
		pw = pw.replaceAll(" ", "");
		
		if(name != null && !name.equals("")) name.replaceAll(" ", "");
		else name = null;
		if(nickname != null && !nickname.equals("")) nickname.replaceAll(" ", "");
		else nickname = null;
		
		// ���� ��� ���ɿ��� �����
		CheckEMail ckMail = new CheckEMail(eMail);
		ckMail.check();
		if(!ckMail.isUsable())
			if(!ckMail.isSocial()) return false;
		// �н����� ��ȿ�� ����
		if(!Pattern.matches("^.*(?=.{8,20})(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W).*$", pw)) return false;
		
		return true;
	}
	
	public void toRandomNick() {
		int r = (int) (Math.random() * 8);
		switch(r) {
		case 0:
			nickname = "ö��";
			break;
		case 1:
			nickname = "����";
			break;
		case 2:
			nickname = "�ͱ�";
			break;
		case 3:
			nickname = "����";
			break;
		case 4:
			nickname = "�浿";
			break;
		case 5:
			nickname = "����";
			break;
		case 6:
			nickname = "����";
			break;
		case 7:
			nickname = "�ϴ�";
			break;
		default:
			nickname = "Jadekcah";
		}
	}
}
