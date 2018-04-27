<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
.replyTable {
	background-color: white;
	width: 600px;
	margin: auto;
	margin-top: 150px;
	border: 2px solid black;
}
</style>
<form id="replyForm" name="replyForm">
	<input type="hidden" name="bId" value=${reply_view.bId }> 
	<input type="hidden" name="bGroup" value=${reply_view.bGroup }> 
	<input type="hidden" name="bStep" value=${reply_view.bStep }> 
	<input type="hidden" name="bIndent" value=${reply_view.bIndent }>
	<table class="replyTable table table-bordered">
		<tr>
			<td>작성자</td>
			<td>${reply_view.nickname }</td>
		</tr>
		<tr>
			<td>제목</td>
			<td><input class="form-control" type="text" name="bTitle"
				value="원글 : ${reply_view.bTitle }" size="50"></td>
		</tr>
		<tr>
			<td>내용</td>
			<td><textarea class="form-control" name="bContent" cols="50" rows="10">원글 : ${reply_view.bContent }</textarea>
			</td>
		<tr>
		</tr>
	</table>
</form>
<div class="boardBtnDiv">
	<button type="button" class="btn btn-success" onclick="replySubmit()">Submit!</button>&nbsp;
	<button type="button" class="btn btn-default" onclick="showBoard(currentPage, false)">Back</button>
</div>