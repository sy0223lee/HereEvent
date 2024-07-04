package com.multi.hereevent.event.crawling;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CrawlingController {
    private final CrawlingService service;

    @GetMapping("/crawling")
    public String crawling(){
        service.insertEventInfo();
        return "redirect:/admin";
    }
}
