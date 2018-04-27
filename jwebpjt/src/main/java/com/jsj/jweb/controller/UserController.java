package com.jsj.jweb.controller;

import java.security.Principal;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.Connection;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.api.plus.Person;
import org.springframework.social.google.api.plus.PlusOperations;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jsj.jweb.command.JCommand;
import com.jsj.jweb.command.UserDeleteCommand;
import com.jsj.jweb.command.UserInfoCommand;
import com.jsj.jweb.command.UserSignUpCommand;
import com.jsj.jweb.command.UserUpdateCommand;
import com.jsj.jweb.dto.UserDto;
import com.jsj.jweb.util.CheckEMail;
import com.jsj.jweb.util.LogInService;
import com.jsj.jweb.util.MyMailService;

@Controller
public class UserController {
	private final static String EMAIL_RECEIVE_CURRENT = "e1";
	private final static String EMAIL_AUTH_CODE = "e2";
	private final static String EMAIL_AUTH_COUNT = "e3";
	private final static String EMAIL_AUTH_EXPIRE_TIME = "e4";
	private final static String EMAIL_CONFIRMED = "e5";
	private final static String EMAIL_IS_SOCIALED = "e6";
	
	
	private JCommand command;
	
	@Autowired
	private GoogleConnectionFactory googleConnectionFactory;
	@Autowired
	private OAuth2Parameters googleOAuth2Parameters;
	
	@Autowired
	private MyMailService mailService;

	
	//회원가입 뷰
	@RequestMapping(value = "/user/sign_up/sign_up_view", method = RequestMethod.GET)
	public String signUpView(Model model) {
		System.out.println("sign up view");
		
		return "/user/sign_up_view";
	}

	// 회원가입 이메일 체크
	@RequestMapping(value = "/user/sign_up/check_mail", method = RequestMethod.POST)
	public @ResponseBody String checkMail(HttpSession session, @RequestBody String eMail) {
		System.out.println("checking for " + eMail);
		
		String result = "unUsable";
		
		eMail = eMail.replaceAll("\"", "");
		System.out.println(eMail);
		
		CheckEMail checkMail = new CheckEMail(eMail);
		checkMail.check();
		
		session.removeAttribute(EMAIL_RECEIVE_CURRENT);
		session.removeAttribute(EMAIL_IS_SOCIALED);
		
		if(checkMail.isUsable()) {
			result = "usable";
			session.setAttribute(EMAIL_RECEIVE_CURRENT, eMail);
		}else if(checkMail.isSocial()) {
			result = "socialId";
			session.setAttribute(EMAIL_RECEIVE_CURRENT, eMail);
			session.setAttribute(EMAIL_IS_SOCIALED, "socialed");
		}
		
		return result;
	}

	// 회원가입 이메일 인증
	@RequestMapping(value = "/user/sign_up/send_auth_mail", method = RequestMethod.POST)
	public @ResponseBody boolean sendMailAuth(HttpSession session) {
		int ran = new Random().nextInt(89999) + 10000;
		String authCode = String.valueOf(ran);
		System.out.println("authCode : " + authCode);
		
		String eMail = (String) session.getAttribute(EMAIL_RECEIVE_CURRENT);

		
		String subject = "회원가입 인증 코드 발급 안내 입니다.";
		StringBuilder sb = new StringBuilder();
		sb.append("인증 코드는 " + authCode + " 입니다.");
		
		if(mailService.send(subject, sb.toString(), "jujanyu2@gmail.com", eMail, null)) {
			session.setAttribute(EMAIL_AUTH_CODE, authCode);
			session.setAttribute(EMAIL_AUTH_COUNT, 5);
			session.setAttribute(EMAIL_AUTH_EXPIRE_TIME, System.currentTimeMillis() + 180000);
			return true;
		}
		
		return false;
	}
	
