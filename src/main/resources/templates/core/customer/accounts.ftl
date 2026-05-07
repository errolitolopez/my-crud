<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Crud | Accounts</title>

    <#include "*/component/css.ftl">
</head>
<body>
<#include "*/component/navbar.ftl">

<div class="container py-5">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/">Home</a></li>
            <li class="breadcrumb-item"><a href="/customers">Customers</a></li>
            <li class="breadcrumb-item active" aria-current="page">Accounts</li>
        </ol>
    </nav>

    <div class="row mb-3">
        <div class="col-12 d-flex justify-content-between align-items-center">
            <h1 class="display-5 fw-bold">Accounts</h1>
        </div>

        <#if accounts?has_content>
            <div class="accordion" id="accountsAccordion">
                <#list accounts as account>
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button <#if account?index != 0>collapsed</#if>"
                                    type="button"
                                    data-bs-toggle="collapse"
                                    data-bs-target="#account-${account?index}"
                                    aria-expanded="${(account?index == 0)?string('true', 'false')}"
                                    aria-controls="account-${account?index}">
                                ${account.accountType} - ${account.accountNumber} - ${account.accountBalance}
                            </button>
                        </h2>
                        <div id="account-${account?index}"
                             class="accordion-collapse collapse <#if account?index == 0>show</#if>"
                             data-bs-parent="#accountsAccordion">
                            <div class="accordion-body">
                                <p class="text-muted mb-0"><strong>${account.accountType}</strong></p>
                                <h1 class="text-muted mb-0"><strong>${account.accountNumber}</strong></h1>
                                <p class="text-muted mb-0"><strong>Account Balance</strong></p>
                                <h1 class="text-muted mb-0"><strong>₱ ${account.accountBalance}</strong></h1>
                                <div class="mt-3">
                                    <a href="/fund-transfer?id=${account.id}" class="btn btn-primary">
                                        <i class="bi bi-arrow-left-right me-1"></i> Fund Transfer
                                    </a>
                                    <a href="/customers/transaction-history?id=${account.id}" class="btn btn-primary">
                                        <i class="bi bi-receipt me-1"></i> Transaction History
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </#list>
            </div>
        <#else>
            <small class="text-muted">No accounts</small>
        </#if>
    </div>
</div>

</body>
<#include "*/component/js.ftl">
<#noparse>
</#noparse>
</html>