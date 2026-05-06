<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Redirecting...</title>
    <#include "*/component/css.ftl">
</head>
<body>

<div class="container min-vh-100 d-flex flex-column align-items-center justify-content-center text-center">
    <div class="spinner-border text-light mb-4" role="status" style="width: 3rem; height: 3rem;">
        <span class="visually-hidden">Loading...</span>
    </div>

    <h4 class="mb-2">Redirecting you</h4>
    <p class="text-secondary mb-1">Please wait while we transfer you...</p>
    <p class="text-secondary small">You will be redirected in <span id="countdown" class="fw-bold text-white">3</span> seconds</p>
</div>
<script>
    const token = new URLSearchParams(window.location.search).get('accessToken');

    if (token) {
        let seconds = 3;
        const countdownEl = document.getElementById('countdown');

        const interval = setInterval(() => {
            seconds--;
            if (countdownEl) countdownEl.textContent = seconds.toString();

            if (seconds <= 0) {
                clearInterval(interval);
                window.location.replace('/');
            }
        }, 1000);
    } else {
        window.location.replace('/login');
    }
</script>
</body>
</html>