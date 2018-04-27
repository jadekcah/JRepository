/**
 * my script / need jquery.js
 */
//browser
var agentBrowser = "browser";

var agent = navigator.userAgent.toLowerCase();
if ((navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1)
		|| (agent.indexOf("msie") != -1)) agentBrowser = "msie";
else if (agent.indexOf("chrome") != -1) agentBrowser = "chrome";
else if (agent.indexOf("firefox") != -1) agentBrowser = "firefox";
else if (agent.indexOf("safari") != -1) agentBrowser = "safari";

//alert(agentBrowser);

//date functions
function datePad(numb) {
	return (numb < 10 ? '0' : '') + numb;
}

function boardDateCalc(date) {
	//return ;
	console.log("date : " + date + ", type " + typeof(date));
	//if(agentBrowser != "chrome") date = date.replace(" ", "T");
	
	var bDate = new Date(date);
	var bDate_ = datePad(bDate.getFullYear()) + "."
			+ datePad(bDate.getMonth() + 1) + "."
			+ datePad(bDate.getDate());
	var today = new Date();
	var diffMonth = (today.getFullYear() - bDate.getFullYear()) * 12
			+ today.getMonth() - bDate.getMonth()
			+ (today.getDate() - bDate.getDate() > 0 ? 0 : -1);
	
	var diff = diffMonth / 12;
	if (diff > 0) return bDate_ + " (" + Math.floor(diff) + "년 전)";
	diff = diffMonth;
	if(diff > 0) return bDate_ + " (" + diff + "달 전)";
	else {
		var diffMinutes = (today.getTime() - bDate.getTime()) / 1000 / 60;
		
		diff = Math.floor(diffMinutes / 60 / 24);
		if (diff > 1) return bDate_ + " (" + diff + "일 전)";
		if (diff == 1) return "어제";
		
		diff = Math.floor(diffMinutes / 60);
		if (diff > 0) return diff + "시간 전";
		
		diff = diffMinutes;
		if (diff > 10) return (Math.floor(diff / 10) * 10) + "분 전";
		if (diff < 10) return "조금 전";
	}
	
	//alert(date + " => bDate:" + bDate + ", bDate_:" + bDate_ + ", " + diffMonth + ", " + diffMinutes);
	
	return null;
}

//json functions
function formToJson(formId){
	var resultJson = {};
	var formChildren = $("#" + formId).find("*");
	formChildren = formChildren.filter("input").add(formChildren.filter("textarea")).add(formChildren.filter("select"));
	
	formChildren.each(function(){
		$(this).attr("name") != null ? resultJson[$(this).attr("name")] = $(this).val() : console.log("name undefined prop;");
		//alert($(this).attr("name") + ", " + resultJson[$(this).attr("name")]);
	});
	
	return JSON.stringify(resultJson);
}

//board listing
function getListElements(func){
	$.ajax({
		url: "/jweb/board/list_elements",
		type: "POST",
		data: null,
		success: function(rslt){
			goToLogIn(rslt, function(){
				boardList = rslt;
				func();
			});
		},
		error:function(responseTxt, statusTxt, xhr){
			alert("Error: " + responseTxt + ", " + xhr.status + ": " + xhr.statusText);
		}
	});
}

function getListView(pageNum){
	$.ajax({
		url: "/jweb/board/list",
		type: "GET",
		data: null,	//나중에 페이지 번호
		success: function(rslt){
			goToLogIn(rslt, function(){
				$("#boardContainer").html(rslt).find("#boardBody").html();
				//setList(pageNum);
				setPagination();
				setList(pageNum);
				showContainer("boardContainer");
			});
		},
		error:function(responseTxt, statusTxt, xhr){
			alert("Error: " + responseTxt + ", " + xhr.status + ": " + xhr.statusText);
		}
	});
}

