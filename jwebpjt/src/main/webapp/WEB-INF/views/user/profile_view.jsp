<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<style>
.modal-header, h4, .close {
    background-color: #003A9D;
    color:white !important;
    text-align: center;
    font-size: 30px;
}

.btn-update{
	color:#FFFFFF !important;
	background-color: #003A9D;
	border-color: #003A9D;
}

.btn-update:hover{
	background-color: #053585;
}

.btn-update:active{
	background-color: #0F3067;
}

.btn-update:visited{
	background-color: #003A9D;
}

.modal-body {
    background-color: #FFFFFF;
}

label{
	margin-bottom:10px;
	margin-left:20px;
}
</style>
<div class="modal-dialog">
	<!-- Modal content-->
	<div class="modal-content">
		<div class="modal-header" style="padding: 35px 50px;">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h4>
				<span class="glyphicon glyphicon-user"></span> Profile
			</h4>
		</div>
		<div class="modal-body" style="padding: 40px 50px;">
			<form class="form-horizontal" id="profileForm" role="form">
				<div class="form-group">
					<label for="usrId"><span class="glyphicon glyphicon-unchecked"></span> 아이디</label><br>
					<div class="col-sm-12" style="cursor:default;">${need.eMail}
					</div>
					<input type="hidden" name="eMail" id="usrId" value="${need.eMail}">
				</div><br>
				<div class="form-group">
					<label for="name"><span class="glyphicon glyphicon-unchecked"></span> 이름</label><br>
					<div class="col-sm-12">
						<input type="text" class="form-control" id="name" name="name" placeholder="이름을 입력해 주세요" value="${need.name}">
						<span class="glyphicon glyphicon-ok form-control-feedback sr-only"></span>
					</div>
					<div id="nameAdd"></div>
				</div><br>
				<div class="form-group">
					<label for="nickname"><span class="glyphicon glyphicon-unchecked"></span> 닉네임</label><br>
					<div class="col-sm-12">
						<input type="text" class="form-control" id="nickname" name="nickname" placeholder="닉네임을 입력해 주세요" value="${need.nickname}">
						<span class="glyphicon glyphicon-ok form-control-feedback sr-only"></span>
					</div>
					<div id="nicknameAdd"></div>
				</div><br>
				<div class="form-group">
					<label for="password"><span class="glyphicon glyphicon-unchecked"></span> 비밀번호 변경</label><br>
					<div class="col-sm-12">
						<input type="password" class="form-control" id="password" name="pw" placeholder="새 비밀번호를 입력해 주세요">
						<span class="glyphicon glyphicon-ok form-control-feedback sr-only"></span>
					</div>
					<div id="passwordRetype" class="col-sm-12" style="display:none">
						<input type="password" class="form-control" placeholder="비밀번호를 다시 입력해 주세요">
					</div>
					<div id="passwordAdd" class="col-sm-12"><span class="help-block"></span></div>
				</div>
				<br>
				<div class="form-group">
					<div class="col-sm-8">
						<button type="button" class="btn btn-update btn-block" id="profileUpdateBtn">
							<span class="glyphicon glyphicon-floppy-disk"></span> Update
						</button>
					</div>
					<div class="col-sm-4">
						<button type="button" class="btn btn-danger btn-block" id="deleteAccountBtn">
							<span class="glyphicon glyphicon-user"></span> Delete Account
						</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>