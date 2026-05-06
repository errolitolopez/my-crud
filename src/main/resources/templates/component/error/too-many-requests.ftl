<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Crud | Too Many Requests</title>

    <#include "*/component/css.ftl">
</head>
<body>
<#include "*/component/navbar.ftl">

<#assign reset = RequestParameters.reset!"10">
<#assign path = RequestParameters.path!"">
<#assign query = RequestParameters.query!"">
<#assign backUrl = path + (query?has_content?then("?" + query, ""))>

<div class="container-fluid min-vh-100 d-flex align-items-center justify-content-center">
    <div class="text-center">
        <div class="mb-4">
            <i class="bi bi-stopwatch text-warning display-1"></i>
        </div>

        <h1 class="display-1 fw-bold">429</h1>

        <h2 class="h4 text-uppercase mb-3">Too Many Requests</h2>

        <p class="lead text-secondary mb-3">
            😮‍💨 Take a breath... You're going too fast!
        </p>

        <div class="mb-5">
            <span class="text-secondary">Please wait </span>
            <span id="countdown" class="display-6 fw-bold text-warning">${reset}</span>
            <span class="text-secondary"> seconds before trying again.</span>
        </div>

        <a id="backBtn"
           href="#"
           class="btn btn-outline-secondary btn-lg px-5 py-3 fw-medium shadow-sm disabled"
           aria-disabled="true">
            <i class="bi bi-arrow-left me-2"></i>Go Back <span id="btnTimer">(${reset}s)</span>
        </a>
    </div>
</div>

<script>
    const backUrl = "${backUrl}";
    let seconds = parseInt("${reset}");

    const countdown = document.getElementById("countdown");
    const backBtn = document.getElementById("backBtn");
    const btnTimer = document.getElementById("btnTimer");

    const interval = setInterval(() => {
        seconds--;

        countdown.textContent = seconds;
        btnTimer.textContent = "(" + seconds + "s)";

        if (seconds <= 0) {
            clearInterval(interval);

            countdown.textContent = "0";
            btnTimer.textContent = "";

            backBtn.classList.remove("disabled");
            backBtn.removeAttribute("aria-disabled");
            backBtn.href = backUrl || "javascript:history.back()";
        }
    }, 1000);
</script>
</body>
</html>