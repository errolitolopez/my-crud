<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Crud | Login</title>

    <#include "*/component/css.ftl">
</head>
<body>
<#include "*/component/navbar.ftl">

<div class="container-fluid d-flex align-items-center justify-content-center py-4">
    <div class="card border text-center p-4" style="width: 100%; max-width: 400px;">
        <div class="card-body">
            <h1 class="card-title fs-4 fw-medium mb-1">Welcome back</h1>
            <p class="text-secondary mb-4">Sign in to your account</p>

            <#if error??>
                <div class="alert alert-danger py-2 small" role="alert">
                    Sign-in failed. Please try again.
                </div>
            </#if>

            <ul class="list-group">
                <a href="/oauth2/authorization/google" class="list-group-item list-group-item-action">
                    <i class="bi bi-google"></i>
                    Continue with Google
                </a>

                <a href="/oauth2/authorization/facebook" class="list-group-item list-group-item-action">
                    <i class="bi bi-facebook"></i>
                    Continue with Facebook
                </a>
            </ul>

            <hr class="border-secondary my-4">

            <p class="text-secondary small mb-0">More sign-in options coming soon</p>
        </div>
    </div>
</div>
</body>
</html>