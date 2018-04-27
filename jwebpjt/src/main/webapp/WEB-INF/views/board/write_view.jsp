<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
.writeTable {
	background-color: white;
	width: 600px;
	margin: auto;
	margin-top: 150px;
	border: 2px solid black;
}
</style>
<form name="writeForm" id="writeForm">
	<table class="writeTable table table-bordered">
		<tr>
			<td>작성자</td>
			<td>${need}</td>
		</tr>
		<tr>
			<td>제목</td>
			<td><input type="text" class="form-control" name="bTitle" size="50"></td>
		</tr>
		<tr>
			<td>내용</td>
			<td><textarea class="form-control" name="bContent" cols="50" rows="10"></textarea></td>
		<tr>
			<td colspan="2"></td>
		</tr>
	</table>
</form>
<div class="boardBtnDiv">
	<button type="button" class="btn btn-success" onclick="newPostSubmit()">Submit!</button> &nbsp;
	<button type="button" class="btn btn-default" onclick="showBoard(currentPage, false)">Cancel</button>
</div>
			