<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Crud | Fund Transfer</title>

    <#include "*/component/css.ftl">
</head>
<body>
<#include "*/component/navbar.ftl">

<div class="container py-5">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="/">Home</a></li>
            <li class="breadcrumb-item active" aria-current="page">Fund Transfer</li>
        </ol>
    </nav>

    <div class="row mb-3">
        <div class="col-12 d-flex align-items-center gap-3">
            <h1 class="display-5 fw-bold mb-0">Fund Transfer</h1>
            <span id="connectionStatus" class="badge bg-warning text-dark">
                <span class="spinner-grow spinner-grow-sm" role="status"></span> Connecting…
            </span>
        </div>
    </div>

    <div class="row mb-2">
        <div class="col-12">
            <p class="text-muted mb-0"><strong>Origin Account</strong></p>
        </div>
    </div>

    <div class="row mb-3">
        <div class="col-12">
            <h3 class="mb-1"><strong>${account.customer.fullName}</strong></h3>
            <p class="text-muted mb-0"><strong>${account.accountType}</strong></p>
            <h1 class="mb-0"><strong>${account.accountNumber}</strong></h1>
            <p class="text-muted mb-0"><strong>Account Balance</strong></p>
            <h1 class="mb-0"><strong>₱ ${account.accountBalance}</strong></h1>
        </div>
    </div>
    <hr>

    <div class="row mb-2">
        <div class="col-12">
            <p class="text-muted mb-0"><strong>Destination Account</strong></p>
        </div>
    </div>

    <div class="row mb-3">
        <div class="col-12">

            <form id="fundTransferForm" class="needs-validation" novalidate>
                <input type="hidden" id="originAccountNumber" value="${account.accountNumber}">
                <div class="mb-3">
                    <label for="amount" class="form-label">Amount</label>
                    <div class="input-group">
                        <span class="input-group-text">₱</span>
                        <input
                                type="text"
                                id="amount"
                                class="form-control"
                                value=""
                                placeholder="0.00"
                                required
                        />
                    </div>
                    <div id="amount-error" class="invalid-feedback">Please enter amount.</div>
                </div>

                <div class="mb-3">
                    <label for="destinationAccountNumber" class="form-label">Destination Account</label>
                    <select id="destinationAccountNumber" class="form-select">
                        <option selected value="">Select Destination Account</option>
                        <#list accounts as account>
                            <option value="${account.accountNumber}">${account.accountNumber}
                                - ${account.customer.fullName}</option>
                        </#list>
                    </select>
                </div>

                <div class="ms-auto d-flex gap-2">
                    <button id="send-btn" class="btn btn-sm bg-primary-subtle border-primary-subtle" type="button">
                        <i class="bi bi-send-fill"></i> Send
                    </button>
                </div>
            </form>

        </div>
    </div>

    <div id="eventLog" class="mt-3"></div>
</div>

</body>
<#include "*/component/js.ftl">
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
<#noparse>
    <script>
        let stompClient = null;
        let activeSubscription = null;
        const logEl = document.getElementById("eventLog");
        const statusEl = document.getElementById("connectionStatus");
        const btn = document.getElementById("send-btn");

        function connect() {
            const socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);
            stompClient.debug = null;

            stompClient.connect({}, () => {
                statusEl.className = 'badge bg-success';
                statusEl.innerHTML = '&#11044; Live Updates Ready';
            }, () => {
                statusEl.className = 'badge bg-danger';
                statusEl.innerHTML = '&#11044; Disconnected';
                setTimeout(connect, 5000);
            });
        }

        async function initiateTransfer() {
            const amountVal = document.getElementById("amount").value.trim();
            const destVal = document.getElementById("destinationAccountNumber").value;
            const originEl = document.getElementById("originAccountNumber");
            const originVal = originEl ? originEl.value : "";

            // Validate amount
            if (!amountVal || isNaN(amountVal) || Number(amountVal) <= 0) {
                document.getElementById("amount").classList.add("is-invalid");
                return;
            }
            document.getElementById("amount").classList.remove("is-invalid");

            if (!destVal) {
                addLog("warning", "Validation", "Please select a destination account.");
                return;
            }

            btn.disabled = true;
            btn.innerHTML = '<span class="spinner-border spinner-border-sm"></span> Sending…';

            const payload = {
                amount: amountVal,
                originAccountNumber: originVal,
                destinationAccountNumber: destVal
            };

            try {
                const response = await fetch('/api/v1/fund-transfers', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify(payload)
                });

                const result = await response.json();
                const refId = result.data?.referenceId;

                if (response.ok && refId) {
                    addLog("info", "Request Accepted", `Reference ID: ${refId}`);
                    btn.innerHTML = '<span class="spinner-grow spinner-grow-sm"></span> Processing…';
                    subscribeToRef(refId);
                } else {
                    addLog("danger", "Failed", result.detail || "Validation Error");
                    resetBtn();
                }
            } catch (error) {
                addLog("danger", "Error", "Network error occurred.");
                resetBtn();
            }
        }

        function subscribeToRef(refId) {
            if (activeSubscription) activeSubscription.unsubscribe();

            activeSubscription = stompClient.subscribe(`/topic/transfer-updates/${refId}`, (msg) => {
                handleEvent(JSON.parse(msg.body));
            });
        }

        function handleEvent(response) {
            const status = response.status;
            let alertType = "info";
            if (status === "SUCCESS") alertType = "success";
            else if (status.includes("NOT_FOUND") || status === "INSUFFICIENT_FUNDS") alertType = "danger";

            addLog(alertType, response.statusMessage, response.message);
            resetBtn();

            if (activeSubscription) {
                activeSubscription.unsubscribe();
                activeSubscription = null;
            }
        }

        function addLog(type, title, message) {
            const div = document.createElement("div");
            div.className = `alert alert-${type} mb-2`;
            div.style.animation = "fadeIn 0.25s ease";
            div.innerHTML = `
            <strong>${title}</strong><br/>
            <small>${message}</small>
            <div class="text-end" style="font-size:0.7rem;opacity:0.6;">${new Date().toLocaleTimeString()}</div>
        `;
            logEl.prepend(div);
        }

        function resetBtn() {
            btn.disabled = false;
            btn.innerHTML = '<i class="bi bi-send-fill"></i> Send';
        }

        // Wire up the send button
        btn.addEventListener("click", initiateTransfer);

        connect();
    </script>
</#noparse>
</html>