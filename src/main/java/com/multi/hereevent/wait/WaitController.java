package com.multi.hereevent.wait;

import com.multi.hereevent.dto.EventDTO;
import com.multi.hereevent.dto.WaitDTO;
import com.multi.hereevent.event.EventService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class WaitController {
    private final WaitService service;
    private final EventService eventService;

    @GetMapping("/wait/register/event/{event_no}")
    public String register(@PathVariable("event_no") int event_no, Model model) {
        EventDTO eventDetails = eventService.getEventDetails(event_no);
        model.addAttribute("event", eventDetails);
        return "waitPage/waitregister";
    }
    @PostMapping("/wait/insert")
    public String register(WaitDTO wait, RedirectAttributes redirectAttributes, @RequestParam("wait_tel") String waitTel){
        if(!service.canInsert(waitTel)){
            redirectAttributes.addAttribute("error", "이미 다른 팝업스토어에 대기 중 입니다.");

        }else {
            service.waitInsert(wait);
        }
        redirectAttributes.addAttribute("event_no", wait.getEvent_no());
        redirectAttributes.addAttribute("success", "true");
        return "redirect:/wait/register/event/{event_no}";
    }

    @GetMapping("/wait/login")
    public String loginPage(){
        return "waitPage/waitlogin";
    }
    @PostMapping("/wait/login")
    public String login(WaitDTO wait, Model model, RedirectAttributes redirectAttributes) {
        WaitDTO loginMyWait = service.waitLogin(wait);
        WaitDTO waitDetailTel = service.waitDetailTel(wait.getWait_tel());
        model.addAttribute("wait", loginMyWait);
        if (loginMyWait == null) {
            redirectAttributes.addAttribute("errorMessage", "등록되지 않은 번호입니다.");
            return "redirect:/wait/login";
        }
        redirectAttributes.addAttribute("wait_no", waitDetailTel.getWait_no());
        redirectAttributes.addAttribute("event_no", waitDetailTel.getEvent_no());
        return "redirect:/wait/mywait/{event_no}/{wait_no}";


    }

    @GetMapping("/wait/mywait/{event_no}/{wait_no}")
    public String mywait(@PathVariable("wait_no") int wait_no,@PathVariable("event_no") int event_no, Model model) {
        WaitDTO eventDetail = service.EventDetail(wait_no);
        model.addAttribute("event", eventDetail);
        System.out.println(eventDetail);
        int position = service.getWaitingPosition(event_no, wait_no);
        int waitingCount = service.getWaitingCount(event_no);
        String waitTime = service.getEntranceWaitTime(event_no, wait_no);

        model.addAttribute("waitTime", waitTime);
        model.addAttribute("position", position);
        model.addAttribute("waitingCount", waitingCount);
        return "waitPage/mywait";
    }
    @PostMapping("/wait/updateState")
    public String updateState(@RequestParam("wait_no") String wait_no, @RequestParam("action") String action, Model model) {

        WaitDTO eventDetail = service.EventDetail(Integer.parseInt(wait_no));
        String statusMessage = "";
        if ("visit".equals(action)) {
            eventDetail.setState("visit");
            statusMessage = "visit";
        } else if ("cancel".equals(action)) {
            eventDetail.setState("cancel");
            statusMessage = "cancel";
        }
        eventDetail.setWait_date(LocalDateTime.now());
        model.addAttribute("event_no", eventDetail.getEvent_no());

        service.updateState(eventDetail);
        System.out.println(eventDetail);

        return "redirect:/main?status=" + statusMessage;
    }

    @GetMapping("/wait/delete")
    public String delete(@RequestParam("wait_no") int wait_no) {
        System.out.println(wait_no);
        service.waitDelete(wait_no);
        return "redirect:/main";
    }







}
