package com.errolito.mycrud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class FundTransferPageController {
    @GetMapping("/")
    public String index() {
        return "fund-transfer";
    }
}
