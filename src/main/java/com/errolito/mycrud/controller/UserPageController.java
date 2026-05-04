package com.errolito.mycrud.controller;

import com.errolito.mycrud.dto.UserQuery;
import com.errolito.mycrud.entity.User;
import com.errolito.mycrud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.Set;

import static java.lang.Integer.parseInt;

@Controller
@RequiredArgsConstructor
public class UserPageController {

    private final UserService service;

    @GetMapping("/users")
    public String list(Model model, UserQuery query, Pageable pageable) {
        Page<User> page = service.findAll(query, pageable);
        model.addAttribute("page", page);
        model.addAttribute("username", query.getUsername());
        return "core/users/list";
    }

    @GetMapping("/users/{mode}/form")
    public String form(Model model, @PathVariable String mode, @RequestParam(required = false) String id) {
        if (!Set.of("create", "edit").contains(mode)) {
            return "component/error/page-not-found";
        }

        if ("edit".equals(mode)) {
            if (!NumberUtils.isDigits(id)) {
                return "component/error/record-not-found";
            }

            Optional<User> userOptional = service.findById(parseInt(id));

            if (userOptional.isPresent()) {
                model.addAttribute("user", userOptional.get());
            } else {
                return "component/error/record-not-found";
            }
        }

        model.addAttribute("mode", mode);
        return "core/users/form";
    }
}