package com.multi.hereevent.event;

import com.multi.hereevent.category.CategoryService;
import com.multi.hereevent.dto.*;
import com.multi.hereevent.event.interest.EventInterestService;
import com.multi.hereevent.event.time.EventTimeService;
import com.multi.hereevent.fileupload.FileUploadService;
import com.multi.hereevent.mail.MailService;
import com.multi.hereevent.review.ReviewService;

import com.multi.hereevent.wait.WaitService;
import lombok.RequiredArgsConstructor;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
    private final CategoryService categoryService;
    private final WaitService waitService;
    private final FileUploadService fileUploadService;
    private final MailService mailService;

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
        List<EventDTO> onGoingList = eventService.getOnGoingEvent();
        model.addAttribute("ongoinglist",onGoingList);
        return "main/mainPage";
    }

    //종류별 목록페이지
    @GetMapping("/list")
    public String listPage(@RequestParam("type") String type, Model model){
        model.addAttribute("type", type);
        return "main/listPage";
    }
    @PostMapping("/list")
    @ResponseBody
    public List<EventDTO> selectEventListByRank(@RequestBody Map<String, Object> data){
        String tag = (String) data.get("tag");
        ArrayList<String> state =  (ArrayList<String>) data.get("state");
        ArrayList<String> type = (ArrayList<String>) data.get("type");
        List<EventDTO> eventlist = new ArrayList<>();
        switch (tag) {
            case "star" -> eventlist = eventService.getStarEventWithCondition(state, type);
            case "popular" -> eventlist = eventService.getPopularEventWithCondition(state, type);
            case "open" -> eventlist = eventService.getOpenEventWithCondition(type);
            case "all" -> eventlist = eventService.getAllEventWithCondition(state, type);
        }
        return eventlist;
    }

    //카테고리별 리스트
    @GetMapping("/list/category/{category_no}")
    public String listCategoryPage(@PathVariable("category_no") int category_no, Model model){
        String categoryName = categoryService.selectCategoryName(category_no);
        model.addAttribute("categoryNo", category_no);
        model.addAttribute("categoryName", categoryName);
        return "main/categoryListPage";
    }
    @PostMapping("/list/category/{category_no}")
    @ResponseBody
    public List<EventDTO> selectEventListByCategory(@PathVariable("category_no") int category_no, @RequestBody Map<String, Object> data){
        ArrayList<String> state =  (ArrayList<String>) data.get("state");
        ArrayList<String> type = (ArrayList<String>) data.get("type");
        return eventService.getEventByCategoryWithCondition(category_no, state, type);
    }

    //행사검색
    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        List<EventDTO> searchlist = eventService.searchEvent(keyword);
        model.addAttribute("events", searchlist);
        model.addAttribute("keyword", keyword);
        return "search/searchResults";
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

        List<EventTimeDTO> eventTime = eventTimeService.getEventTime(event_no);
        List<CategoryDTO> category = categoryService.getListCategory();
//         System.out.println("시작일===>"+eventDetails.getStart_date());
        List<ReviewDTO> reviewList = reviewService.selectReviewByEventNo(event_no);
//         System.out.println(eventTime);
        List<String> closedDays = eventTimeService.getHolidayDays(event_no);

        model.addAttribute("event", eventDetails);
        model.addAttribute("eventtime",eventTime);
        model.addAttribute("category",category);
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("closedDays", closedDays);

        return "detailedPage/detailedPage";
    }

    //대기 현황 확인 페이지
    @GetMapping("/event/waitSituation")
    public String waitSituation(@RequestParam("event_no") int event_no, Model model) {

        int waitingCount = waitService.getWaitingCount(event_no);
        EventDTO eventDetails = eventService.getEventDetails(event_no);
        model.addAttribute("waitingCount", waitingCount);
        model.addAttribute("event", eventDetails);
        return "detailedPage/waitDetailedPage";
    }



//    @PostMapping("/event/reservation")
//    public String reservation(@PathVariable int event_no, ReserveDTO reserve,Model model){
//        if(eventService.checkReserveOrder(reserve.getEvent_no(),
//                reserve.getReserve_date(),reserve.getReserve_time())==null){
//            reserve.setReserve_order(1);
//        }else{
//            int order = reserve.getReserve_order();
//            order++;
//            reserve.setReserve_order(order);
//        }
//        MemberDTO member = (MemberDTO) model.getAttribute("member");
//        reserve.setReserve_no(member.getMember_no());
//        eventService.insertReserve(reserve);
//        return "redirect:/main";
//    }

    @PostMapping("/reservation/times")
    public ResponseEntity<Map<String, List<String>>> getEventTimes(@RequestBody Map<String, Object> request) {
        int event_no = (Integer) request.get("eventNo");
        String day = (String) request.get("day");
        System.out.println(event_no+":"+day);
        // 행사 번호와 요일에 따른 운영 시간을 가져오는 로직 (예시)
        List<String> times = eventTimeService.getOperTime(event_no, day);
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

    /***** 마이페이지 이벤트 내역 *****/
    @GetMapping("/myevent")
    public String myevent(Model model) {
        MemberDTO member = (MemberDTO) model.getAttribute("member");
        assert member != null;
        List<MemberEventDTO> eventList = eventService.selectMemberEvent(member.getMember_no());
        model.addAttribute("eventList", eventList);
        return "mypage/myevent";

    }


}
