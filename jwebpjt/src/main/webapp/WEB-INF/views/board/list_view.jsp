<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
.boardTable {
	background-color: white;
	width: 700px;
	margin: auto;
	margin-top: 150px;
	border: 2px solid black;
}

.disp-none{
	display:none;
}
</style>
<table class="boardTable table table-hover">
	<thead>
		<tr>
			<th class="table-bordered" width="10%">글번호</th>
			<th width="10%">작성자</th>
			<th width="43%">| 제목</th>
			<th width="25%">| 작성일</th>
			<th width="12%">| 조회수</th>
		</tr>
	</thead>
	<tbody id="boardBody">
	</tbody>
	<tfoot id="boardFooter">
	</tfoot>
</table>
<div class="boardBtnDiv" style="width:700px;">
	<button type="button" class="btn btn-default" onclick="boardNewPost()">New Post</button>&nbsp;
</div>