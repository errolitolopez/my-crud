package com.errolito.mycrud.controller;

import com.errolito.mycrud.dto.FileQuery;
import com.errolito.mycrud.dto.FileResponse;
import com.errolito.mycrud.dto.UserResponse;
import com.errolito.mycrud.facade.FileFacade;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static java.lang.Integer.parseInt;

@Controller
@RequiredArgsConstructor
@RequestMapping("/files")
public class FilePageController {

    private final FileFacade facade;

    @GetMapping
    public String list(Model model, FileQuery query, Pageable pageable) {
        Page<FileResponse> page = facade.findAll(query, pageable);
        model.addAttribute("page", page);
        model.addAttribute("name", query.getName());
        return "core/files/list";
    }

    @GetMapping("/upload")
    public String upload() {
        return "core/files/upload";
    }

    @GetMapping("/view")
    public String view(Model model, @RequestParam(required = false) String id) {

        if (!NumberUtils.isDigits(id)) {
            return "component/error/record-not-found";
        }

        Optional<FileResponse> fileOptional = facade.findById(parseInt(id));

        if (fileOptional.isPresent()) {
            model.addAttribute("file", fileOptional.get());
        } else {
            return "component/error/record-not-found";
        }

        return "core/files/view";
    }
}
