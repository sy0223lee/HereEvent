package com.multi.hereevent.admin;

import com.multi.hereevent.category.CategoryService;
import com.multi.hereevent.dto.*;
import com.multi.hereevent.event.EventService;
import com.multi.hereevent.event.time.EventTimeService;
import com.multi.hereevent.fileupload.FileUploadService;
import com.multi.hereevent.member.MemberService;
import com.multi.hereevent.reserve.ReserveService;
import com.multi.hereevent.review.ReviewService;
import com.multi.hereevent.wait.WaitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@SessionAttributes("member")
public class AdminController {
    private final MemberService memberService;
    private final FileUploadService fileUploadService;
    private final ReviewService reviewService;
    private final EventService eventService;
    private final EventTimeService eventTimeService;
    private final CategoryService categoryService;
    private final ChartService chartService;
    private final WaitService waitService;
    private final ReserveService reserveService;

    /************** 관리 메인페이지 ************/
    @GetMapping("/admin")
    public String adminPage(Model model){
        MemberDTO member = (MemberDTO) model.getAttribute("member");
        if(member != null && member.getMgr() == 1) {
            // 차트에 필요한 리스트 model 에 add
            model.addAttribute("eventList", chartService.startEndEventCount());
            model.addAttribute("categoryList", chartService.categoryRate());
            model.addAttribute("memberList", chartService.newMemberCount());
            model.addAttribute("reserveList", chartService.reserveTopEvent());
            model.addAttribute("waitList", chartService.waitTopEvent());

            return "admin/home";
        }else{
            return "common/errorPage";
        }
    }

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
    public String deleteOneMember(@RequestParam("member_no") String member_no){
        int result = memberService.deleteMember(Integer.parseInt(member_no));
        if(result > 0){
            return "redirect:/admin/member";
        }else {
            return "common/errorPage";
        }
    }

    /*회원선택삭제*/
    @PostMapping("/admin/member/delete-select")
    public String deleteMember(@RequestParam("select") List<Integer> memberNo) {
        memberService.deleteMembers(memberNo);
        return "redirect:/admin/member";
    }

