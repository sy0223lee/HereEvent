package com.multi.hereevent.member;

import com.multi.hereevent.category.interest.CategoryInterestService;
import com.multi.hereevent.dto.CategoryInterestDTO;
import com.multi.hereevent.dto.MemberDTO;
import com.multi.hereevent.fileupload.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@SessionAttributes("member")
public class MemberController {
    private final MemberService memberService;
    private final FileUploadService fileService;
    private final CategoryInterestService categoryService;

    /***** 로그인, 회원가입 *****/
    @GetMapping("/login")
    public String loginPage(){
        return "login/login";
    }
    @PostMapping("/login")
    public String login(MemberDTO member, Model model) {
        MemberDTO loginMember = memberService.loginMember(member);
        model.addAttribute("member", loginMember);
        return "redirect:/main";
    }
    @GetMapping("/logout")
    public String logout(SessionStatus status) {
        status.setComplete(); // 세션에 있는 객체를 제거
        return "redirect:/main";
    }
    @GetMapping("/register")
    public String register() {
        return "login/register";
    }
    @PostMapping("/insert")
    public String register(MemberDTO member, Model model){
        memberService.insertMember(member);
        MemberDTO findmem = memberService.findMemberByEmail(member.getEmail());
        List<CategoryInterestDTO> categoryList = categoryService.selectCategoryInterestByMemberNo(findmem.getMember_no());
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("member",findmem);
        return "login/interestCategory";
    }
    //관심 카테고리 설정
    @GetMapping("/interestCategory")
    public String interestCategoryPage(Model model){
        MemberDTO member = (MemberDTO) model.getAttribute("member");
        assert member != null;
        List<CategoryInterestDTO> categoryList = categoryService.selectCategoryInterestByMemberNo(member.getMember_no());
        model.addAttribute("categoryList", categoryList);
        return "login/interestCategory";
    }
    // 관심 카테고리 추가
    @GetMapping("/interestCategory/insert")
    public String insertCategoryInterest(@RequestParam("category_no") int category_no, Model model) {
        MemberDTO member = (MemberDTO) model.getAttribute("member");
        if(member != null) {
            int result = categoryService.insertCategoryInterest(category_no, member.getMember_no());
            if(result > 0) {
                return "redirect:/interestCategory";
            }
        }
        return "common/errorPage";
    }
    // 관심 카테고리 삭제
    @GetMapping("/interestCategory/delete")
    public String deleteCategoryInterest(@RequestParam("category_no") int category_no, Model model) {
        MemberDTO member = (MemberDTO) model.getAttribute("member");
        if(member != null) {
            int result = categoryService.deleteCategoryInterest(category_no, member.getMember_no());
            if(result > 0) {
                return "redirect:/interestCategory";
            }
        }
        return "common/errorPage";
    }

    /***** 마이페이지 *****/
    @GetMapping("/mypage")
    public String mypage() {
        return "mypage/mypage";
    }
    // 닉네임 수정 페이지 이동
    @GetMapping("/mypage/edit-nick")
    public String editNickPage() {
        return "mypage/editNick";
    }
    // 닉네임 수정
    @PostMapping("/mypage/edit-nick")
    public String editNick(MemberDTO member, Model model) {
        int result = memberService.updateMemberNick(member);
        if(result > 0) {
            model.addAttribute("member", memberService.selectMemberDetail(member.getMember_no()));
            return "redirect:/mypage";
        }else{
            return "common/errorPage";
        }
    }
    // 닉네임 중복 확인(마이페이지)
    @PostMapping(value = "/mypage/check-nick", produces = "application/text; charset=utf-8")
    @ResponseBody
    public String checkNick(@RequestParam("nick") String nick) {
        boolean available = memberService.checkMemberNick(nick);
        if (available) {
            return "사용 가능한 닉네임";
        }else {
            return "사용 불가능한 닉네임";
        }
    }
    // 닉네임 중복 확인(회원가입)
    @PostMapping(value = "/check-nick", produces = "application/text; charset=utf-8")
    @ResponseBody
    public String checkNickRegister(@RequestParam("nick") String nick) {
        boolean available = memberService.checkMemberNick(nick);
        if (available) {
            return "사용 가능한 닉네임";
        }else {
            return "사용 불가능한 닉네임";
        }
    }
    //이메일 중복 확인(회원가입)
    @PostMapping(value = "/check-email", produces = "application/text; charset=utf-8")
    @ResponseBody
    public String checkEmail(@RequestParam("email") String email) {
        boolean available = memberService.checkMemberEmail(email);
        if (available) {
            return "사용 가능한 이메일";
        }else {
            return "사용 불가능한 이메일";
        }
    }
    // 생일 수정 페이지 이동
    @GetMapping("/mypage/edit-birth")
    public String editBirthPage() {
        return "mypage/editBirth";
    }
    // 생일 수정
    @PostMapping("/mypage/edit-birth")
    public String editBirth(MemberDTO member, Model model) {
        int result = memberService.updateMemberBirth(member);
        if(result > 0) {
            model.addAttribute("member", memberService.selectMemberDetail(member.getMember_no()));
            return "redirect:/mypage";
        }else {
            return "common/errorPage";
        }
    }
    // 프로필 사진 수정 페이지 이동
    @GetMapping("/mypage/edit-profile-img")
    public String editProfileImgPage(){
        return "mypage/editProfileImg";
    }
    // 프로필 사진 수정
    @PostMapping("/mypage/edit-profile-img")
    public String editProfileImg(MemberDTO member, Model model) {
        System.out.println("[editProfileImg] member = " + member);
        MultipartFile profileImg = member.getProfile_img();
        String storeFilename = null;
        try {
            storeFilename = fileService.uploadProfileImg(profileImg);
            member.setImg_path(storeFilename);
            memberService.updateMemberProfileImg(member);
            model.addAttribute("member", memberService.selectMemberDetail(member.getMember_no()));
            return "redirect:/mypage";
        } catch (IOException e) {
            e.printStackTrace();
            return "common/errorPage";
        }
    }

    /***** 관리자 페이지 이동 *****/
    @GetMapping("/admin")
    public String adminPage(Model model){
        MemberDTO member = (MemberDTO) model.getAttribute("member");
        if(member != null && member.getMgr() == 1){
            return "admin/home";
        }else{
            return "common/errorPage";
        }
    }
}