function setList(pageNum){
	pageNum = pageNum < 1 ? 1 : pageNum;
	var count = 10;
	var startCount = (pageNum - 1) * 10;
	var boardBody = $("#boardBody");

	currentPage = pageNum;
	
	boardBody.html("");		
	$.each(boardList, function(index, item){
		if(startCount > 0) {
			if(item.bIndent == 0) startCount--;
			return true;
		}
		if(item.bIndent == 0) {
			if(count == 0) return false;
			count--;
		} else if(boardBody.html() == "") return true;
		
		var html = '';
		
		if(item.bIndent > 0){
			boardShowRepliesBtn(item.bGroup);
			html += '<tr class="disp-none" name="group_' + item.bGroup + '">';
		}else{
			html += '<tr>';
		}
		html += '<td class="table-bordered">' + item.bId + '<span class="disp-none glyphicon glyphicon-chevron-right pull-right" id="toggleRepliesBtn_' + item.bId + '"onclick="boardToggleReplies(' + item.bGroup + ')"></span></td>';
		html += '<td>' + item.nickname + '</td>';
		html += '<td>';
		for(var i = 1; i <= item.bIndent; i++){
			if(i == item.bIndent) html += '└';
			else html += '&nbsp;&nbsp;&nbsp;';
		}
		html += '&nbsp;<a href="javascript:;" onclick="boardContentView(' + item.bId + ')">' + item.bTitle + '</a>';
		html += '</td>';
		html += '<td>' + boardDateCalc(item.bDate) + '</td>';
		html += '<td>' + item.bHit + '</td>';
		html += '</tr>';
		
		boardBody.html($("#boardBody").html() + html);
	});
	
	if(boardBody.html() == "" && pageNum > 1) {
		setList(--pageNum);
	} else pagination(pageNum);
}

function setPagination(){
	var html = '';
	var boardFooter = $("#boardFooter");
	var maxPage = 0;
	
	$.each(boardList, function(index, item){
		if(item.bIndent == 0) maxPage++;
	});
	
	maxPage--;
	maxPage = ((maxPage - (maxPage % 10)) / 10) + 1;

	html += '<tr>';
	html += '<td colspan="5" style="text-align: center;">';
	html += '<nav>';
	html += '<ul class="pagination pagination-sm">';
	html += '<li class="page-item"><a class="page-link" href="javascript:;" onclick="setList(currentPage - 1)">◁</a></li>';
	for(var i = 1; i <= maxPage; i++){
		html += '<li class="page-item" name="page_' + i + '"><a class="page-link" href="javascript:;" onclick="setList(' + i + ')">' + i + '</a></li>';
	}
	html += '<li class="page-item"><a class="page-link" href="javascript:;" onclick="setList(currentPage + 1)">▷</a></li>';
	html += '</ul>';
	html += '</nav>';
	html += '</td>';
	html += '</tr>';
	
	boardFooter.html(html);
}

function pagination(pageNum){
	pageNum = pageNum != null ? pageNum : 1;
	var boardFooter = $("#boardFooter");
	
	boardFooter.find("li").removeClass("active").filter("li[name='page_" + pageNum + "']").addClass("active");
}

//signUp
function mailCheck(mailInput){
	var data = JSON.stringify(mailInput.val());
	$.ajax({
		url: "/jweb/user/sign_up/check_mail",
		type: "POST",
		contentType : "application/json",
		data: data,
		success: function(rslt){
			if(rslt != "unUsable"){
				var text;
				if(rslt == "usable") text = "사용 가능한 이메일 입니다.\n이 이메일을 사용하시겠습니까?\n\n(이메일로 인증번호를 보냅니다.)";
				else if(rslt == "socialId") text = "소셜 로그인으로 사용된 적이 있는 이메일 입니다.\n이 이메일을 사용하시겠습니까? \n(소셜 로그인 시에도 이 아이디 사용)\n\n(이메일로 인증번호를 보냅니다.)"
				if(confirm(text)){
					if(mailSend()){
						mailInput.parent().next().children().filter("button").attr("disabled", "true");
						mailInput.attr("readonly", "true");
						//mailInput.parent().addClass("has-success");
						
						$("#usrIdAdd").slideDown(500);
					}else{
						alert("존재하지 않는 이메일입니다.\n다른 이메일을 사용해 주세요.");
					}
				}
			} else alert("사용 불가능한 이메일 입니다.\n다른 이메일을 사용해 주세요.");
		},
		error:function(responseTxt, statusTxt, xhr){
			alert("Error: " + responseTxt + ", " + xhr.status + ": " + xhr.statusText);
		}
	});
}

