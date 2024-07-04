package com.multi.hereevent.admin;

import com.multi.hereevent.dto.CategoryInterestDTO;
import com.multi.hereevent.dto.MemberDTO;
import com.multi.hereevent.dto.MemberImgDTO;
import com.multi.hereevent.fileupload.FileUploadService;
import com.multi.hereevent.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@SessionAttributes("member")
public class AdminController {
    private final MemberService memberService;
    private final FileUploadService fileService;

    /* 회원 관리 */
    @GetMapping("/admin/member")
    public String adminMemberPage(@RequestParam Map<String,Object> params,
                                  @PageableDefault(value = 10) Pageable page, Model model) {
        Page<MemberDTO> result = memberService.selectMemberWithPage(params,page);
        model.addAttribute("type", params.get("type"));
        model.addAttribute("keyword", params.get("keyword"));
        model.addAttribute("memberList", result.getContent());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("totalElements", result.getTotalElements());
        model.addAttribute("pageNumber", page.getPageNumber());
        return "admin/member";
    }

    /*단일 삭제버튼*/
    @PostMapping("/admin/member/delete")
    public String deleteAdminMember(@RequestParam("member_no") String member_no){
        int result = memberService.deleteMember(Integer.parseInt(member_no));
        if(result > 0){
            return "redirect:/admin/member";
        }else {
            return "common/errorPage";
        }
    }

    /*회원등록버튼*//*
    @GetMapping("/admin/member/memberReg")
    public String register() {
        return "admin/memberReg";
    }
    @PostMapping("/admin/member/memberReg")
    public String register(MemberDTO member) throws IOException {
        MultipartFile file = member.getProfile_img();

        if (file == null || file.isEmpty()) {
            System.out.println("파일이 전송되지 않았습니다.");
            return "common/errorPage"; // 에러 페이지로 리디렉션 또는 다른 처리
        }

        System.out.println(file); // 파일 정보 출력
        MemberImgDTO memberImgDTO = fileService.uploadMemberImg(file);
        System.out.println(memberImgDTO); // 파일 업로드 결과 출력
        int result = memberService.RegMember(member, memberImgDTO);
        System.out.println(result); // 결과 출력

        if(result > 0){
            return "redirect:/admin/member";
        } else {
            return "common/errorPage";
        }
    }*/

    /*회원수정버튼*/
    @GetMapping("/admin/member/memberCor")
    public String memberCor() {
        return "admin/memberCor";
    }
    /*@PostMapping("/admin/member/memberReg")
    public String memberCor() throws{

    }*/
}
