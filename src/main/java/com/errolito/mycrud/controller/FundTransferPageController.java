package com.errolito.mycrud.controller;

import com.errolito.mycrud.entity.Account;
import com.errolito.mycrud.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping("/fund-transfer")
@RequiredArgsConstructor
public class FundTransferPageController {

    private final AccountService accountService;

    @GetMapping
    public String fundTransfer(Model model, @RequestParam(required = false) String id) {
        if (!NumberUtils.isDigits(id)) {
            return "component/error/record-not-found";
        } else {
            Optional<Account> accountOptional = accountService.findById(parseInt(id));

            if (accountOptional.isPresent()) {
                model.addAttribute("account", accountOptional.get());

                List<Account> accounts = accountService.findAllByAccountNumberNotIn(List.of(id))
                        .stream()
                        .filter(account -> !account.getId().equals(accountOptional.get().getId()))
                        .toList();

                model.addAttribute("accounts", accounts);

            } else {
                return "component/error/record-not-found";
            }
        }
        return "core/fund-transfer/fund-transfer";
    }
}
