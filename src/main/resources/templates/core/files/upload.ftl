<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Crud | Upload File</title>
    <#include "*/component/css.ftl">
</head>
<body>
<#include "*/component/navbar.ftl">

<div class="container py-5">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/">Home</a></li>
            <li class="breadcrumb-item"><a href="/files">Files</a></li>
            <li class="breadcrumb-item active" aria-current="page">File Upload</li>
        </ol>
    </nav>

    <div class="row mb-3">
        <div class="col-12">
            <h1 class="display-5 fw-bold">Upload File</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-12 col-md-5">
            <form id="fileForm" class="needs-validation" novalidate>

                <div class="mb-3">
                    <label for="file" class="form-label"></label>
                    <div id="dropZone" class="border border-2 rounded-3 p-4 text-center"
                         style="cursor:pointer; border-style:dashed!important; transition:.2s;"
                         onclick="document.getElementById('file').click()">
                        <i class="bi bi-cloud-upload fs-2 text-secondary"></i>
                        <p class="mt-2 mb-1 fw-medium">Drag &amp; drop a file here</p>
                        <p class="text-muted small mb-0">or click to browse · max 10 MB</p>
                        <input type="file" id="file" name="file" style="display:none" required>
                    </div>
                    <div id="filePreview" class="d-none d-flex align-items-center gap-2 border rounded p-2 mt-2 bg-body">
                        <i class="bi bi-file-earmark fs-5 text-secondary flex-shrink-0" id="fileIcon"></i>
                        <div class="flex-grow-1 overflow-hidden">
                            <div class="d-flex justify-content-between gap-2">
                                <span class="text-truncate small fw-medium" id="fileName"></span>
                                <span class="text-muted" style="font-size:12px;white-space:nowrap" id="fileSize"></span>
                            </div>
                        </div>
                        <button type="button" class="btn btn-sm p-0 text-muted" id="removeBtn" title="Remove">
                            <i class="bi bi-x-lg" style="font-size:14px"></i>
                        </button>
                    </div>
                    <div id="file-error" class="invalid-feedback" style="display:none;">Please select a file.</div>
                </div>

                <div id="error-msg" class="text-danger mb-3 d-none"></div>

                <div class="d-flex gap-2">
                    <button type="submit" id="saveBtn" class="btn btn-primary">Upload</button>
                    <a href="/files" class="btn btn-outline-secondary">Cancel</a>
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
                File uploaded successfully! Redirecting...
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
</div>

</body>
<#include "*/component/js.ftl">
<script>
    const toastEl = document.getElementById('successToast');
    const toast = new bootstrap.Toast(toastEl, { delay: 3000 });

    const dropZone   = document.getElementById('dropZone');
    const fileInput  = document.getElementById('file');
    const filePreview = document.getElementById('filePreview');
    const fileIcon   = document.getElementById('fileIcon');
    const fileName   = document.getElementById('fileName');
    const fileSize   = document.getElementById('fileSize');
    const removeBtn  = document.getElementById('removeBtn');
    const fileError  = document.getElementById('file-error');

    const iconMap = {
        pdf: 'bi-file-earmark-pdf', doc: 'bi-file-earmark-word', docx: 'bi-file-earmark-word',
        xls: 'bi-file-earmark-excel', xlsx: 'bi-file-earmark-excel',
        png: 'bi-file-earmark-image', jpg: 'bi-file-earmark-image', jpeg: 'bi-file-earmark-image',
        zip: 'bi-file-earmark-zip', txt: 'bi-file-earmark-text', csv: 'bi-file-earmark-spreadsheet'
    };

    function fmt(b) {
        return b < 1024 ? b + ' B' : b < 1048576 ? (b / 1024).toFixed(1) + ' KB' : (b / 1048576).toFixed(1) + ' MB';
    }

    function setFile(f) {
        if (f.size > 10 * 1024 * 1024) {
            fileError.textContent = 'File exceeds the 10 MB limit.';
            fileError.style.display = 'block';
            return;
        }
        const ext = f.name.split('.').pop().toLowerCase();
        fileIcon.className = 'bi ' + (iconMap[ext] || 'bi-file-earmark') + ' fs-5 text-secondary flex-shrink-0';
        fileName.textContent = f.name;
        fileSize.textContent = fmt(f.size);
        filePreview.classList.remove('d-none');
        dropZone.style.display = 'none';
        fileError.style.display = 'none';
    }

    function clearFile() {
        fileInput.value = '';
        filePreview.classList.add('d-none');
        dropZone.style.display = '';
    }

    fileInput.addEventListener('change', e => { if (e.target.files[0]) setFile(e.target.files[0]); });
    removeBtn.addEventListener('click', clearFile);
    dropZone.addEventListener('dragover',  e => { e.preventDefault(); dropZone.classList.add('bg-primary-subtle'); });
    dropZone.addEventListener('dragleave', () => dropZone.classList.remove('bg-primary-subtle'));
    dropZone.addEventListener('drop', e => {
        e.preventDefault();
        dropZone.classList.remove('bg-primary-subtle');
        if (e.dataTransfer.files[0]) {
            setFile(e.dataTransfer.files[0]);
            // transfer to input for FormData
            const dt = new DataTransfer();
            dt.items.add(e.dataTransfer.files[0]);
            fileInput.files = dt.files;
        }
    });

    document.getElementById('fileForm').addEventListener('submit', async function (e) {
        e.preventDefault();
        e.stopPropagation();

        const form = this;
        const errorMsg = document.getElementById('error-msg');
        const saveBtn = document.getElementById('saveBtn');

        // clear previous errors
        errorMsg.classList.add('d-none');
        errorMsg.textContent = '';
        fileError.style.display = 'none';

        // validate file
        if (!fileInput.files || fileInput.files.length === 0) {
            fileError.style.display = 'block';
            return;
        }

        saveBtn.disabled = true;
        saveBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span>Uploading...';

        const fd = new FormData();
        fd.append('file', fileInput.files[0]);

        try {
            const response = await fetch('/api/v1/files', {
                method: 'POST',
                body: fd
            });

            <#noparse>
            if (!response.ok) {
                const data = await response.json();
                if (data.invalidParams && data.invalidParams.length > 0) {
                    data.invalidParams.forEach(({name, message}) => {
                        if (name === 'file') {
                            fileError.textContent = message;
                            fileError.style.display = 'block';
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
                saveBtn.innerHTML = 'Upload';
                return;
            }
            </#noparse>

            toast.show();
            setTimeout(() => { window.location.href = '/files'; }, 3000);

        } catch (err) {
            errorMsg.textContent = 'Network error. Please try again.';
            errorMsg.classList.remove('d-none');
            saveBtn.disabled = false;
            saveBtn.innerHTML = 'Upload';
        }
    });
</script>
</html>