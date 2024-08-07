package com.multi.hereevent.event.crawling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CrawlingController {
    private final CrawlingService service;

    @GetMapping("/crawling")
    public String crawling(){
        log.info("crawling");
        service.insertEventInfo();
        return "redirect:/admin";
    }
}
