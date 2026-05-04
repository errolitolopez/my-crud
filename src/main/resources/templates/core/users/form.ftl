<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Crud | Users Form</title>

    <#include "*/component/css.ftl">
</head>
<body>
<#include "*/component/navbar.ftl">

<div class="container py-5">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/">Home</a></li>
            <li class="breadcrumb-item"><a href="/users">Users</a></li>
            <li class="breadcrumb-item active" aria-current="page">Form</li>
        </ol>
    </nav>

    <div class="row mb-3">
        <div class="col-12">
            <#if mode == 'create'>
                <h1 class="display-5 fw-bold">New User</h1>
            <#else>
                <h1 class="display-5 fw-bold">Edit User</h1>
            </#if>
        </div>
    </div>

    <div class="row">
        <div class="col-12 col-md-5">
            <form id="userForm" class="needs-validation" novalidate>
                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input
                            type="text"
                            id="username"
                            class="form-control"
                            value="${(user.username)!''}"
                            placeholder="Username"
                            required
                    />
                    <div id="username-error" class="invalid-feedback">Please enter a username.</div>
                </div>
                <div class="mb-3">
                    <label for="fullName" class="form-label">Full Name</label>
                    <input
                            type="text"
                            id="fullName"
                            class="form-control"
                            value="${(user.getUserProfile().fullName)!''}"
                            placeholder="Full Name"
                            required
                    />
                    <div id="fullName-error" class="invalid-feedback">Please enter a full name.</div>
                </div>
                <div id="error-msg" class="text-danger mb-3 d-none"></div>
                <div class="d-flex gap-2">
                    <button type="submit" id="saveBtn" class="btn btn-primary">Save</button>
                    <a href="/users" class="btn btn-outline-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="toast-container position-fixed bottom-0 end-0 p-3">
    <div id="successToast" class="toast align-items-center text-bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
            <div class="toast-body">
                <i class="bi bi-check-circle-fill me-2"></i>
                User saved successfully! Redirecting...
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
</div>
</body>
<#include "*/component/js.ftl">
<script>
    const mode = '${mode}';

    const toastEl = document.getElementById('successToast');
    const toast = new bootstrap.Toast(toastEl, { delay: 3000 });

    function clearServerErrors() {
        document.querySelectorAll('.form-control').forEach(el => el.classList.remove('is-invalid'));
        document.querySelectorAll('.invalid-feedback').forEach(el => {
            const defaults = {
                'username-error': 'Please enter a username.',
                'fullName-error': 'Please enter a full name.'
            };
            if (defaults[el.id]) el.textContent = defaults[el.id];
        });
        const errorMsg = document.getElementById('error-msg');
        errorMsg.classList.add('d-none');
        errorMsg.textContent = '';
    }

    document.getElementById('userForm').addEventListener('submit', async function (e) {
        e.preventDefault();
        e.stopPropagation();

        const form = this;
        const errorMsg = document.getElementById('error-msg');
        const saveBtn = document.getElementById('saveBtn');

        clearServerErrors();
        form.classList.add('was-validated');
        if (!form.checkValidity()) return;

        saveBtn.disabled = true;
        saveBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span>Saving...';

        const username = document.getElementById('username').value.trim();
        const fullName = document.getElementById('fullName').value.trim();

        const isCreate = mode === 'create';
        const url = isCreate ? '/api/v1/users' : `/api/v1/users/${(user.id)!''}`;
        const method = isCreate ? 'POST' : 'PUT';

        try {
            const response = await fetch(url, {
                method,
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({username, fullName})
            });

            <#noparse>
            if (!response.ok) {
                const data = await response.json();
                form.classList.remove('was-validated');
                if (data.invalidParams && data.invalidParams.length > 0) {
                    data.invalidParams.forEach(({name, message}) => {
                        const input = document.getElementById(name);
                        const feedback = document.getElementById(`${name}-error`);
                        if (input && feedback) {
                            input.classList.add('is-invalid');
                            feedback.textContent = message;
                        } else {
                            errorMsg.textContent += message + ' ';
                            errorMsg.classList.remove('d-none');
                        }
                    });
                } else {
                    errorMsg.textContent = data.detail || data.message || 'Something went wrong.';
                    errorMsg.classList.remove('d-none');
                }
                saveBtn.disabled = false;
                saveBtn.innerHTML = 'Save';
                return;
            }
            </#noparse>

            toast.show();
            setTimeout(() => { window.location.href = '/users'; }, 3000);

        } catch (e) {
            errorMsg.textContent = 'Network error. Please try again.';
            errorMsg.classList.remove('d-none');
            saveBtn.disabled = false;
            saveBtn.innerHTML = 'Save';
        }
    });
</script>
</html>