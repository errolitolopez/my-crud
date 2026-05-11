<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Crud | Home</title>

    <#include "*/component/css.ftl">
</head>
<body>
<#include "*/component/navbar.ftl">

<div class="container py-5">
    <div class="row">
        <div class="col-12">
            <h1 class="display-5 fw-bold">Hello!</h1>
            <p class="text-secondary">Welcome to your home page.</p>
        </div>
    </div>

    <div class="row">
        <div class="col-12">
            <ul class="list-group">
                <a href="/spring-ai/chat" class="list-group-item list-group-item-action">Spring AI Chat</a>
                <a href="/users" class="list-group-item list-group-item-action">Crud - Users</a>
                <a href="/files" class="list-group-item list-group-item-action">AWS S3 - File Upload</a>
            </ul>
        </div>
    </div>
</div>
</body>
    <#include "*/component/js.ftl">
</html>