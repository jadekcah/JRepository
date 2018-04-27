package com.jsj.jweb.util;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.jsj.jweb.dao.UserDao;
import com.jsj.jweb.dto.UserDto;

public class LogInService implements UserDetailsService {
	public static final boolean SOCIAL_LOGIN = true;
	public static final boolean NOT_SOCIAL_LOGIN = false;
	
	boolean isSocialLogIn = false;
	
	public LogInService() {
		
	}
	
	public LogInService(boolean isSocialLogIn) {
		this.isSocialLogIn = isSocialLogIn;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {	
		// TODO Auto-generated method stub
		
		UserDao dao = new UserDao();
	    UserDto dto = dao.getUserProp(username);
	    
	    System.out.println(username);
		
		if (dto == null || (dto.isSocial() && !isSocialLogIn)) {	// 아이디가 없거나 소셜 아이디인데 소셜 로그인이 아닐 때..
		    throw new UsernameNotFoundException("No user found with username" + username);
		}
		
		Collection<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
	    roles.add(new SimpleGrantedAuthority(dto.getAuth()));	
	    
	    UserDetails user = new User(username, isSocialLogIn ? dto.geteMail() : dto.getPw(), roles);
		
        return user;
	}
	
}
