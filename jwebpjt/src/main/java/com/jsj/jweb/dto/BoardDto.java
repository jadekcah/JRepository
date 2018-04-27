package com.jsj.jweb.dto;

import java.sql.Timestamp;

public class BoardDto {
	private int bId;
	private String eMail;
	private String bTitle;
	private String bContent;
	private Timestamp bDate;
	private int bHit;
	private int bGroup;
	private int bStep;
	private int bIndent;
	private String nickname;	//User Nickname
	
	public BoardDto(){
		
	}
	
	public BoardDto(int bId, String eMail, String bTitle, String bContent, Timestamp bDate, int bHit, int bGroup, int bStep,
			int bIndent, String nickname) {
		this.bId = bId;
		this.eMail = eMail;
		this.bTitle = bTitle;
		this.bContent = bContent;
		this.bDate = bDate;
		this.bHit = bHit;
		this.bGroup = bGroup;
		this.bStep = bStep;
		this.bIndent = bIndent;
		this.nickname = nickname;
	}
	public int getbId() {
		return bId;
	}
	public void setbId(int bId) {
		this.bId = bId;
	}
	public String geteMail() {
		return eMail;
	}
	public void seteMail(String eMail){
		this.eMail = eMail;
	}
	public String getbTitle() {
		return bTitle;
	}
	public void setbTitle(String bTitle) {
		this.bTitle = bTitle;
	}
	public String getbContent() {
		return bContent;
	}
	public void setbContent(String bContent) {
		this.bContent = bContent;
	}
	public Timestamp getbDate() {
		return bDate;
	}
	public void setbDate(Timestamp bDate) {
		this.bDate = bDate;
	}
	public int getbHit() {
		return bHit;
	}
	public void setbHit(int bHit) {
		this.bHit = bHit;
	}
	public int getbGroup() {
		return bGroup;
	}
	public void setbGroup(int bGroup) {
		this.bGroup = bGroup;
	}
	public int getbStep() {
		return bStep;
	}
	public void setbStep(int bStep) {
		this.bStep = bStep;
	}
	public int getbIndent() {
		return bIndent;
	}
	public void setbIndent(int bIndent) {
		this.bIndent = bIndent;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Object getVarByName(String vname) {
		
		switch(vname) {
		case "eMail":
			return eMail;
		case "bTitle":
			return bTitle;
		case "bContent":
			return bContent;
		default:
			return null;
		}
	}
}
