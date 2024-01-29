package com.kimdev.Blog.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kimdev.Blog.config.auth.PrincipalDetail;
import com.kimdev.Blog.model.KakaoProfile;
import com.kimdev.Blog.model.OAuthToken;
import com.kimdev.Blog.model.User;
import com.kimdev.Blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

// 인증이 안 된 사용자들이 출입할 수 있는 경로를 /auth/**에만 허용할 것.
// 그냥 주소가 /이면 가는 index.jsp도 허용.
// static 이하에 있는 /js/**, /css/**, /image/** 등도 허용.


@Controller
public class UserController {
    @Value("${kimdev.key}")
    private String kkey;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/auth/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/auth/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(String code) {

        // POST 방식으로 key=value 데이터를 카카오 쪽으로 요청
        RestTemplate rt = new RestTemplate(); // http 요청 보내는 라이브러리
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // key-value 형태의 데이터를 보낸다고 알려주는 것.

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "xxxxxxxxxx");
        params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
        params.add("code", code);

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers); // params를 body로, headers를 헤더로 가지는 엔티티가 됨.

        // Http 요청하기 - Post 방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, kakaoTokenRequest, String.class); // 맨 마지막 String.class는 응답 받을 타입.


        // 액세스 토큰 발급
        // JSON에서 key=value 형태로 되어 있는 데이터를 각 오브젝트에 파싱해서 담음.
        ObjectMapper objectMapper = new ObjectMapper();

        OAuthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // 액세스 토큰을 가지고 리소스에 접근
        // POST 방식으로 key=value 데이터를 카카오 쪽으로 요청
        RestTemplate rt2 = new RestTemplate(); // http 요청 보내는 라이브러리
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // key-value 형태의 데이터를 보낸다고 알려주는 것.

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(headers2); // params를 body로, headers를 헤더로 가지는 엔티티가 됨.

        // Http 요청하기 - Post 방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response2 = rt2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, kakaoProfileRequest2, String.class); // 맨 마지막 String.class는 응답 받을 타입.


        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;

        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        // 받아온 카카오톡 프로필을 가지고 강제 회원 등록
        User kakaoUser = User.builder()
                    .username(kakaoProfile.getKakao_account().getProfile().getNickname())
                    .password(kkey)
                    .email("kakao@kakao.com")
                    .oauth("kakao")
                    .build();

        // 이미 가입되어 있는 회원인지 확인
        User originUser = userService.회원찾기(kakaoUser.getUsername());

        if (originUser.getUsername() == null) {
            userService.회원가입(kakaoUser);
        }

        // 로그인 처리
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), kkey));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/";
    }

    @GetMapping("/user/updateForm")
    public String updateForm(@AuthenticationPrincipal PrincipalDetail principal) {
        return "user/updateForm";
    }
}
