package com.errolito.mycrud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class AiChatPageController {

    @GetMapping("/spring-ai/chat")
    public String chat(Model model) {
        Map<String, String> llm = new LinkedHashMap<>() {{
            put("Gemini 3.1 Flash", "gemini-3.1-flash-lite-preview");
            put("Gemini 2.5 Flash", "gemini-2.5-flash-lite");
            put("Gemma 4", "gemma-4-26b-a4b-it");
        }};

        model.addAttribute("models", llm);
        return "core/spring-ai/chat";
    }
}