package com.multi.hereevent.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @GetMapping("/send-mail")
    public String SendRecommend(){
        log.info("send-mail");
        mailService.sendRecommendEmail();
        return "redirect:/admin";
    }
}
