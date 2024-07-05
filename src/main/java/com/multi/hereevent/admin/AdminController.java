package com.multi.hereevent.admin;

import com.multi.hereevent.dto.CategoryDTO;
import com.multi.hereevent.dto.EventDTO;
import com.multi.hereevent.dto.MemberDTO;
import com.multi.hereevent.fileupload.FileUploadService;
import com.multi.hereevent.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
    /*회원선택삭제*/
    @PostMapping("/admin/member/deletelist")
    public String deleteMember(@RequestParam("memberNo") List<Integer> memberNo) {
        memberService.deleteMembers(memberNo);
        return "redirect:/admin/member";
    }

    /*회원수정버튼*/
    @GetMapping("/admin/member/update/{member_no}")
    public String memberUpdate(@PathVariable("member_no") int member_no, Model model){
        MemberDTO member = memberService.selectMemberDetail(member_no);
        model.addAttribute("member",member);
        return "admin/memberCor";
    }
    @PostMapping("/admin/member/update")
    public String memberUpdate(MemberDTO member) {
        MultipartFile memberImg = member.getProfile_img();
        if(!memberImg.isEmpty()) {
            String storeFilename = null;
            try {
                storeFilename = fileService.uploadProfileImg(memberImg);
                member.setImg_path(storeFilename);
            } catch (IOException e) {
                new RuntimeException(e);
                return "common/errorPage";
            }
        }
        memberService.updateMember(member);
        return "redirect:/admin/member";
    }
}
