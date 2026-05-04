<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Crud | Page Not Found</title>

    <#include "*/component/css.ftl">
</head>
<body>
<#include "*/component/navbar.ftl">

<div class="container-fluid min-vh-100 d-flex align-items-center justify-content-center">
    <div class="text-center">
        <div class="mb-4">
            <i class="bi bi-exclamation-circle text-warning display-1"></i>
        </div>

        <h1 class="display-1 fw-bold">404</h1>

        <h2 class="h4 text-uppercase mb-3">Page Not Found</h2>

        <p class="lead text-secondary mb-5">
            The page you're looking for doesn't exist or has been moved.
        </p>

        <a href="/" class="btn btn-outline-secondary btn-lg px-5 py-3 fw-medium shadow-sm">
            <i class="bi bi-arrow-left me-2"></i>Return Home
        </a>
    </div>
</div>
</body>
</html>