function mailSend(){
	return $.ajax({
		url: "/jweb/user/sign_up/send_auth_mail",
		type: "POST",
		data: null,
		success: function(rslt){
			return rslt;
		},
		error:function(responseTxt, statusTxt, xhr){
			alert("Error: " + responseTxt + ", " + xhr.status + ": " + xhr.statusText);
		}
	});
}

function mailConfirm(codeInput){
	var data = JSON.stringify(codeInput.val());
	$.ajax({
		url: "/jweb/user/sign_up/confirm_auth_code",
		type: "POST",
		contentType : "application/json",
		data: data,
		success: function(rslt){
			if(rslt == 0){
				codeInput.parent().parent().parent().addClass("has-success").addClass("has-feedback");
				$("#usrId").siblings().filter("span").removeClass("sr-only");
				
				codeInput.parent().parent().slideUp(500);
			} else if(rslt > 0) alert("틀린 인증번호 입니다.\n정확한 번호를 입력해 주세요.\n(" + rslt + "회 남았습니다.)");
			else if(rslt == -1) alert("인증 시간을 초과하였습니다.");
			else if(rslt == -2) alert("인증 횟수를 초과하였습니다.");
			else if(rslt == -3) alert("인증에 실패하였습니다.")
		},
		error:function(responseTxt, statusTxt, xhr){
			alert("Error: " + responseTxt + ", " + xhr.status + ": " + xhr.statusText);
		}
	});
}

function checkPass(mthis){
	$(mthis).val($(mthis).val().replace(/ /g, ""));
	
	$(mthis).parent().parent().removeClass("has-success").removeClass("has-feedback");
	$(mthis).siblings().filter("span").addClass("sr-only");
	if(isPassValid($(mthis).val())){
		$(mthis).parent().parent().removeClass("has-error");
		$("#passwordRetype").slideDown(500);
		$("#passwordAdd").children().html("다시 한번 입력해 주세요.");
	} else {
		$(mthis).parent().parent().addClass("has-error")
		$("#passwordRetype").slideUp(500);
		$("#passwordAdd").children().html("영문자, 숫자, 특수문자를 포함하여 8자 이상 입력해 주세요!");
	}
}

function checkPassRetype(mthis){
	if($(mthis).val() == $("#password").val()){
		$(mthis).parent().slideUp(500);
		$(mthis).parent().parent().addClass("has-success").addClass("has-feedback");
		$("#password").siblings().filter("span").removeClass("sr-only");
		$("#passwordAdd").children().html("");
	}
}

function checkInput(mthis){
	$(mthis).val($(mthis).val().replace(/ /g, ""));
	
	if($(mthis).attr("name") != "email"){
		if($(mthis).val() != ""){
			$(mthis).parent().parent().addClass("has-success").addClass("has-feedback");
			$(mthis).siblings().filter("span").removeClass("sr-only");
		} else {
			$(mthis).parent().parent().removeClass("has-success").removeClass("has-feedback");
			$(mthis).siblings().filter("span").addClass("sr-only");
		}
	}
}

function isPassValid(pass){	// 정규식을 이용한 비밀번호 검증
	var passwd = pass;
	var reg = /^.*(?=.{8,20})(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W).*$/;
	
	console.log(passwd.search(reg));
	
	if(passwd.search(reg) < 0) return false;
	
	return true;
}

