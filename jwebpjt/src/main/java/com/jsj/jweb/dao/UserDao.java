package com.jsj.jweb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;

import com.jsj.jweb.dto.UserDto;
import com.jsj.jweb.util.Constant;

public class UserDao {
	private JdbcTemplate jdbcTemplate;
	
	public UserDao() {
		this.jdbcTemplate = Constant.jdbcTemplate;
	}
	
	public UserDto getUserProp(String eMail) {
		
		String query = "select * from users where eMail = '" + eMail + "'";
		
		UserDto dto = null;
		try {
			dto = jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<UserDto>(UserDto.class));
		}catch(Exception e) {
			return null;
		}
		
		return dto;
	}
	
	public void createAccount(UserDto dto) {
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub
				String query = "insert into users (userNum, eMail, pw, auth, name, nickname, social) values (user_seq.nextval, ?, ?, ?, ?, ?, ?)";
				PreparedStatement preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, dto.geteMail());
				preparedStatement.setString(2, dto.getPw());
				preparedStatement.setString(3, dto.getAuth());
				preparedStatement.setString(4, dto.getName());
				preparedStatement.setString(5, dto.getNickname());
				preparedStatement.setBoolean(6, dto.isSocial());
				
				return preparedStatement;
			}
		});
	}
	
	public void updateProfile(UserDto dto, String ...fields) {
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub
				PreparedStatement preparedStatement;
				
				if (fields.length > 0) {
					String query = "update users set ";
					for(String field : fields) {
						query += field + " = ?,";
					}
					query = query.substring(0, query.length() - 1) + " where eMail = ?";
					preparedStatement = con.prepareStatement(query);
					
					int i = 0;
					for(i = 0; i < fields.length; i++) {
						Object obj = dto.getVarByName(fields[i]);
						System.out.println(obj.getClass().getName());
						switch(obj.getClass().getName()) {
						case "java.lang.String":
							preparedStatement.setString(i + 1, (String) obj);
							break;
						case "java.lang.Integer":
							preparedStatement.setInt(i + 1, (Integer) obj);
							break;
						case "java.lang.Boolean":
							preparedStatement.setBoolean(i + 1, (Boolean) obj);
							break;
						}
					}
					
					preparedStatement.setString(i + 1, dto.geteMail());
				} else {
					String query = "update users set pw = ?, auth = ?, name = ?, nickname = ?, social = ? where eMail = ?";
					preparedStatement = con.prepareStatement(query);
					preparedStatement.setString(1, dto.getPw());
					preparedStatement.setString(2, dto.getAuth());
					preparedStatement.setString(3, dto.getName());
					preparedStatement.setString(4, dto.getNickname());
					preparedStatement.setBoolean(5, dto.isSocial());
					preparedStatement.setString(6, dto.geteMail());
				}
				
				return preparedStatement;
			}
		});
	}
	
	public void deleteAccount(String eMail) {
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub
				PreparedStatement preparedStatement;
				
				String query = "delete from users where eMail = ?";
				preparedStatement = con.prepareStatement(query);
				
				preparedStatement.setString(1, eMail);
				
				return preparedStatement;
			}
		});
	}
}
