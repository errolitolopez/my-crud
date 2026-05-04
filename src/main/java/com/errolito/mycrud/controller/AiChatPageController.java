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
            put("Llama 3 (Meta AI)", "llama3.2:1b");
            put("Phi 3 (Microsoft)", "phi3:3.8b");
            put("Phi 3 Mini (Microsoft)", "phi3:mini");
        }};

        model.addAttribute("models", llm);
        return "core/spring-ai/chat";
    }
}