function signUpSubmit(){
	if(!$("#usrId").parent().parent().hasClass("has-success")){
		if($("#usrId").val() == "")
			alert("이메일을 입력해 주세요!");
		else
			alert("이메일을 검증해 주세요!");
		
		$("#usrId").focus();
		return false;
	}
	if(!$("#password").parent().parent().hasClass("has-success")){
		if($("#password").val() == ""){
			alert("비밀번호를 입력해 주세요!");
			$("#password").focus();
		}else if($("#password").val() != $("#passwordRetype").val()){
			alert("입력된 비밀번호가 서로 다릅니다.\n다시 입력해 주세요!");
			$("#password").parent().parent().addClass("has-error");
			$("#passwordRetype").focus();
		}
		
		return false;
	}
	var formData = formToJson("signUpForm");
	//alert(formData);
	$.ajax({
		url: "/jweb/user/sign_up/sign_up",
		type: "POST",
		contentType : "application/json",
		data: formData,
		success: function(rslt){
			if(rslt){
				alert("회원가입 완료!");
				$("#myModal").html("");
				$("#myModal").modal("hide");
			}else{
				alert("Error occurred!");
			}
		},
		error:function(responseTxt, statusTxt, xhr){
			alert("Error: " + responseTxt + ", " + xhr.status + ": " + xhr.statusText);
		}
	});
}

//logIn
function logInSubmit(){
	if($("#usrId").val() == ""){
		alert("아이디를 입력해 주세요!");
		
		$("#usrId").focus();
		return false;
	}
	if($("#password").val() == ""){
		alert("비밀번호를 입력해 주세요!");
		$("#password").focus();
		
		return false;
	}
	var formData = $("#logInForm").serialize();//formToJson("logInForm");
	//alert(formData);
	$.ajax({
		url: "/jweb/j_spring_security_check",
		type: "POST",
		data: formData,
		success: function(rslt){
			if(rslt){
				//alert("로그인 성공!");
				$("#myModal").html("");
				$("#myModal").modal("hide");
				window.location.href = "/jweb/";
			}else{
				alert("아이디와 비밀번호를 확인해 주세요!");
			}
		},
		error:function(responseTxt, statusTxt, xhr){
			alert("Error: " + responseTxt + ", " + xhr.status + ": " + xhr.statusText);
		}
	});
}

function googleLogIn(){
	logInPopUp = window.open("/jweb/user/log_in/google_log_in_view", "google_login", "width=600, height=600, scrollbars=0, toolbar=0, menubar=no");
	$("#myModal").html("");
	$("#myModal").modal("hide");
}

function goToLogIn(rslt, func){
	if(rslt != null && rslt == "needLogIn"){
		showLogInModal();
	}else{
		func();
	}
}

//logOut
function logOut(){
	if(confirm("로그아웃 하시겠습니까?")){
		window.location.href = "/jweb/user/log_out";
	}
}

//profileUpdate
function profileUpdate(){
	if(confirm("입력한 정보로 변경합니다.")){
		var formData = formToJson("profileForm");
		//alert(formData);
		$.ajax({
			url: "/jweb/user/profile/update_profile",
			type: "POST",
			contentType : "application/json",
			data: formData,
			success: function(rslt){
				if(rslt){
					alert("변경 완료!");
					$("#myModal").html("");
					$("#myModal").modal("hide");
				}else{
					alert("Error occurred!");
				}
			},
			error:function(responseTxt, statusTxt, xhr){
				alert("Error: " + responseTxt + ", " + xhr.status + ": " + xhr.statusText);
			}
		});
	}
}

//deleteAccount
function deleteAccount(){
	var eMail = $("#profileForm").find("[name = 'eMail']").val();
	if(confirm("계정을 삭제 하시겠습니까? \n(이 선택은 되돌릴 수 없습니다.)")){
		$.ajax({
			url: "/jweb/user/profile/delete_account",
			type: "POST",
			contentType : "application/json",
			data: {"eMail" : eMail},
			success: function(rslt){
				if(rslt){
					alert("계정이 성공적으로 삭제되었습니다.");
					$("#myModal").html("");
					$("#myModal").modal("hide");
					window.location.href = "/jweb/user/log_out";
				}else{
					alert("Error occurred!");
				}
			},
			error:function(responseTxt, statusTxt, xhr){
				alert("Error: " + responseTxt + ", " + xhr.status + ": " + xhr.statusText);
			}
		});
	}
}

//board	
function boardNewPost(){
	$.ajax({
		url: "/jweb/board/write_view",
		type: "GET",
		data: null,
		success: function(rslt){
			goToLogIn(rslt, function(){
				$("#writeViewer").html(rslt);
				showContainer("writeViewer");
			});
		},
		error:function(responseTxt, statusTxt, xhr){
			alert("Error: " + responseTxt + ", " + xhr.status + ": " + xhr.statusText);
		}
	});
}

