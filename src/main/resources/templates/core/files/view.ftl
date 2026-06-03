<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Crud | View File</title>
    <#include "*/component/css.ftl">
    <style>
        #pdf-container {
            border-radius: 0.5rem;
            overflow-y: auto;
        }
        #pdf-container canvas {
            display: block;
            margin: 0 auto 1rem;
            border-radius: 4px;
            box-shadow: 0 2px 12px rgba(0,0,0,0.4);
            max-width: 100%;
        }
        #pdf-toolbar {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            flex-wrap: wrap;
        }
        #img-viewer img {
            max-width: 100%;
            max-height: 80vh;
            object-fit: contain;
            border-radius: 0.5rem;
            cursor: zoom-in;
        }
    </style>
</head>
<body>
<#include "*/component/navbar.ftl">

<div class="container py-5">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/">Home</a></li>
            <li class="breadcrumb-item"><a href="/files">Files</a></li>
            <li class="breadcrumb-item active" aria-current="page">View File</li>
        </ol>
    </nav>

    <div class="row mb-3">
        <div class="col-12 d-flex align-items-start justify-content-between flex-wrap gap-2">
            <div>
                <h1 class="display-5 fw-bold mb-1">${file.name}</h1>
                <span class="text-muted small">${file.url}</span>
            </div>
            <a href="${file.url}" download="${file.name}" class="btn btn-outline-secondary">
                <i class="bi bi-download me-1"></i> Download
            </a>
        </div>
    </div>

    <div class="row">
        <div class="col-12">

            <div id="pdf-viewer" style="display:none;">
                <div id="pdf-toolbar" class="mb-3">
                    <button class="btn btn-sm btn-outline-secondary" id="pdf-prev">
                        <i class="bi bi-chevron-left"></i> Prev
                    </button>
                    <span class="text-muted small">
                        Page <span id="pdf-page-num">1</span> of <span id="pdf-page-count">—</span>
                    </span>
                    <button class="btn btn-sm btn-outline-secondary" id="pdf-next">
                        Next <i class="bi bi-chevron-right"></i>
                    </button>
                    <div class="ms-auto d-flex align-items-center gap-2">
                        <button class="btn btn-sm btn-outline-secondary" id="pdf-zoom-out"><i class="bi bi-zoom-out"></i></button>
                        <span class="text-muted small" id="pdf-zoom-label">100%</span>
                        <button class="btn btn-sm btn-outline-secondary" id="pdf-zoom-in"><i class="bi bi-zoom-in"></i></button>
                    </div>
                </div>
                <div id="pdf-container"></div>
            </div>

            <div id="img-viewer" style="display:none;">
                <div class="text-center">
                    <img id="img-el" src="" alt="${file.name}"
                         data-bs-toggle="modal" data-bs-target="#imgModal">
                    <p class="text-muted small mt-2">Click image to enlarge</p>
                </div>
            </div>

            <div id="fallback-viewer" style="display:none;">
                <div class="border rounded-3 p-5 text-center">
                    <i class="bi bi-file-earmark fs-1 text-secondary"></i>
                    <p class="mt-3 mb-1 fw-medium">Preview not available</p>
                    <p class="text-muted small mb-3">This file type cannot be previewed in the browser.</p>
                    <a href="/api/v1/files/${file.id}/proxy" download="${file.name}" class="btn btn-primary">
                        <i class="bi bi-download me-1"></i> Download file
                    </a>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="modal fade" id="imgModal" tabindex="-1" aria-label="Image fullscreen" aria-hidden="true">
    <div class="modal-dialog modal-xl modal-dialog-centered">
        <div class="modal-content bg-transparent border-0">
            <div class="modal-header border-0 pb-0">
                <button type="button" class="btn-close btn-close-white ms-auto" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body text-center p-2">
                <img src="${file.url}" alt="${file.name}" style="max-width:100%; max-height:85vh; object-fit:contain; border-radius:0.5rem;">
            </div>
        </div>
    </div>
</div>

</body>
<#include "*/component/js.ftl">
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/4.4.168/pdf.min.mjs" type="module"></script>
<script type="module">
    const FILE_URL  = '/api/v1/files/${file.id}/proxy';
    const FILE_NAME = '${file.name}';
    const ext = FILE_NAME.split('.').pop().toLowerCase();

    const IMAGE_EXTS = ['jpg', 'jpeg', 'png', 'gif', 'webp', 'svg', 'bmp', 'ico'];
    const PDF_EXTS   = ['pdf'];

    if (PDF_EXTS.includes(ext)) {
        initPdf();
    } else if (IMAGE_EXTS.includes(ext)) {
        initImage();
    } else {
        document.getElementById('fallback-viewer').style.display = '';
        // auto-trigger download for truly unknown types
        const a = document.createElement('a');
        a.href = FILE_URL;
        a.download = FILE_NAME;
        a.click();
    }

    // ── PDF ──────────────────────────────────────────────────────────────────
    async function initPdf() {
        document.getElementById('pdf-viewer').style.display = '';

        const { GlobalWorkerOptions, getDocument } = await import(
            'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/4.4.168/pdf.min.mjs'
            );
        GlobalWorkerOptions.workerSrc =
            'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/4.4.168/pdf.worker.min.mjs';

        const pdf = await getDocument(FILE_URL).promise;
        const totalPages = pdf.numPages;
        document.getElementById('pdf-page-count').textContent = totalPages;

        let currentPage = 1;
        let scale = 1.5;

        const container = document.getElementById('pdf-container');

        async function renderPage(num) {
            container.innerHTML = '';
            document.getElementById('pdf-page-num').textContent = num;
            document.getElementById('pdf-zoom-label').textContent = Math.round(scale * 100) + '%';

            const page = await pdf.getPage(num);
            const viewport = page.getViewport({ scale });
            const canvas = document.createElement('canvas');
            canvas.width  = viewport.width;
            canvas.height = viewport.height;
            container.appendChild(canvas);
            await page.render({ canvasContext: canvas.getContext('2d'), viewport }).promise;
        }

        await renderPage(currentPage);

        document.getElementById('pdf-prev').addEventListener('click', async () => {
            if (currentPage > 1) { currentPage--; await renderPage(currentPage); }
        });
        document.getElementById('pdf-next').addEventListener('click', async () => {
            if (currentPage < totalPages) { currentPage++; await renderPage(currentPage); }
        });
        document.getElementById('pdf-zoom-in').addEventListener('click', async () => {
            if (scale < 3) { scale = Math.min(scale + 0.25, 3); await renderPage(currentPage); }
        });
        document.getElementById('pdf-zoom-out').addEventListener('click', async () => {
            if (scale > 0.5) { scale = Math.max(scale - 0.25, 0.5); await renderPage(currentPage); }
        });
    }

    // ── Image ─────────────────────────────────────────────────────────────────
    function initImage() {
        document.getElementById('img-viewer').style.display = '';
        document.getElementById('img-el').src = FILE_URL;
    }
</script>
</html>