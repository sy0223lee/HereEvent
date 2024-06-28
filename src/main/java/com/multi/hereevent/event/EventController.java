package com.multi.hereevent.event;

import com.multi.hereevent.dto.*;
import com.multi.hereevent.event.interest.EventInterestService;
import com.multi.hereevent.event.time.EventTimeService;
import com.multi.hereevent.review.ReviewService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@SessionAttributes("member")
public class EventController {
    private final EventService eventService;
    private final ReviewService reviewService;
    private final EventInterestService interestService;
    private final EventTimeService eventTimeService;

    @GetMapping("/main")
    public String mainPage(Model model) {
        List<FourEventByCategoryDTO> fourlist = eventService.selectFourEventByCategory();
        model.addAttribute("fourlist",fourlist);
        List<EventDTO> starlist = eventService.getListByStarRank();
        model.addAttribute("starlist",starlist);
        List<EventDTO> alleventlist = eventService.getAllEvent();
        model.addAttribute("alleventlist",alleventlist);
        List<EventDTO> openlist = eventService.getOpenEvent();
        model.addAttribute("openlist",openlist);
        List<EventDTO> popularlist = eventService.getPopularEvent();
        model.addAttribute("popularlist",popularlist);
        return "main/mainPage";
    }

    //행사검색(프론트 아직)
    @GetMapping("/event/searchlist")
    public String searchPage() {
        return "main/search";
    }
    @PostMapping("/event/searchlist")
    public String searchlist(@RequestParam("keyword") String keyword, Model model) {
        List<EventDTO> searchlist = eventService.searchEvent(keyword);
        model.addAttribute("searchlist",searchlist);
        return "main/search";
    }

    // 세부페이지
    @GetMapping("/event/{event_no}")
    public String getEventDetails(@PathVariable("event_no") int event_no, Model model) {
        MemberDTO member = (MemberDTO) model.getAttribute("member");
        EventDTO eventDetails;
        if(member != null){
            // 로그인 되어 있는 경우 사용자가 관심 있는 이벤트인지 같이 넘겨주기
            eventDetails = eventService.getEventDetails(event_no, member.getMember_no());
        }else {
            // 로그인이 안 되어 있는 경우 이벤트 정보만 넘겨주기
            eventDetails = eventService.getEventDetails(event_no);
        }
        System.out.println("시작일===>"+eventDetails.getStart_date());
        List<ReviewDTO> reviewList = reviewService.selectReviewByEventNo(event_no);
        model.addAttribute("event", eventDetails);
        model.addAttribute("reviewList", reviewList);
        return "detailedPage/detailedPage";
    }

    //예약기능
    @PostMapping("/event/reservation")
    public String reservation(ReserveDTO reserve,Model model){
        if(eventService.checkReserveOrder(reserve.getEvent_no(),
                reserve.getReserve_date(),reserve.getReserve_time())==null){
            reserve.setReserve_order(1);
        }else{
            int order = reserve.getReserve_order();
            order++;
            reserve.setReserve_order(order);
        }
        MemberDTO member = (MemberDTO) model.getAttribute("member");
        reserve.setReserve_no(member.getMember_no());
        eventService.insertReserve(reserve);
        return "redirect:/main";
    }
    @PostMapping("/reservation/times")
    public ResponseEntity<Map<String, List<String>>> getEventTimes(@RequestBody Map<String, Object> request) {
        int event_no = (Integer) request.get("eventNo");
        String day = (String) request.get("day");
        System.out.println(event_no+":"+day);
        // 행사 번호와 요일에 따른 운영 시간을 가져오는 로직 (예시)
        List<String> times = eventTimeService.getOperTime(event_no,day);
        Map<String, List<String>> response = new HashMap<>();
        response.put("times", times);
        return ResponseEntity.ok(response);
    }

    //이벤트 사진 가져오기
    @GetMapping("/event/image/{event_no}")
    @ResponseBody
    public EventDTO getEventImage(@PathVariable("event_no") int event_no, Model model) {
        EventDTO eventDetails = eventService.getEventDetails(event_no);
        model.addAttribute("event", eventDetails);
        return eventService.getEventImage(event_no);
    }

    //카테고리별 리스트
    @GetMapping("/event/list/{category_no}")
    public String listCategory(@PathVariable("category_no") int category_no, Model model){
        List<EventDTO> eventlist = eventService.selectEventByCategoryNo(category_no);
        model.addAttribute("eventlist",eventlist);
        return "event/eventCategoryList";
    }

    // 관심 이벤트 등록, 해제
    @GetMapping("/event/interest/insert")
    public String insertInterest(@RequestParam("event_no") int event_no, Model model){
        MemberDTO member = (MemberDTO) model.getAttribute("member");
        assert member != null;
        int result = interestService.insertEventInterest(event_no, member.getMember_no());
        if(result > 0){
            return "redirect:/event/" + event_no;
        }
        return "common/errorPage";
    }
    @GetMapping("/event/interest/delete")
    public String deleteInterest(@RequestParam("event_no") int event_no, Model model){
        MemberDTO member = (MemberDTO) model.getAttribute("member");
        assert member != null;
        int result = interestService.deleteEventInterest(event_no, member.getMember_no());
        if(result > 0){
            return "redirect:/event/" + event_no;
        }
        return "common/errorPage";
    }
    @GetMapping("/myinterest/delete")
    public String deleteMyInterest(@RequestParam("event_no") int event_no, Model model){
        MemberDTO member = (MemberDTO) model.getAttribute("member");
        assert member != null;
        int result = interestService.deleteEventInterest(event_no, member.getMember_no());
        if(result > 0){
            return "redirect:/myinterest";
        }
        return "common/errorPage";
    }

    /***** 관리자 페이지 *****/
    @GetMapping("/admin/event")
    public String adminEventPage(Model model){
        List<EventDTO> eventList = eventService.selectAll();
        for(EventDTO event : eventList){
            EventDTO eventDetails = eventService.getEventDetails(event.getEvent_no());
            event.setImg_path(eventDetails.getImg_path());
        }
        model.addAttribute("event", eventList);
        return "admin/event";
    }
    // insert, update, delete 만들어 놨는데 수정해서 쓰시면 될거같습니다.
//    @PostMapping("/admin/event")
//    public String createEvent(EventDTO eventDTO) {
//        eventService.insertEvent(eventDTO);
//        return "redirect:/admin/event";
//    }
//
//    @PostMapping("/admin/event/update/{event_no}")
//    public String updateEvent(@PathVariable("event_no") int event_no, @RequestBody EventDTO eventDTO) {
//        eventDTO.setEvent_no(event_no);
//        eventService.updateEvent(eventDTO);
//        return "redirect:/admin/event";
//    }
//
//    @GetMapping("/admin/event/delete/")
//    public String deleteEvent(@RequestParam("event_no") int event_no) {
//        eventService.deleteEvent(event_no);
//        return "redirect:/admin/event";
//    }



}

