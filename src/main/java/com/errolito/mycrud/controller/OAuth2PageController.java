package com.errolito.mycrud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuth2PageController {
    @GetMapping("/oauth2/callback")
    public String oAuth2CallBack() {
        return "oauth2-callback";
    }
}
