package com.jsj.jweb.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageToHash {
	private String msg;
	private String hashed;
	
	public MessageToHash() {
		
	}
	
	public MessageToHash(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getHashed() {
		return hashed;
	}
	
	
	public void toSHA256() {
        String rtnSHA = "";
        
        try{
            MessageDigest sh = MessageDigest.getInstance("SHA-256"); 
            sh.update(msg.getBytes()); 
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer(); 
            
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            rtnSHA = sb.toString();
            
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace(); 
            rtnSHA = null; 
        }
        
        hashed = rtnSHA;
    }
}
