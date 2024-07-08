package com.multi.hereevent.member;

import com.multi.hereevent.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.SecureRandom;

@Controller
@RequiredArgsConstructor
@SessionAttributes("member")
public class NaverLoginController {
    private final MemberService service;

    @GetMapping("/login/naver")
    public String naverConnect(){
        // state 용 난수 생성
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString(32);

        // 네이버 로그인 인증 요청문 생성
        String url = "https://nid.naver.com/oauth2.0/authorize?client_id=" + "08J45Bf5olDzizmNsIcJ" +
                    "&response_type=code&redirect_uri=http://223.130.158.5:9090/hereevent/login/naver/callback" +
                    "&state=" + state;

        System.out.println("===== 네이버 로그인 인증 요청 =====");
        return "redirect:" + url;
    }

    @GetMapping("/login/naver/callback")
    public String naverLogin(@RequestParam("code") String code, @RequestParam("state") String state, Model model) throws ParseException {
        System.out.println("===== 네이버 로그인 접근 토큰 요청 =====");
        String url = "https://nid.naver.com/oauth2.0/token";
        RestTemplate restTemplate = new RestTemplate();

        // HttpHeader Object
        HttpHeaders headers = new HttpHeaders();
        // HttpBody Object
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "08J45Bf5olDzizmNsIcJ");
        params.add("client_secret", "53JS4ixZ2M");
        params.add("code", code);
        params.add("state", state);
        // Http Header 와 Http Body params 를 가진 엔티티
        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(params, headers);

        // url 로 Http 요청, POST 방식
        ResponseEntity<String> response = restTemplate.exchange(url,
                HttpMethod.POST, naverTokenRequest, String.class);

        // 출력
        String responseBody = response.getBody();
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(responseBody);
        String accessToken = (String) object.get("access_token");

        getUserInfo(accessToken, model);

        return "mypage/mypage"; // 나중에 메인 페이지로 갈 수 있도록 수정
    }

    public void getUserInfo(String accessToken, Model model) throws ParseException {
        String url = "https://openapi.naver.com/v1/nid/me";
        RestTemplate restTemplate = new RestTemplate();

        // HttpHeader Object
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        // Http Header 와 Http Body params 를 가진 엔티티
        HttpEntity<MultiValueMap<String, String>> naverInfoRequest = new HttpEntity<>(headers);

        // url 로 Http 요청, POST 방식
        ResponseEntity<String> response = restTemplate.exchange(url,
                HttpMethod.POST, naverInfoRequest, String.class);

        // 출력
        String responseBody = response.getBody();
        JSONParser parser = new JSONParser();
        JSONObject root = (JSONObject) parser.parse(responseBody);
        JSONObject info = (JSONObject) root.get("response");

        String email = (String) info.get("email");
        String pass = "naver"; // 소셜 로그인의 경우 패스워드를 따로 저장하지 않으므로 어느 사이트 로그인인지 저장
        String name = (String) info.get("name");
        String nick = (String) info.get("nickname");
        String tel = (String) info.get("mobile");

        MemberDTO member = service.findMemberByEmail(email);
        if(member == null){ // 회원 정보가 존재하지 않으면 DB에 insert 한 후 로그인
            member = new MemberDTO(email, pass, name, nick, tel);
            service.insertMember(member);
            model.addAttribute("member", member);
        }
        model.addAttribute("member", member);
    }
}
