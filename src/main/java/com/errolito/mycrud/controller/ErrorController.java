package com.errolito.mycrud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {

    @GetMapping("/too-many-requests")
    public String tooManyRequests() {
        return "/component/error/too-many-requests";
    }
}