	@RequestMapping(value = "/user/sign_up/confirm_auth_code", method = RequestMethod.POST)
	public @ResponseBody int confirmAuthCode(HttpSession session, @RequestBody String enteredCode) {
		System.out.println("enteredCode : " + enteredCode);
		
		int result = -3;
		
		enteredCode = enteredCode.replaceAll("\"", "");
		
		String authCode = (String) session.getAttribute(EMAIL_AUTH_CODE);
		if (authCode != null) {
			int count = (Integer) session.getAttribute(EMAIL_AUTH_COUNT);
			long expireTime = (Long) session.getAttribute(EMAIL_AUTH_EXPIRE_TIME);

			if (expireTime > System.currentTimeMillis()) {
				if (count-- > 0) {
					if (authCode.equals(enteredCode)) {
						session.setAttribute(EMAIL_CONFIRMED, session.getAttribute(EMAIL_RECEIVE_CURRENT));
						
						sessionRemoveEMailAuth(session);
						return 0;
					}

					if (count > 0) {
						result = count;
						session.setAttribute(EMAIL_AUTH_COUNT, count);
					} else {
						result = -2;
						sessionRemoveEMailAuth(session);
					}
				}
			} else {
				result = -1;
				sessionRemoveEMailAuth(session);
			}
		}
	
		return result;
	}
	private void sessionRemoveEMailAuth(HttpSession session) {
		session.removeAttribute(EMAIL_AUTH_COUNT);
		session.removeAttribute(EMAIL_AUTH_EXPIRE_TIME);
		session.removeAttribute(EMAIL_AUTH_CODE);
		session.removeAttribute(EMAIL_RECEIVE_CURRENT);
	}
	
	// 회원 가입
	@RequestMapping(value = "/user/sign_up/sign_up", method = RequestMethod.POST)
	public @ResponseBody boolean signUp(HttpSession session, Model model, @RequestBody UserDto dto) {
		System.out.println("signUp : " + dto.geteMail() + ", " + dto.getPw() + ", " + dto.getName() + ", " + dto.getNickname() + ", " + dto.getAuth());
		
		String eMail = (String) session.getAttribute(EMAIL_CONFIRMED);
		String socialed = (String) session.getAttribute(EMAIL_IS_SOCIALED);
		
		System.out.println("confirmed eMail : " + eMail);
		
		if(dto.geteMail() == null || eMail == null || !dto.geteMail().equals(eMail)) return false;
		
		dto.seteMail(eMail);
		
		if(!dto.verify()) return false;
		
		dto.setAuth("ROLE_USER");
		dto.setSocial(false);
		
		model.addAttribute("dto", dto);
		
		command = socialed != null ? new UserUpdateCommand() : new UserSignUpCommand();
		command.execute(model);

		session.removeAttribute(EMAIL_CONFIRMED);
		session.removeAttribute(EMAIL_IS_SOCIALED);
		
		return true;
	}
	
	// 로그인 뷰로.. (spring security)
	@RequestMapping(value = "/user/log_in/to_log_in_view", method = RequestMethod.GET)
	public @ResponseBody String toLogInView() {
		System.out.println("need login!");
		return "needLogIn";
	}
	
	// 로그인 뷰
	@RequestMapping(value = "/user/log_in/log_in_view", method = RequestMethod.GET)
	public String logInView(Model model) {
		System.out.println("login view");
		return "/user/log_in_view";
	}
	
	// 로그인
	@RequestMapping(value = "/user/log_in/log_in", method = RequestMethod.GET)
	public @ResponseBody boolean logIn(@RequestParam(value = "error", required = false) String error) {
		System.out.println("Log In : " + error);
		
		if(error != null) return false;
		
		return true;
	}
	
	// 구글 로그인 뷰
	@RequestMapping(value = "/user/log_in/google_log_in_view", method = { RequestMethod.GET, RequestMethod.POST })
	public String googleLogInView(Model model, HttpSession session) throws Exception {

		/* 구글code 발행 */
		OAuth2Operations oauthOperations = googleConnectionFactory.getOAuthOperations();
		String url = oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, googleOAuth2Parameters);

		System.out.println("구글:" + url);

