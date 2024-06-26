package com.multi.hereevent.event;

import com.multi.hereevent.category.CategoryService;
import com.multi.hereevent.dto.*;
import com.multi.hereevent.event.interest.EventInterestService;
import com.multi.hereevent.review.ReviewService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@SessionAttributes("member")
public class EventController {
    private final EventService eventService;
    private final ReviewService reviewService;
    private final EventInterestService interestService;

    @GetMapping("/test")
    public String test() {
        return "main/bootTest";
    }
    @GetMapping("/main")
    public String mainPage() {
        return "main/mainPage";
    }
    @GetMapping("/test2")
    public String test2(Model model) {
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
        return "main/main3";
    }

    //행사검색(프론트 아직)
    @GetMapping("/searchlist")
    public String searchPage() {
        return "main/search";
    }
    @PostMapping("/searchlist")
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
        List<ReviewDTO> reviewList = reviewService.selectReviewByEventNo(event_no);
        model.addAttribute("event", eventDetails);
        model.addAttribute("reviewList", reviewList);
        return "detailedPage/detailedPage";
    }

    //예약기능
    @PostMapping("/event/reservation")
    public String reservation(ReserveDTO reserve){
        if(eventService.checkReserveOrder(reserve.getEvent_no(),
                reserve.getReserve_date(),reserve.getReserve_time())==null){
            reserve.setReserve_order(1);
        }else{
            int order = reserve.getReserve_order();
            order++;
            reserve.setReserve_order(order);
        };
        eventService.insertReserve(reserve);
        return "main/mainPage2";
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
}

