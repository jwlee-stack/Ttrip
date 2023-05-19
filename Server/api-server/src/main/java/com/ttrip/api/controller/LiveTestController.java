package com.ttrip.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LiveTestController {
    @GetMapping("/ws/live")
    public String liveGET(){
        return "live";
    }
}