		model.addAttribute("google_url", url);
		/* 생성한 인증 URL을 View로 전달 */
		return "/user/google_login";
	}
	

	// 구글 Callback호출 메소드
	@RequestMapping(value = "/user/log_in/oauth2callback", method = { RequestMethod.GET, RequestMethod.POST })
	public String googleCallback(Model model, HttpServletRequest request, HttpSession session) throws Exception {
		System.out.println("googleCallback");
		
		String code = request.getParameter("code");
		
		System.out.println("code is : " + code);
		
		OAuth2Operations oAuthOperations = googleConnectionFactory.getOAuthOperations();
		AccessGrant accessGrant = null;
		try {
			accessGrant = oAuthOperations.exchangeForAccess(code, googleOAuth2Parameters.getRedirectUri(), null);
		}catch(Exception e) {
			return "/user/google_log_in_failed";
		}
		String accessToken = accessGrant.getAccessToken();
		Long expireTime = accessGrant.getExpireTime();
		
		System.out.println(accessToken + ", " + expireTime);
		
		if(expireTime != null && expireTime < System.currentTimeMillis()) {
			accessToken = accessGrant.getRefreshToken();
			System.out.println("accessToken is expired. refresh token = " + accessToken);
		}
		
		Connection<Google> connection = googleConnectionFactory.createConnection(accessGrant);
		Google google = connection == null ? new GoogleTemplate(accessToken) : connection.getApi();
		
		PlusOperations plusOperations = google.plusOperations();
		Person profile = plusOperations.getGoogleProfile();
		
		UserDto dto = new UserDto();
		dto.seteMail(profile.getAccountEmail());
		dto.setName(profile.getDisplayName());
		dto.setSocial(true);
		dto.setAuth("ROLE_USER");
		
		CheckEMail ck = new CheckEMail(dto.geteMail());
		ck.check();
		if(ck.isUsable()) {
			model.addAttribute("dto", dto);
			
			command = new UserSignUpCommand();
			command.execute(model);
		}
		
		UserDetailsService userDetailsService = new LogInService(LogInService.SOCIAL_LOGIN);
		UserDetails ckUserDetails = userDetailsService.loadUserByUsername(dto.geteMail());
		
		// 임의 spring security session 생성
		Authentication authentication = new UsernamePasswordAuthenticationToken(ckUserDetails, dto.geteMail(), ckUserDetails.getAuthorities());
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authentication);
		session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
		//session.setAttribute("socialLoggedIn", );
		
		System.out.println("email : " + dto.geteMail() + ", " + dto.getName());
		
		return "/user/google_login_success";
	}
	

	// 회원정보 조회
	@RequestMapping(value = "/user/profile/view", method = RequestMethod.GET)
	public String userProfileView(Model model, Principal principal) throws Exception {
		System.out.println("profile view");
		
		if(principal == null){
			return "error";
		}
		
		model.addAttribute("loggedIn", principal.getName());
		model.addAttribute("need", "all");

		command = new UserInfoCommand();
		command.execute(model);
		
		return "/user/profile_view";
	}
	
	// 회원정보 변경
	@RequestMapping(value = "/user/profile/update_profile", method = RequestMethod.POST)
	public @ResponseBody boolean userUpdateProfile(Model model, Principal principal, @RequestBody UserDto dto) throws Exception {
		System.out.println("update profile");
		
		if(principal == null || dto == null || !principal.getName().equals(dto.geteMail())){
			System.out.println(principal.getName() + ", " + dto.geteMail());
			return false;
		}
		
		model.addAttribute("dto", dto);

		command = new UserUpdateCommand();
		command.execute(model);
		
		return true;
	}
	
	// 회원 탈퇴
	@RequestMapping(value = "/user/profile/delete_account", method = RequestMethod.POST)
	public @ResponseBody boolean userDeleteAccount(Model model, Principal principal, @RequestBody String eMail) throws Exception {
		System.out.println("delete account" + principal.getName());
		
		if(principal == null || eMail == null || principal.getName().equals(eMail)) {
			return false;
		}
		
		model.addAttribute("eMail", principal.getName());
		
		command = new UserDeleteCommand();
		command.execute(model);
		
		return true;
	}
}
