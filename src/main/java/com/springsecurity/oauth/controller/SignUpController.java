package com.springsecurity.oauth.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.springsecurity.oauth.config.google.GoogleLogin;
import com.springsecurity.oauth.entity.Member;
import com.springsecurity.oauth.service.SignUpOAuthService;
import com.springsecurity.oauth.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class SignUpController {
    // 회원가입 및 로그인 인증 서비스
    @Autowired
    SignUpService signUpService;

    @Autowired
    SignUpOAuthService signUpOAuthService;

    // 비밀번호 암호화 메소드
    @Autowired
    PasswordEncoder passwordEncoder;

    // 로그인 진행 URL
    @PostMapping("/loginform/login")
    public void login(@RequestParam(value = "emailId") String emailId) { // 1. 파라미터로 로그인 할때 작성한 아이디를 받아온다.
        // 2. 서비스 파라미터로 로그인 할때 작성한 아이디를 넘겨준다.
        signUpService.loadUserByUsername(emailId);
        // 로그인 성공 및 실패 후 이동 페이지는 Spring Security가 각 핸들러를 통해 잡고 있기에 여기서 굳이 잡아줄 필요가 없다.
        //return "Main";
    }

    // 회원가입 페이지
    @GetMapping("/joinform")
    public String joinform(Model model) {
        // 0. Entity 대신 DTO를 사용하기 위해 사용할 DTO를 모델로 바인딩 한다.
        model.addAttribute("memberDto", new Member.rqJoinMember());
        return "SignUp/JoinForm";
    }

    // 회원가입 진행 URL
    @PostMapping("/joinform/join")
    public String join(Member.rqJoinMember rqJoinMember, Model model) { // 1. 파라미터로 form에서 넘어온 DTO를 받아온다.
        // 2. 서비스 파라미터로 MemberDTO와 비밀번호 암호화 메소드를 같이 넘겨준다.
        Member.rpJoinMember member = signUpService.joinMember(rqJoinMember, passwordEncoder);
        // 11. 반환된 DTO를 모델로 바인딩 한다.
        model.addAttribute("member", member);
        return "SignUp/Welcome";
    }

    // 구글 로그인

    // 구글 Access_Token 발급 페이지
    @GetMapping("/loginform/google/token")
    public String googleAccessToken(String code, HttpSession httpSession, Model model) {
        System.out.println("code : " + code);
        JsonNode jsonToken = GoogleLogin.getAccessToken(code);
        String accessToken = jsonToken.get("access_token").toString();
        System.out.println("access_token : " + accessToken);
        return "SignUp/GoogleAccessToken";
    }
    @PostMapping("/loginform/google/authentication")
    @ResponseBody
    public String google(Member.rqGoogleMember rqGoogleMember) {
        String ok = "ok";
        return ok;
    }

    @GetMapping("/google/joinform")
    public String googleJoinForm() {
        return "SignUp/GoogleJoinForm";
    }
}
