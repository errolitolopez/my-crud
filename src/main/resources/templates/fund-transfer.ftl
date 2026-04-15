<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Fund Transfer</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
    <style>
        .glass-card {
            background: rgba(255, 255, 255, 0.7);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.3);
            border-radius: 15px;
        }
        .status-badge {
            transition: all 0.3s ease;
        }
    </style>
</head>
<body class="bg-light">

<div class="container py-5" style="max-width: 550px;">
    <div class="glass-card p-4 shadow-sm">
        <h3 class="mb-4 text-primary fw-bold">Transfer Funds</h3>

        <div id="connectionStatus" class="small mb-3 text-muted">
            <span class="spinner-grow spinner-grow-sm text-warning" role="status"></span>
            Connecting to event stream...
        </div>

        <div class="row g-3">
            <div class="col-12">
                <div class="form-floating">
                    <input type="text" class="form-control" id="origin" placeholder="Origin Account" value="100054724248">
                    <label for="origin">Origin Account</label>
                </div>
            </div>

            <div class="col-12">
                <div class="form-floating">
                    <input type="text" class="form-control" id="destination" placeholder="Destination Account" value="100080952227">
                    <label for="destination">Destination Account</label>
                </div>
            </div>

            <div class="col-12">
                <div class="form-floating">
                    <input type="number" class="form-control" id="amount" placeholder="Amount" step="0.0001" value="10.00">
                    <label for="amount">Amount (PHP)</label>
                </div>
            </div>

            <div class="col-12 pt-2">
                <button id="transferBtn" onclick="initiateTransfer()" class="btn btn-primary w-100 py-3 fw-bold shadow-sm">
                    Transfer Now
                </button>
            </div>
        </div>
    </div>

    <div id="eventLog" class="mt-4"></div>
</div>

<#noparse>
    <script>
        let stompClient = null;
        let activeSubscription = null; // Track current sub to avoid duplicates
        const logContainer = document.getElementById("eventLog");
        const statusEl = document.getElementById("connectionStatus");
        const btn = document.getElementById("transferBtn");

        function connect() {
            const socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);
            stompClient.debug = null;

            stompClient.connect({}, () => {
                statusEl.innerHTML = '<span class="text-success">● Live Updates Ready</span>';
            }, (err) => {
                statusEl.innerHTML = '<span class="text-danger">● Disconnected</span>';
                setTimeout(connect, 5000);
            });
        }

        async function initiateTransfer() {
            const payload = {
                amount: document.getElementById("amount").value,
                originAccountNumber: document.getElementById("origin").value,
                destinationAccountNumber: document.getElementById("destination").value
            };

            btn.disabled = true;
            btn.innerHTML = '<span class="spinner-border spinner-border-sm"></span> Initializing...';

            try {
                const response = await fetch('/api/v1/fund-transfers', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });

                const result = await response.json();

                // Get referenceId from the response body (based on your sample response)
                const refId = result.data?.referenceId;

                if (response.ok && refId) {
                    addLog("info", "Request Accepted", `Reference ID: ${refId}`);
                    btn.innerHTML = '<span class="spinner-grow spinner-grow-sm"></span> Processing Event...';

                    // DYNAMIC SUBSCRIPTION: Listen only for this specific referenceId
                    subscribeToRef(refId);
                } else {
                    addLog("danger", "Failed", result.detail || "Validation Error");
                    btn.disabled = false;
                    btn.innerHTML = 'Transfer Now';
                }
            } catch (error) {
                addLog("danger", "Error", "Network error occurred");
                btn.disabled = false;
                btn.innerHTML = 'Transfer Now';
            }
        }

        function subscribeToRef(refId) {
            // Unsubscribe from previous if user clicks button twice
            if (activeSubscription) activeSubscription.unsubscribe();

            activeSubscription = stompClient.subscribe(`/topic/transfer-updates/${refId}`, (msg) => {
                handleEvent(JSON.parse(msg.body));
            });
        }

        function handleEvent(response) {
            console.log("Response Received:", response);

            const status = response.status;
            const msg = response.message;
            const statusMessage = response.statusMessage;

            let alertType = "info";
            if (status === "SUCCESS") alertType = "success";
            else if (status.includes("NOT_FOUND") || status === "INSUFFICIENT_FUNDS") alertType = "danger";

            addLog(alertType, `${statusMessage}`, msg);

            // UI Cleanup
            btn.disabled = false;
            btn.innerHTML = 'Transfer Now';

            if (activeSubscription) {
                activeSubscription.unsubscribe();
                activeSubscription = null;
            }
        }

        function addLog(type, title, message) {
            const div = document.createElement("div");
            div.className = `alert alert-${type} glass-card border-0 mb-2 shadow-sm animate-fade-in`;
            div.innerHTML = `
                <strong>${title}</strong><br>
                <small>${message}</small>
                <div class="text-end small text-muted" style="font-size: 0.7rem;">${new Date().toLocaleTimeString()}</div>
            `;
            logContainer.prepend(div);
        }

        connect();
    </script>
</#noparse>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>