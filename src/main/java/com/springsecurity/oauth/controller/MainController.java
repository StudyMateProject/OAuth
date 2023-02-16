package com.springsecurity.oauth.controller;

import com.springsecurity.oauth.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class MainController {
    // 로그인 후 메인 페이지
    @GetMapping("/")
    public String main(Principal principal) {
        if ( principal == null ) {
            return "redirect:/n";
        }
        // 메인 페이지로 이동
        return "Main";
    }

    // 로그인 전 메인 페이지
    @GetMapping("/n")
    public String nmain() {
        // 메인 페이지로 이동
        return "Main";
    }

    // 로그인 페이지
    @GetMapping("/loginform") // required = false - 해당 필드가 URL 파라미터에 존재하지 않아도 에러가 발생하지 않는다.
    public String loginform(@RequestParam(value = "error", required = false) String error, // 1-1. URL 파라미터로 넘어오는 에러 체크값이 있을 경우 받는다.
                            @RequestParam(value = "errorMsg", required = false) String errorMsg, // 1-2. URL 파라미터로 넘어오는 에러 메세지가 있을 경우 받는다.
                            @RequestParam(value = "loginErrMsg", required = false) String loginErrMsg, // 1-3. URL 파라미터로 넘어오는 소셜 로그인 에러 메세지가 있을 경우 받는다.
                            Model model) { // 1. 파라미터로 넘어오는 각종 에러값들을 받아온다.
        // 구글 로그인 API를 사용하기 위해선 몇가지 과정을 거쳐야 한다.
        // 2. 가장 먼저 code가 필요하기에 여기서 code를 받아오는 구글 서버 URL을 미리 만들어서 가져간다.
        String googleUrl = "https://accounts.google.com/o/oauth2/v2/auth?" +
                           "scope=" + "email profile https://www.googleapis.com/auth/user.birthday.read https://www.googleapis.com/auth/user.gender.read https://www.googleapis.com/auth/user.phonenumbers.read" +
                           "&access_type=" + "offline" +
                           "&include_granted_scopes=" + "true" +
                           "&response_type=" + "code" +
                           "&state=" + "security_token%3D138r5719ru3e1%26url%3Dhttps://oauth2.example.com/token" +
                           "&redirect_uri=" + "http://localhost:8888/loginform/googletoken" +
                           "&client_id=" + "346535144521-qthfl467cl8jrdcdc2g8l3lvpurqsk1h.apps.googleusercontent.com";
        // 3. 구글 로그인 URL을 바인딩 한다.
        model.addAttribute("googleUrl", googleUrl);
        // 4. 1-1에서 받아온 에러 체크값을 바인딩 한다.
        model.addAttribute("error", error);
        // 5. 1-2에서 받아온 에러 메세지를 바인딩 한다.
        model.addAttribute("errorMsg", errorMsg);
        // 6. 1-3에서 받아온 소셜 로그인 에러 메세지를 바인딩 한다.
        model.addAttribute("loginErrMsg", loginErrMsg);
        // 로그인 페이지로 이동
        return "SignUp/LoginForm";
    }

    // 로그아웃 페이지
    @PostMapping("/logout")
    public void logout() {
        // 로그아웃 후 이동 페이지는 Spring Security가 각 핸들러를 통해 잡고 있기에 여기서 굳이 잡아줄 필요가 없다.
        // 메인 페이지로 이동
        // return "Main";
    }
}