function newPostSubmit(){
	var formData = formToJson("writeForm");
	//alert(formData);
	$.ajax({
		url: "/jweb/board/write",
		type: "POST",
		contentType : "application/json",
		data: formData,
		success: function(rslt){
			goToLogIn(rslt, function(){
				if(rslt) showBoard(currentPage);
				else alert("error");
			});
		},
		error:function(responseTxt, statusTxt, xhr){
			alert("Error: " + responseTxt + ", " + xhr.status + ": " + xhr.statusText);
		}
	});
}

function boardContentView(bId){
	$.ajax({
		url: "/jweb/board/content_view",
		type: "GET",
		contentType : "application/json",
		data: {"bId" : bId},
		success: function(rslt){
			goToLogIn(rslt, function(){
				$("#contentViewer").html(rslt);
				showContainer("contentViewer");
			});
		},
		error:function(responseTxt, statusTxt, xhr){
			alert("Error: " + responseTxt + ", " + xhr.status + ": " + xhr.statusText);
		}
	});
}

function modifySubmit(){
	if(!confirm("수정 하시겠습니까?")) return;
	var formData = formToJson("contentForm");
	//alert(formData);
	$.ajax({
		url: "/jweb/board/modify",
		type: "POST",
		contentType : "application/json",
		data: formData,
		success: function(rslt){
			goToLogIn(rslt, function(){
				if(rslt){
					showBoard(currentPage);
				} else alert("작성자만 수정할 수 있습니다!");
			});
		},
		error:function(responseTxt, statusTxt, xhr){
			alert("Error: " + responseTxt + ", " + xhr.status + ": " + xhr.statusText);
		}
	});
}

function boardReply(bId){
	$.ajax({
		url: "/jweb/board/reply_view",
		type: "GET",
		contentType : "application/json",
		data: {"bId" : bId},
		success: function(rslt){
			goToLogIn(rslt, function(){
				$("#writeViewer").html(rslt);
				showContainer("writeViewer");
			});
		},
		error:function(responseTxt, statusTxt, xhr){
			alert("Error: " + responseTxt + ", " + xhr.status + ": " + xhr.statusText);
		}
	});
}

function replySubmit(){
	var formData = formToJson("replyForm");
	//alert(formData);
	$.ajax({
		url: "/jweb/board/reply",
		type: "POST",
		contentType : "application/json",
		data: formData,
		success: function(rslt){
			goToLogIn(rslt, function(){
				if(rslt) showBoard(currentPage);
				else alert("error");
			});
		},
		error:function(responseTxt, statusTxt, xhr){
			alert("Error: " + responseTxt + ", " + xhr.status + ": " + xhr.statusText);
		}
	});
}

function boardDelete(bId){
	if(!confirm("삭제 하시겠습니까?")) return;
	$.ajax({
		url: "/jweb/board/delete",
		type: "GET",
		contentType : "application/json",
		data: {"bId" : bId},
		success: function(rslt){
			goToLogIn(rslt, function(){
				if(rslt){
					showBoard(currentPage);
				} else alert("작성자만 삭제할 수 있습니다!");
			});
		},
		error:function(responseTxt, statusTxt, xhr){
			alert("Error: " + responseTxt + ", " + xhr.status + ": " + xhr.statusText);
		}
	});
}

function boardShowRepliesBtn(bId){
	$("#toggleRepliesBtn_" + bId).show();
}

function boardToggleReplies(bGroup){
	var togBtn = $("#toggleRepliesBtn_" + bGroup);
	var replies = $("#boardBody").find("*").filter("tr[name = 'group_" + bGroup + "']");
	//alert(replies);
	
	if(togBtn.hasClass("glyphicon-chevron-right")){
		replies.show();
		togBtn.removeClass("glyphicon-chevron-right").addClass("glyphicon-chevron-down");
	}else{
		replies.hide();
		togBtn.removeClass("glyphicon-chevron-down").addClass("glyphicon-chevron-right");
	}
}