package com.multi.hereevent.admin;

import com.multi.hereevent.category.CategoryService;
import com.multi.hereevent.dto.CategoryDTO;
import com.multi.hereevent.dto.EventDTO;
import com.multi.hereevent.dto.MemberDTO;
import com.multi.hereevent.dto.ReviewDTO;
import com.multi.hereevent.event.EventService;
import com.multi.hereevent.fileupload.FileUploadService;
import com.multi.hereevent.member.MemberService;
import com.multi.hereevent.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final FileUploadService fileUploadService;
    private final ReviewService reviewService;
    private final EventService eventService;
    private final CategoryService categoryService;

    /************** 회원관리 ************/
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
                storeFilename = fileUploadService.uploadProfileImg(memberImg);
                member.setImg_path(storeFilename);
            } catch (IOException e) {
                new RuntimeException(e);
                return "common/errorPage";
            }
        }
        memberService.updateMember(member);
        return "redirect:/admin/member";
    }

    /************** 리뷰관리 ************/
    @GetMapping("/admin/review")
    public String selectReviewWithPage(@RequestParam Map<String, Object> params,
                                       @PageableDefault(value = 10) Pageable page, Model model){
        Page<ReviewDTO> result = reviewService.selectReviewWithPage(params, page);
        model.addAttribute("type", params.get("type"));
        model.addAttribute("keyword", params.get("keyword"));
        model.addAttribute("reviewList", result.getContent());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("totalElements", result.getTotalElements());
        model.addAttribute("pageNumber", page.getPageNumber());
        return "admin/review";
    }
    /*상세보기 삭제버튼*/
    @PostMapping("/admin/review/deleteOne")
    public String deleteOneAdminReview(@RequestParam("review_no") String review_no){
        int result = reviewService.deleteReview(Integer.parseInt(review_no));
        if(result > 0){
            return "redirect:/admin/review";
        }else {
            return "common/errorPage";
        }
    }
    /*선택 삭제기능*/
    @PostMapping("/admin/review/delete")
    public String deleteAdminReview(@RequestParam("reviewNo") List<Integer> reviewNo){
        int result = reviewService.deleteReviewSelect(reviewNo);
        if(result > 0){
            return "redirect:/admin/review";
        }else {
            return "common/errorPage";
        }
    }

    /************** 행사관리 ************/
    @GetMapping("/admin/event")
    public String selectEventWithPage(@RequestParam Map<String, Object> params,
                                      @PageableDefault(value = 10,sort = "event_no", direction = Sort.Direction.DESC) Pageable page, Model model){
        Page<EventDTO> result = eventService.selectEventWithPage(params, page);
        model.addAttribute("type", params.get("type"));
        model.addAttribute("keyword", params.get("keyword"));
        model.addAttribute("eventList", result.getContent());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("totalElements", result.getTotalElements());
        model.addAttribute("pageNumber", page.getPageNumber());
        return "admin/event";
    }
    // insert, update, delete 만들어 놨는데 수정해서 쓰시면 될거같습니다.
    @GetMapping("/admin/event/insert")
    public String createEventPage(Model model){
        List<CategoryDTO> categoryList = new ArrayList<>();
        categoryList = categoryService.getListCategory();
        model.addAttribute("categoryList",categoryList);
        return "event/insert";
    }
    @PostMapping("/admin/event/insert")
    public String createEvent(EventDTO event) {
        //주소 합쳐서 넣기
        event.setAddr(event.getAddr()+event.getDetailAddress()+event.getExtraAddress());
        //행사 이미지 등록하기
        MultipartFile eventImg = event.getEvent_img();
        String storeFilename = null;
        try {
            storeFilename = fileUploadService.uploadEventImg(eventImg);
            event.setImg_path(storeFilename);
            //DB에 삽입
            eventService.insertEvent(event);
            System.out.println("+++++"+event);
            return "redirect:/admin/event";
        } catch (IOException e) {
            new RuntimeException();
            return "common/errorPage";
        }
    }
    @GetMapping("/admin/event/update/{event_no}")
    public String updateEventPage(@PathVariable("event_no") int event_no, Model model){
        List<CategoryDTO> categoryList = new ArrayList<>();
        categoryList = categoryService.getListCategory();
        model.addAttribute("categoryList",categoryList);
        EventDTO event = eventService.getEventDetails(event_no);
        model.addAttribute("event",event);
        return "event/update";
    }
    @PostMapping("/admin/event/update")
    public String updateEvent(@RequestParam("event_no") int event_no,EventDTO event) {
        MultipartFile eventImg = event.getEvent_img();
        String storeFilename = null;
        try {
            storeFilename = fileUploadService.uploadEventImg(eventImg);
            event.setImg_path(storeFilename);
            event.setAddr(event.getAddr()+event.getDetailAddress()+event.getExtraAddress());
            eventService.updateEvent(event);
            return "redirect:/admin/event";
        } catch (IOException e) {
            new RuntimeException(e);
            return "common/errorPage";
        }
    }

    @PostMapping("/admin/event/delete")
    public String deleteEvent(@RequestParam("eventNo") List<Integer> eventNo) {
        eventService.deleteEvent(eventNo);
        return "redirect:/admin/event";
    }
}
