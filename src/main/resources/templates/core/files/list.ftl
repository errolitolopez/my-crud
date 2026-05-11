<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Crud | Files</title>

    <#include "*/component/css.ftl">
</head>
<body>
<#include "*/component/navbar.ftl">

<div class="container py-5">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/">Home</a></li>
            <li class="breadcrumb-item active" aria-current="page">Files</li>
        </ol>
    </nav>

    <div class="row mb-3">
        <div class="col-12 d-flex justify-content-between align-items-center">
            <h1 class="display-5 fw-bold">Files</h1>
            <a href="/files/upload" class="btn btn-primary">Upload</a>
        </div>
    </div>
    <div class="row mb-3">
        <div class="col-12">
            <form method="get" action="/files">
                <div class="input-group">
                    <input
                            type="text"
                            name="name"
                            class="form-control"
                            placeholder="Search by name..."
                            value="${name!''}"
                    />
                    <button class="btn btn-outline-secondary" type="submit">Search</button>
                    <#if name?has_content>
                        <a href="/files" class="btn btn-outline-danger">Clear</a>
                    </#if>
                </div>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col-12">
            <ul class="list-group list-group-flush">
                <#list page.content as file>
                    <li class="list-group-item d-flex justify-content-between align-items-center"
                        id="file-row-${file.id}">
                        <div>
                            <h6 class="mb-1">${file.name}</h6>
                            <small class="text-muted">${file.name}</small>
                        </div>
                        <div>
                            <a href="/files/view?id=${file.id}"speak_english_like_a_star_learning_
                               class="btn btn-sm btn-outline-primary me-1">View</a>
                            <button
                                    class="btn btn-sm btn-outline-danger btn-delete"
                                    data-id="${file.id}"
                                    data-name="${file.name}"
                            >Delete
                            </button>
                        </div>
                    </li>
                <#else>
                    <li class="list-group-item text-muted">No files found.</li>
                </#list>
            </ul>
        </div>
    </div>
    <#if !page.last>
        <div class="row mt-3">
            <div class="col-12 text-center">
                <a href="/files?name=${name!''}&size=${page.size + 10}"
                   class="btn btn-outline-secondary">
                    Show More
                </a>
            </div>
        </div>
    </#if>
</div>

<div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="deleteModalLabel">Confirm Delete</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                Are you sure you want to delete <strong id="deleteName"></strong>? This action cannot be undone.
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="button" id="confirmDeleteBtn" class="btn btn-danger">Delete</button>
            </div>
        </div>
    </div>
</div>

<div class="toast-container position-fixed bottom-0 end-0 p-3">
    <div id="toastSuccess" class="toast align-items-center text-bg-success border-0" role="alert" aria-live="assertive"
         aria-atomic="true">
        <div class="d-flex">
            <div class="toast-body">
                <i class="bi bi-check-circle-fill me-2"></i>
                File deleted successfully.
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"
                    aria-label="Close"></button>
        </div>
    </div>
    <div id="toastFailure" class="toast align-items-center text-bg-danger border-0" role="alert" aria-live="assertive"
         aria-atomic="true">
        <div class="d-flex">
            <div class="toast-body" id="toastFailureMsg">
                <i class="bi bi-x-circle-fill me-2"></i>
                Failed to delete file.
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"
                    aria-label="Close"></button>
        </div>
    </div>
</div>
</body>
<#include "*/component/js.ftl">
<#noparse>
    <script>
        const deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
        const toastSuccess = new bootstrap.Toast(document.getElementById('toastSuccess'), {delay: 3000});
        const toastFailure = new bootstrap.Toast(document.getElementById('toastFailure'), {delay: 4000});

        let pendingDeleteId = null;

        document.querySelectorAll('.btn-delete').forEach(btn => {
            btn.addEventListener('click', function () {
                pendingDeleteId = this.dataset.id;
                document.getElementById('deleteName').textContent = this.dataset.name;
                deleteModal.show();
            });
        });

        document.getElementById('confirmDeleteBtn').addEventListener('click', async function () {
            if (!pendingDeleteId) return;

            const confirmBtn = this;
            confirmBtn.disabled = true;
            confirmBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span>Deleting...';

            deleteModal.hide();

            try {
                const response = await fetch(`/api/v1/files/${pendingDeleteId}`, {
                    method: 'DELETE',
                    headers: {'Content-Type': 'application/json'}
                });

                if (response.ok) {
                    const row = document.getElementById(`file-row-${pendingDeleteId}`);
                    if (row) {
                        row.style.transition = 'opacity 0.3s';
                        row.style.opacity = '0';
                        setTimeout(() => row.remove(), 300);
                    }
                    toastSuccess.show();
                } else {
                    let msg = 'Failed to delete file.';
                    try {
                        const data = await response.json();
                        msg = data.detail || data.message || msg;
                    } catch (_) {
                    }
                    document.getElementById('toastFailureMsg').innerHTML =
                        `<i class="bi bi-x-circle-fill me-2"></i>${msg}`;
                    toastFailure.show();
                }
            } catch (_) {
                document.getElementById('toastFailureMsg').innerHTML =
                    '<i class="bi bi-x-circle-fill me-2"></i>Network error. Please try again.';
                toastFailure.show();
            } finally {
                confirmBtn.disabled = false;
                confirmBtn.innerHTML = 'Delete';
                pendingDeleteId = null;
            }
        });
    </script>
</#noparse>
</html>