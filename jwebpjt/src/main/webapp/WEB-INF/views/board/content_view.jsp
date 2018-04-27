<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
.contentTable {
	background-color: white;
	width: 600px;
	margin: auto;
	margin-top: 150px;
	border: 2px solid black;
}
</style>
<form id="contentForm" name="contentForm">
	<input type="hidden" name="bId" value=${content_view.bId }>
	<table class="contentTable table table-bordered">
		<tr>
			<td>작성자</td>
			<td>${content_view.nickname }</td>
		</tr>
		<tr>
			<td>조회수</td>
			<td>${content_view.bHit }</td>
		</tr>
		<tr>
			<td>제목</td>
			<td><input type="text" name="bTitle" class="form-control" value="${content_view.bTitle }"></td>
		</tr>
		<tr>
			<td>내용</td>
			<td><textarea name="bContent" class="form-control" cols="50" rows="10">${content_view.bContent }</textarea>
			</td>
		<tr>
			<td colspan=2></td>
		</tr>
	</table>
</form>
<div class="boardBtnDiv">
	<button type="button" class="btn btn-info" onclick="modifySubmit()">Modify</button>&nbsp;
	<button type="button" class="btn btn-danger" onclick="boardDelete(${content_view.bId})">Delete</button>&nbsp;
	<button type="button" class="btn btn-default" onclick="boardReply(${content_view.bId})">Reply</button>&nbsp;
	<button type="button" class="btn btn-default" onclick="showBoard(currentPage, false)">Back</button>
</div>