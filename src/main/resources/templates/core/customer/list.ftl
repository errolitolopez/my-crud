<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Crud | Customers</title>

    <#include "*/component/css.ftl">
</head>
<body>
<#include "*/component/navbar.ftl">

<div class="container py-5">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/">Home</a></li>
            <li class="breadcrumb-item active" aria-current="page">Customers</li>
        </ol>
    </nav>

    <div class="row mb-3">
        <div class="col-12 d-flex justify-content-between align-items-center">
            <h1 class="display-5 fw-bold">Customers</h1>
            <a href="/customers/create/form" class="btn btn-primary">New User</a>
        </div>
    </div>
    <div class="row mb-3">
        <div class="col-12">
            <form method="get" action="/customers">
                <div class="input-group">
                    <input
                            type="text"
                            name="fullName"
                            class="form-control"
                            placeholder="Search by full name..."
                            value="${fullName!''}"
                    />
                    <button class="btn btn-outline-secondary" type="submit">Search</button>
                    <#if fullName?has_content>
                        <a href="/customers" class="btn btn-outline-danger">Clear</a>
                    </#if>
                </div>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col-12">
            <ul class="list-group list-group-flush">
                <#list page.content as customer>
                    <li class="list-group-item d-flex justify-content-between align-items-center"
                        id="customer-row-${customer.id}">
                        <div>
                            <h6 class="mb-1">${customer.fullName}</h6>
                        </div>
                        <div>
                            <a href="/customers/accounts?id=${customer.id}"
                               class="btn btn-sm btn-outline-primary me-1">Accounts</a>
                        </div>
                    </li>
                <#else>
                    <li class="list-group-item text-muted">No customers found.</li>
                </#list>
            </ul>
        </div>
    </div>
    <#if !page.last>
        <div class="row mt-3">
            <div class="col-12 text-center">
                <a href="/customers?fullName=${fullName!''}&size=${page.size + 10}"
                   class="btn btn-outline-secondary">
                    Show More
                </a>
            </div>
        </div>
    </#if>
</div>

</body>
<#include "*/component/js.ftl">
<#noparse>
</#noparse>
</html>