<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Crud | Transaction History</title>

    <#include "*/component/css.ftl">
</head>
<body>
<#include "*/component/navbar.ftl">

<div class="container py-5">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/">Home</a></li>
            <li class="breadcrumb-item"><a href="/customers">Customers</a></li>
            <li class="breadcrumb-item"><a href="/customers/accounts?id=${id}">Accounts </a></li>
            <li class="breadcrumb-item active" aria-current="page">Transaction History</li>
        </ol>
    </nav>

    <div class="row mb-3">
        <div class="col-12 d-flex justify-content-between align-items-center">
            <h1 class="display-5 fw-bold">Transaction History</h1>
        </div>
    </div>

    <#if fundTransferAudits?has_content>
        <table class="table">
            <thead>
            <tr>
                <th scope="col">Reference No.</th>
                <th scope="col">Amount</th>
                <th scope="col">Origin</th>
                <th scope="col">Destination</th>
                <th scope="col">Date</th>
                <th scope="col">Type</th>
                <th scope="col">Status</th>
            </tr>
            </thead>
            <tbody>
            <#list fundTransferAudits as fundTransferAudit>
                <tr>
                    <td>${fundTransferAudit.referenceId}</td>
                    <td>₱ ${fundTransferAudit.amount}</td>
                    <td>${fundTransferAudit.originAccountNumber}</td>
                    <td>${fundTransferAudit.destinationAccountNumber}</td>
                    <td>${fundTransferAudit.createdDate}</td>
                    <td>${fundTransferAudit.auditType}</td>
                    <td>${fundTransferAudit.auditStatus}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    <#else>
        <small class="text-muted">No transaction history</small>
    </#if>
</div>

</body>
<#include "*/component/js.ftl">
<#noparse>
</#noparse>
</html>