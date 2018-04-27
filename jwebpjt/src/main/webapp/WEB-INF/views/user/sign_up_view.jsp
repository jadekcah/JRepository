<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<style>
.modal-header, h4, .close {
    background-color: #28A0FF;
    color:white !important;
    text-align: center;
    font-size: 30px;
}

.btn-sign-up{
	background-color: #28A0FF;
}

.btn-sign-up:hover{
	background-color: #0078FF;
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
				<span class="glyphicon glyphicon-user"></span> Sign Up
			</h4>
		</div>
		<div class="modal-body" style="padding: 40px 50px;">
			<form class="form-horizontal" id="signUpForm" role="form">
				<div class="form-group">
					<label for="usrId"><span class="glyphicon glyphicon-unchecked"></span> 사용자 아이디 *</label><br>
					<div class="col-sm-9">
						<input type="text" class="form-control" id="usrId" name="eMail" placeholder="이메일을 입력해 주세요">
						<span class="glyphicon glyphicon-ok form-control-feedback sr-only"></span>
					</div>
					<div class="col-sm-3">
						<button type="button" class="btn btn-default btn-block" id="usrIdChkBtn" onclick="mailCheck($('#usrId'))">사용하기 <i class="glyphicon glyphicon-ok"></i></button>
					</div>
					<div id="usrIdAdd" style="display:none">
						<div class="col-sm-6">
							<input type="text" class="form-control" id="usrIdAuthCode" placeholder="인증번호를 입력해 주세요">
						</div>
						<div class="col-sm-3">
							<button type="button" class="btn btn-default btn-block" onclick="mailConfirm($('#usrIdAuthCode'))">확인 <i class="glyphicon glyphicon-ok"></i></button>
						</div>
					</div>
				</div><br>
				<div class="form-group">
					<label for="password"><span class="glyphicon glyphicon-unchecked"></span> 비밀번호 *</label><br>
					<div class="col-sm-12">
						<input type="password" class="form-control" id="password" name="pw" placeholder="비밀번호를 입력해 주세요">
						<span class="glyphicon glyphicon-ok form-control-feedback sr-only"></span>
					</div>
					<div id="passwordRetype" class="col-sm-12" style="display:none">
						<input type="password" class="form-control" placeholder="비밀번호를 다시 입력해 주세요">
					</div>
					<div id="passwordAdd" class="col-sm-12"><span class="help-block"></span></div>
				</div><br>
				<div class="form-group">
					<label for="name"><span class="glyphicon glyphicon-unchecked"></span> 이름</label><br>
					<div class="col-sm-12">
						<input type="text" class="form-control" id="name" name="name" placeholder="이름을 입력해 주세요">
						<span class="glyphicon glyphicon-ok form-control-feedback sr-only"></span>
					</div>
					<div id="nameAdd"></div>
				</div><br>
				<div class="form-group">
					<label for="nickname"><span class="glyphicon glyphicon-unchecked"></span> 닉네임</label><br>
					<div class="col-sm-12">
						<input type="text" class="form-control" id="nickname" name="nickname" placeholder="닉네임을 입력해 주세요">
						<span class="glyphicon glyphicon-ok form-control-feedback sr-only"></span>
					</div>
					<div id="nicknameAdd"></div>
				</div>
				<p style="text-align:right;">* 필수 입력란</p>
				<br>
				<button type="button" class="btn btn-info btn-sign-up btn-block" id="signUpSubmitBtn">
					<span class="glyphicon glyphicon-user"></span> Sign Up !
				</button>
			</form>
		</div>
	</div>
</div>