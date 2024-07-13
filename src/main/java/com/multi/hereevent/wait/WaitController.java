package com.multi.hereevent.wait;

import com.multi.hereevent.dto.EventDTO;
import com.multi.hereevent.dto.WaitDTO;
import com.multi.hereevent.event.EventService;
import com.multi.hereevent.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
@SessionAttributes("member")
public class WaitController {
    private final WaitService waitService;
    private final EventService eventService;
    private final MailService mailService;

    //대기 현황 확인 페이지
    @GetMapping("/event/waitSituation")
    public String waitSituation(@RequestParam("event_no") int event_no, Model model) {

        int waitingCount = waitService.getWaitingCount(event_no);
        EventDTO eventDetails = eventService.getEventDetails(event_no);
        model.addAttribute("waitingCount", waitingCount);
        model.addAttribute("event", eventDetails);
        return "wait/waitDetail";
    }

    @GetMapping("/wait/register/event/{event_no}")
    public String register(@PathVariable("event_no") int event_no, Model model) {
        EventDTO eventDetails = eventService.getEventDetails(event_no);
        model.addAttribute("event", eventDetails);
        return "wait/waitregister";
    }

    @PostMapping("/wait/insert")
    public String register(WaitDTO wait, RedirectAttributes redirectAttributes, @RequestParam("wait_tel") String waitTel) {
        if (!waitService.canInsert(waitTel)) {
            redirectAttributes.addAttribute("error", "이미 다른 이벤트에 대기 중 입니다.");
        } else {
            boolean inserted = waitService.registerWait(wait);
            if (inserted) {
                // 대기 등록 성공 시 메일 전송
                mailService.sendWaitSuccessEmail(wait);
                redirectAttributes.addAttribute("success", "true");
            } else {
                redirectAttributes.addAttribute("error", "일일 대기 입장 인원이 초과되었습니다.");
            }
        }
        redirectAttributes.addAttribute("event_no", wait.getEvent_no());
        return "redirect:/wait/register/event/{event_no}";
    }

    @GetMapping("/wait/login")
    public String loginPage(){
        return "wait/waitlogin";
    }
    @PostMapping("/wait/login")
    public String login(WaitDTO wait, Model model, RedirectAttributes redirectAttributes) {
        WaitDTO waitInfo = waitService.getWaitInfo(wait.getWait_tel());
        model.addAttribute("wait", waitInfo);
        if (waitInfo == null) {
            redirectAttributes.addAttribute("errorMessage", "등록되지 않은 번호입니다.");
            return "redirect:/wait/login";
        }
        redirectAttributes.addAttribute("wait_no", waitInfo.getWait_no());
        redirectAttributes.addAttribute("event_no", waitInfo.getEvent_no());
        return "redirect:/wait/mywait/{event_no}/{wait_no}";
    }

    @GetMapping("/wait/mywait/{event_no}/{wait_no}")
    public String mywait(@PathVariable("event_no") int event_no, @PathVariable("wait_no") int wait_no, Model model) {
        log.info("[wait_no] {}", wait_no);
        WaitDTO eventDetail = waitService.eventDetail(wait_no);
        model.addAttribute("event", eventDetail);
//        log.info(String.valueOf(eventDetail));
        int position = waitService.getWaitingPosition(event_no, wait_no);
        int waitingCount = waitService.getWaitingCount(event_no);
        String waitTime = waitService.getEntranceWaitTime(event_no, wait_no);

        model.addAttribute("waitTime", waitTime);
        model.addAttribute("position", position);
        model.addAttribute("waitingCount", waitingCount);
        return "wait/mywait";
    }
    @PostMapping("/wait/updateState")
    public String updateState(@RequestParam("wait_no") String wait_no, @RequestParam("action") String action, Model model) {
        WaitDTO eventDetail = waitService.eventDetail(Integer.parseInt(wait_no));
        if ("visit".equals(action)) {
            eventDetail.setState("visit");
        } else if ("cancel".equals(action)) {
            eventDetail.setState("cancel");
        }
        eventDetail.setWait_date(LocalDateTime.now());
        waitService.updateState(eventDetail);
//        log.info(String.valueOf(eventDetail));

        if(model.getAttribute("member") == null) {
            return "redirect:/event/" + eventDetail.getEvent_no();
        }else{
            return "redirect:/myevent";
        }
    }

    @GetMapping("/wait/delete")
    public String delete(@RequestParam("wait_no") int wait_no) {
        log.info(String.valueOf(wait_no));
        waitService.waitDelete(wait_no);
        return "redirect:/main";
    }

}
