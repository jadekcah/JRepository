<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<style>
.modal-header, h4, .close {
	background-color: #5cb85c;
	color: white !important;
	text-align: center;
	font-size: 30px;
}

.modal-footer {
	background-color: #ffffff;
}
</style>
<div class="modal-dialog">
	<!-- Modal content-->
	<div class="modal-content">
		<div class="modal-header" style="padding: 35px 50px;">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h4>
				<span class="glyphicon glyphicon-lock"></span> Log In
			</h4>
		</div>
		<div class="modal-body" style="padding: 40px 50px;">
			<form role="form" id="logInForm">
				<div class="form-group">
					<label for="usrId"><span class="glyphicon glyphicon-user"></span>
						로그인 </label> <input type="text" class="form-control" id="usrId" name="email"
						placeholder="아이디 (이메일)">
				</div>
				<div class="form-group">
					<input type="password" class="form-control" id="password" name="pw" placeholder="비밀번호">
				</div>
				<!-- <div class="checkbox">
					<label><input type="checkbox" value="" checked>Remember
						me</label>
				</div> -->
				<button type="button" class="btn btn-success btn-block" id="logInSubmitBtn">
					<span class="glyphicon glyphicon-log-in"></span> Log In !
				</button>
			</form>
		</div>
		<div class="modal-footer" style="padding: 35px 50px;">
			<form role="form">
				<button type="button" class="btn btn-danger btn-block" id="googleLogInBtn">
					Google 아이디로 로그인
				</button>
			</form>
		</div>
	</div>
</div>