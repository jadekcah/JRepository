<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="resources/js/myscript.js"></script>
<style>
.myNavbar {
	background-color: #ffffff;
	padding-right: 10px;
	margin-bottom: 0px;
	border-bottom: 1px solid black;
	box-shadow: 0px 1px 10px black;
}

.mainPage {
	background-image: url(resources/img/main.jpg);
	background-repeat: no-repeat;
	background-size: cover;
	background-position: center 50px;
	background-attachment: fixed;
	overflow: auto;
}

.boardBtnDiv {
	width: 600px;
	margin: auto;
	margin-top: 10px;
	padding: 10px;
	border: 1px solid rgba(255, 255, 255, 0.8);
	background-color: #ffffff;
	background-color: rgba(0, 0, 0, 0.5);
	text-align: right;
}

#navLoggedIn{
	color:#888888;
	text-align:center;
   -ms-user-select: none; 
   -moz-user-select: -moz-none;
   -khtml-user-select: none;
   -webkit-user-select: none;
	user-select:none;
	cursor:pointer;
}

#navLoggedIn:hover{
	color:black;
}
</style>
<title>JBoard</title>
</head>
<body class="mainPage">
<div>
	<nav id="topNavbar" class="myNavbar navbar navbar-light navbar-fixed-top">
		<div class="navbar-brand">JBoard</div>
		<button class="navbar-toggle"
			style="background-color: white; border: 1px solid #303030"
			type="button" data-toggle="collapse" data-target="#myNavbar">
			<span class="icon-bar" style="background-color: #303030"></span> <span
				class="icon-bar" style="background-color: #303030"></span> <span
				class="icon-bar" style="background-color: #303030"></span>
		</button>
		<div class="collapse navbar-collapse" id="myNavbar">
			<ul class="nav navbar-nav">
				<li><a href="javascript:;" id="navHomeBtn">Home</a></li>
				<li><a href="javascript:;" id="navBoardBtn">Board</a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<c:choose>
					<c:when test="${empty loggedIn}">
						<li><a href="javascript:;" id="navLogInBtn">
						<i class="glyphicon glyphicon-log-in"></i>&nbsp;Log In</a></li>
						<li><a href="javascript:;" id="navSignUpBtn">
						<i class="glyphicon glyphicon-user"></i>&nbsp;Sign Up</a></li>
					</c:when>
					<c:otherwise>
						<li id="navLoggedIn">Logged in<br>[
						<c:choose>
							<c:when test="${!empty need.name}">
								${need.name}
							</c:when>
							<c:when test="${!empty need.nickname}">
								${need.nickname}
							</c:when>
						</c:choose>
						]</li>
						<li><a href="javascript:;" id="navLogOutBtn">
						<i class="glyphicon glyphicon-log-out"></i>&nbsp;Log Out</a></li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</nav>
	<div id="mainContainer">
		<div id="boardContainer" style="display:none"></div>
		<div id="contentViewer"></div>
		<div id="writeViewer"></div>
		<div id="testViewer"></div>
	</div>
	<!-- <form name="ajaxTestForm" id="ajaxTestForm">
		<input type="text" name="bName" autocomplete="off"><br>
		<input type="text" name="bTitle" autocomplete="off"><br>
		<input type="text" name="bContent" autocomplete="off"><br>
		<button onclick="test_()">button</button>
	</form> -->
</div>
<div class="modal fade" id="myModal" role="dialog">

</div>
<script>
	//static variables
	var boardList;
	var currentPage = 1;
	
	//Container
	function allContainerHide(){
		$("#mainContainer").children().hide(500);
	}
	
	function showContainer(id){
		$("#" + id).siblings().hide(500);
		$("#" + id).show(500);
	}
	
	//for ajax
	//show func
	function showHome() {
		allContainerHide();
	}
	
	function showBoard(pageNum, needNewElements) {
		pageNum = pageNum != null ? pageNum : 1;
		needNewElements = needNewElements != null ? needNewElements : 1;
		if(needNewElements) getListElements(function(){getListView(pageNum);});
		else getListView(pageNum);
	}
	
	function showSignUpModal(){
		$("#myModal").load("/jweb/user/sign_up/sign_up_view", function(responseTxt, statusTxt, xhr){
			if(statusTxt == "success") {
				$(this).modal({backdrop:"static"});
				$("#usrId").bind("keyup", function(){checkInput(this);});
				$("#password").bind("keyup", function(){checkPass(this);});
				$("#passwordRetype").children().bind("keyup", function(){checkPassRetype(this);});
				$("#name").bind("keyup", function(){checkInput(this);});
				$("#nickname").bind("keyup", function(){checkInput(this);});
				$("#signUpSubmitBtn").bind("click", function(){signUpSubmit();});
			}else if(statusTxt == "error") alert("Error: " + xhr.status + ": " + xhr.statusText);
		});
	}

	function showLogInModal(){
		$("#myModal").load("/jweb/user/log_in/log_in_view", function(responseTxt, statusTxt, xhr){
			if(statusTxt == "success") {
				$(this).modal({backdrop:"static"});
				$("#logInSubmitBtn").bind("click", function(){logInSubmit();});
				$("#password").bind("keyup", function(e){if(e.keyCode == 13) logInSubmit();});
				$("#googleLogInBtn").bind("click", function(){googleLogIn();});
			}
			else if(statusTxt == "error") alert("Error: " + xhr.status + ": " + xhr.statusText);
		});
	}
	
	function showUserProfModal(){
		$("#myModal").load("/jweb/user/profile/view", function(responseTxt, statusTxt, xhr){
			if(statusTxt == "success") {
				$(this).modal({backdrop:"static"});
				$("#name").bind("keyup", function(){checkInput(this);});
				$("#nickname").bind("keyup", function(){checkInput(this);});
				$("#password").bind("keyup", function(){checkPass(this);});
				$("#passwordRetype").children().bind("keyup", function(){checkPassRetype(this);});
				$("#profileUpdateBtn").bind("click", function(){profileUpdate();});
				$("#deleteAccountBtn").bind("click", function(){deleteAccount();});
			}
		});
	}

	//binding menu
	$("#navHomeBtn").bind("click", function(){showHome();});
	$("#navBoardBtn").bind("click", function(){showBoard();});
	$("#navLogInBtn").bind("click", function(){showLogInModal();});
	$("#navSignUpBtn").bind("click", function(){showSignUpModal();});
	$("#navLoggedIn").bind("click", function(){showUserProfModal();});
	$("#navLogOutBtn").bind("click", function(){logOut();});
	
	$("#myModal").on("shown.bs.modal", function(){
		$("#usrId").focus();
	});
</script>
</body>
</html>
