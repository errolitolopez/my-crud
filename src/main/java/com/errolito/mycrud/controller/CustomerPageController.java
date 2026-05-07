package com.errolito.mycrud.controller;

import com.errolito.mycrud.dto.CustomerQuery;
import com.errolito.mycrud.dto.CustomerResponse;
import com.errolito.mycrud.entity.Account;
import com.errolito.mycrud.entity.FundTransferAudit;
import com.errolito.mycrud.facade.CustomerFacade;
import com.errolito.mycrud.service.AccountService;
import com.errolito.mycrud.service.FundTransferAuditService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerPageController {

    private final CustomerFacade facade;
    private final AccountService accountService;
    private final FundTransferAuditService fundTransferAuditService;

    @GetMapping
    public String list(Model model, CustomerQuery query, Pageable pageable) {
        Page<CustomerResponse> page = facade.findAll(query, pageable);
        model.addAttribute("page", page);
        model.addAttribute("fullName", query.getFullName());
        return "core/customer/list";
    }

    @GetMapping("/accounts")
    public String accounts(Model model, @RequestParam(required = false) String id) {
        if (!NumberUtils.isDigits(id)) {
            return "component/error/record-not-found";
        } else {
            Optional<CustomerResponse> customerOptional = facade.findById(Integer.parseInt(id));

            if (customerOptional.isPresent()) {
                model.addAttribute("accounts", customerOptional.get().getAccounts());
            } else {
                return "component/error/record-not-found";
            }
        }
        return "core/customer/accounts";
    }

    @GetMapping("/transaction-history")
    public String transactionHistory(Model model, @RequestParam(required = false) String id) {
        if (!NumberUtils.isDigits(id)) {
            return "component/error/record-not-found";
        } else {
            Optional<Account> accountOptional = accountService.findById(parseInt(id));

            if (accountOptional.isPresent()) {
                List<FundTransferAudit> fundTransferAudits = fundTransferAuditService
                        .findAllByAccountNumber(accountOptional.get().getAccountNumber());

                model.addAttribute("fundTransferAudits", fundTransferAudits);
                model.addAttribute("id", id);

            } else {
                return "component/error/record-not-found";
            }
        }
        return "core/customer/transaction-history";
    }
}