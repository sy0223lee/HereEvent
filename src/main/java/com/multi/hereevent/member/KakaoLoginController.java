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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;




@Controller
@RequiredArgsConstructor
@SessionAttributes("member")
public class KakaoLoginController {
    private final MemberService service;

    @GetMapping("/login/kakao")
    public String kakaoConnect(){
        // state 용 난수 생성

        // 네이버 로그인 인증 요청문 생성
        String url = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+
                "798073a2ccc2f0cb985ac0f4f178f563&redirect_uri=http://223.130.158.5/hereevent/login/kakao/callback";
        //https://kauth.kakao.com/oauth/authorize

        System.out.println("===== 카카오 로그인 인증 요청 =====");
        return "redirect:" + url;
    }

    @GetMapping("/login/kakao/callback")
    public String kakaoLogin(@RequestParam("code") String code, Model model) throws ParseException {
        System.out.println("===== 카카오 로그인 접근 토큰 요청 =====");
        String url = "https://kauth.kakao.com/oauth/token";
        RestTemplate restTemplate = new RestTemplate();

        // HttpHeader Object
        HttpHeaders headers = new HttpHeaders();
        // HttpBody Object
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "798073a2ccc2f0cb985ac0f4f178f563");
        params.add("redirect_uri", "http://223.130.158.5:9090/hereevent/login/kakao/callback");
        params.add("code", code);
        // Http Header 와 Http Body params 를 가진 엔티티
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // url 로 Http 요청, POST 방식
        ResponseEntity<String> response = restTemplate.exchange(url,
                HttpMethod.POST, kakaoTokenRequest, String.class);

        // 출력
        String responseBody = response.getBody();
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(responseBody);
        String accessToken = (String) object.get("access_token");
        System.out.println("===카카오 토큰 발급==="+accessToken);
        getUserInfo(accessToken, model);

        return "redirect:/main";
    }

    public void getUserInfo(String accessToken, Model model) throws ParseException {
        String url = "https://kapi.kakao.com/v2/user/me";
        RestTemplate restTemplate = new RestTemplate();

        // HttpHeader Object
        HttpHeaders headers = new HttpHeaders();
        //headers.add("Content-Type", "application/x-www-form-urlencoded" + accessToken);
        headers.add("Authorization", "Bearer " + accessToken);
        // Http Header 와 Http Body params 를 가진 엔티티
        HttpEntity<MultiValueMap<String, String>> kakaoInfoRequest = new HttpEntity<>(headers);

        // url 로 Http 요청, POST 방식
        ResponseEntity<String> response = restTemplate.exchange(url,
                HttpMethod.POST, kakaoInfoRequest, String.class);

        // 출력
        String responseBody = response.getBody();
        System.out.println("responseBody=========>"+responseBody);
        JSONParser parser = new JSONParser();
        JSONObject root = (JSONObject) parser.parse(responseBody);
        JSONObject properties = (JSONObject) root.get("properties");
        JSONObject kakaoAccount = (JSONObject) root.get("kakao_account");
        System.out.println("root===>"+root);
        System.out.println("properties===>"+properties);
        System.out.println("kakaoAccount===>"+kakaoAccount);

        String email = (String) kakaoAccount.get("email");
        System.out.println("email===>"+email);
        String pass = "kakao"; // 소셜 로그인의 경우 패스워드를 따로 저장하지 않으므로 어느 사이트 로그인인지 저장
        String name = "kakao";
        String nick = (String) properties.get("nickname");
        System.out.println("nick===>"+nick);
        String tel = "kakao";

        MemberDTO member = service.findMemberByEmail(email);
        if(member == null){ // 회원 정보가 존재하지 않으면 DB에 insert 한 후 로그인
            member = new MemberDTO(email, pass, name, nick, tel);
            service.insertMember(member);
            model.addAttribute("member", member);
        }
        model.addAttribute("member", member);
    }
}
