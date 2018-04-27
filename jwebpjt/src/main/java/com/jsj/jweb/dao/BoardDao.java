package com.jsj.jweb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.jsj.jweb.dto.BoardDto;
import com.jsj.jweb.util.Constant;

public class BoardDao {
	private JdbcTemplate jdbcTemplate;
	
	public BoardDao(){
		this.jdbcTemplate = Constant.jdbcTemplate;
	}
	
	public BoardDto getBoardProp(int bId) {
		
		String query = "select * from board where bId = " + bId;
		BoardDto dto = null;
		try{
			dto = jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<BoardDto>(BoardDto.class));
		}catch(Exception e) {
			return null;
		}
		
		return dto;
	}
	
	public ArrayList<BoardDto> list(){
		
		String query = "select board.bId, board.bTitle, board.bHit, board.bDate, board.bGroup, board.bStep, board.bIndent, users.nickname from board left outer join users on board.email = users.email order by bGroup desc, bStep asc";
		ArrayList<BoardDto> dtos = (ArrayList<BoardDto>) jdbcTemplate.query(query, new BeanPropertyRowMapper<BoardDto>(BoardDto.class));
		
		return dtos;
	}
	
	public void write(BoardDto dto){
		jdbcTemplate.update(new PreparedStatementCreator(){

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub
				
				String query = "insert into board (bId, eMail, bTitle, bContent, bHit, bGroup, bStep, bIndent) values (board_seq.nextval, ?, ?, ?, 0, board_seq.currval, 0, 0)";
				PreparedStatement preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, dto.geteMail());
				preparedStatement.setString(2, dto.getbTitle());
				preparedStatement.setString(3, dto.getbContent());
				return preparedStatement;
			}
			
		});
	}
	
	public BoardDto contentView(String strId){
		
		String query = "select board.bId, board.bTitle, board.bContent, board.bHit, board.bGroup, board.bStep, board.bIndent, users.nickname from board left outer join users on board.email = users.email where bId = " + strId;
		BoardDto dto = jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<BoardDto>(BoardDto.class));
		
		return dto;
	}
	
	public void modify(BoardDto dto){
		jdbcTemplate.update(new PreparedStatementCreator(){

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub
				String query = "update board set bTitle = ?, bContent = ? where bId = ?";
				PreparedStatement preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, dto.getbTitle());
				preparedStatement.setString(2, dto.getbContent());
				preparedStatement.setInt(3, dto.getbId());
				
				return preparedStatement;
			}
			
		});
	}
	
	public void delete(String bId){
		
		jdbcTemplate.update(new PreparedStatementCreator(){

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub
				String query = "delete from board where bId = ?";
				PreparedStatement preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, bId);
				
				return preparedStatement;
			}
			
		});
	}

	public void reply(BoardDto dto){
		
		String query = "insert into board (bId, eMail, bTitle, bContent, bHit, bGroup, bStep, bIndent) values (board_seq.nextval, ?, ?, ?, 0, ?, ?, ?)";
		jdbcTemplate.update(query, new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				// TODO Auto-generated method stub
				preparedStatement.setString(1, dto.geteMail());
				preparedStatement.setString(2, dto.getbTitle());
				preparedStatement.setString(3, dto.getbContent());
				preparedStatement.setInt(4, dto.getbGroup());
				preparedStatement.setInt(5, dto.getbStep());
				preparedStatement.setInt(6, dto.getbIndent());
			}
			
		});
	}
	
	public void upHit(String strId){

		String query = "update board set bHit = bHit + 1 where bId = ?";
		jdbcTemplate.update(query, new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setInt(1, Integer.parseInt(strId));
			}
			
		});
	}
	
	public void replyShape(int bGroup, int bStep){
		String query = "update board set bStep = bStep + 1 where bGroup = ? and bStep > ?";
		
		jdbcTemplate.update(query, new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				// TODO Auto-generated method stub
				preparedStatement.setInt(1, bGroup);
				preparedStatement.setInt(2, bStep);
			}
			
		});
	}
}