    /*회원수정버튼*/
    @GetMapping("/admin/member/update/{member_no}")
    public String memberUpdate(@PathVariable("member_no") int member_no, Model model){
        MemberDTO member = memberService.selectMemberDetail(member_no);
        // 속성명을 member 로 하면 session 에 저장된 member 의 값이 바뀌게 되므로 다른 속성명 이용 필요
        model.addAttribute("editMember",member);
        return "admin/updateMember";
    }
    @PostMapping("/admin/member/update")
    public String memberUpdate(MemberDTO member) {
        MultipartFile memberImg = member.getProfile_img();
        if(!memberImg.isEmpty()) {
            String storeFilename;
            try {
                storeFilename = fileUploadService.uploadProfileImg(memberImg);
                member.setImg_path(storeFilename);
            } catch (IOException e) {
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
    @PostMapping("/admin/review/delete")
    public String deleteOneReview(@RequestParam("review_no") String review_no){
        int result = reviewService.deleteReview(Integer.parseInt(review_no));
        if(result > 0){
            return "redirect:/admin/review";
        }else {
            return "common/errorPage";
        }
    }
    /*선택 삭제기능*/
    @PostMapping("/admin/review/delete-select")
    public String deleteReview(@RequestParam("select") List<Integer> reviewNo){
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
    @GetMapping("/admin/event/insert")
    public String createEventPage(Model model){
        List<CategoryDTO> categoryList  = categoryService.getListCategory();
        model.addAttribute("categoryList",categoryList);
        // 시간을 나타내는 문자열 리스트 생성
        List<String> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hours.add(String.format("%02d:00", i));
        }
        model.addAttribute("hours", hours);

        return "event/insert";
    }
    @PostMapping("/admin/event/insert")
    public String createEvent(EventDTO event) {
        //주소 합쳐서 넣기
        event.setAddr(event.getAddr()+event.getDetailAddress()+event.getExtraAddress());
        //행사 이미지 등록하기
        MultipartFile eventImg = event.getEvent_img();
        String storeFilename;
        try {
            storeFilename = fileUploadService.uploadEventImg(eventImg);
            event.setImg_path(storeFilename);
            //DB에 삽입
            eventService.insertEvent(event);
//            log.info("+++++"+event);
            EventDTO regievent = eventService.getEventDetail(event.getName());
//            log.info("register event::"+regievent);
            List<EventTimeDTO> eventTimeList = parseEventTimes(regievent.getEvent_no(),event.getEvent_time());
//            log.info("timeList::>>"+eventTimeList);
            eventTimeService.insertEventTimeList(eventTimeList);
            return "redirect:/admin/event";
        } catch (IOException e) {
            return "common/errorPage";
        }
    }
    //이벤트 시간 LIST 처리
    private List<EventTimeDTO> parseEventTimes(int eventNo, String eventTimesString) {
        List<EventTimeDTO> eventTimeList = new ArrayList<>();
        String[] eventTimesArray = eventTimesString.split(",");
        for (int i = 0; i < eventTimesArray.length; i += 3) {
            String day = eventTimesArray[i].trim();
            String startTime = eventTimesArray[i + 1].trim();
            String endTime = eventTimesArray[i + 2].trim();
            if (startTime.equalsIgnoreCase("휴무") || endTime.equalsIgnoreCase("휴무")) {
                startTime = null;
                endTime = null;
            }
            eventTimeList.add(new EventTimeDTO(eventNo, day, startTime, endTime));
        }
        return eventTimeList;
    }
    @GetMapping("/admin/event/update/{event_no}")
    public String updateEventPage(@PathVariable("event_no") int event_no, Model model){
        List<CategoryDTO> categoryList  = categoryService.getListCategory();
        model.addAttribute("categoryList",categoryList);
        EventDTO event = eventService.getEventDetails(event_no);
        System.out.println("event::"+event);
        model.addAttribute("event",event);
        List<EventTimeDTO> eventTimeList = eventTimeService.getEventTime(event_no);
        System.out.println("eventTimeList::"+eventTimeList);
        List<String> openTimes = new ArrayList<>();
        List<String> closeTimes = new ArrayList<>();
        for (EventTimeDTO eventTime : eventTimeList) {
            String openTime = eventTime.getOpen_time();
            String closeTime = eventTime.getClose_time();
            if (openTime != null) {
                openTimes.add(openTime.substring(0, 5));
            } else {
                openTimes.add("휴무"); // 기본값을 설정하거나 빈 값 추가
            }
            if (closeTime != null) {
                closeTimes.add(closeTime.substring(0, 5));
            } else {
                closeTimes.add("휴무"); // 기본값을 설정하거나 빈 값 추가
            }
        }
        model.addAttribute("openTimes", openTimes);
        System.out.println("opentimes:::"+openTimes);
        model.addAttribute("closeTimes", closeTimes);
        System.out.println("closetimes:::"+closeTimes);
//        log.info(eventTimeList.toString());
        model.addAttribute("eventTimeList",eventTimeList);
        // 시간을 나타내는 문자열 리스트 생성
        List<String> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hours.add(String.format("%02d:00", i));
            hours.add(String.format("%02d:30", i));
        }
        model.addAttribute("hours", hours);
        return "event/update";
    }
    @PostMapping("/admin/event/update")
    public String updateEvent(EventDTO event) {
        List<EventTimeDTO> eventTimeList = parseEventTimes(event.getEvent_no(), event.getEvent_time());
        eventTimeService.updateEventTImeList(eventTimeList);
        MultipartFile eventImg = event.getEvent_img();
        if (!eventImg.isEmpty()) {
            String storeFilename;
            try {
                storeFilename = fileUploadService.uploadEventImg(eventImg);
                event.setImg_path(storeFilename);
                event.setAddr(event.getAddr() + event.getDetailAddress() + event.getExtraAddress());

            } catch (IOException e) {
                return "common/errorPage";
            }
        }
        eventService.updateEvent(event);
        return "redirect:/admin/event";
    }
    @PostMapping("/admin/event/delete")
    public String deleteOneEvent(@RequestParam("event_no") String event_no){
        List<Integer> eventNo = new ArrayList<>();
        eventNo.add(Integer.parseInt(event_no));
        int result = eventService.deleteEvent(eventNo);
        if(result > 0){
            return "redirect:/admin/event";
        }else {
            return "common/errorPage";
        }
    }
    @PostMapping("/admin/event/delete-select")
    public String deleteEvent(@RequestParam("select") List<Integer> eventNo) {
        eventService.deleteEvent(eventNo);
        return "redirect:/admin/event";
    }


    /************** 대기 ************/
    @GetMapping("/admin/wait")
    public String selectWaitWithPage(@RequestParam Map<String, Object> params,
                                     @PageableDefault(value = 10) Pageable page, Model model){
        Page<WaitDTO> result = waitService.selectWaitWithPage(params, page);

        model.addAttribute("type", params.get("type"));
        model.addAttribute("keyword", params.get("keyword"));
        model.addAttribute("waitList", result.getContent());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("totalElements", result.getTotalElements());
        model.addAttribute("pageNumber", page.getPageNumber());
        return "admin/wait";
    }
    @PostMapping("/admin/wait/updateState")
    public String updateState(@RequestParam("wait_no") String wait_no, Model model) {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        WaitDTO eventDetail = waitService.eventDetail(Integer.parseInt(wait_no));
        eventDetail.setState("cancel");

        eventDetail.setWait_date(LocalDateTime.now());
        model.addAttribute("event_no", eventDetail.getEvent_no());
        waitService.updateState(eventDetail);

        return "redirect:/admin/wait";
    }

    @PostMapping("/admin/wait/update-select")
    public String updateStateSelect(@RequestParam("select") List<Integer> waitNos) {
        List<WaitDTO> waitList = new ArrayList<>();

        for (int i = 0; i < waitNos.size(); i++) {
            WaitDTO eventDetail = waitService.eventDetail(waitNos.get(i));
            eventDetail.setState("cancel");
            eventDetail.setWait_date(LocalDateTime.now());
            waitList.add(eventDetail);
        }
        int result = waitService.updateStateSelect(waitList);
        if(result > 0){
            return "redirect:/admin/wait";
        } else {
            return "common/errorPage";
        }
    }

    /************** 예약관리 ************/
    @GetMapping("/admin/reserve")
    public String selectReserveWithPage(@RequestParam Map<String, Object> params,
                                       @PageableDefault(value = 10) Pageable page, Model model){
        Page<ReserveDTO> result = reserveService.selectReserveWithPage(params, page);
        model.addAttribute("type", params.get("type"));
        model.addAttribute("keyword", params.get("keyword"));
        model.addAttribute("reserveList", result.getContent());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("totalElements", result.getTotalElements());
        model.addAttribute("pageNumber", page.getPageNumber());
        return "admin/reserve";
    }
    @PostMapping("/admin/reserve/cancel")
    public String cancelOneReserve(@RequestParam("reserve_no") String reserve_no){
        List<Integer> reserveNo = new ArrayList<>();
        reserveNo.add(Integer.parseInt(reserve_no));
        int result = reserveService.cancelReserve(reserveNo);
        if(result > 0){
            return "redirect:/admin/reserve";
        }else {
            return "common/errorPage";
        }
    }
    @PostMapping("/admin/reserve/cancel-select")
    public String cancelReserve(@RequestParam("select") List<Integer> reserveNo) {
        reserveService.cancelReserve(reserveNo);
        return "redirect:/admin/reserve";
    }




}
