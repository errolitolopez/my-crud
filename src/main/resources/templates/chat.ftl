<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chat App</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
</head>
<body class="bg-light">

<div class="container py-4" style="max-width: 600px;">
    <h2 class="mb-3 text-primary">Simple Chat</h2>

    <div class="card shadow-sm mb-3">
        <div class="card-header bg-white border-bottom-0 text-center">
            <button id="loadMoreBtn" onclick="loadMore()" class="btn btn-outline-primary btn-sm rounded-pill" style="display: none;">
                Load Previous Messages
            </button>
        </div>

        <div id="chatBox" class="card-body overflow-y-auto" style="height: 400px; display: flex; flex-direction: column;">
        </div>
    </div>

    <div class="card card-body shadow-sm">
        <div class="row g-2 mb-2">
            <div class="col-md-4">
                <div class="form-floating">
                    <input type="text" class="form-control" id="roomId" placeholder="Room" value="general">
                    <label for="roomId">Room</label>
                </div>
            </div>
            <div class="col-md-8">
                <div class="form-floating">
                    <input type="text" class="form-control" id="sender" placeholder="Sender">
                    <label for="sender">Your Name</label>
                </div>
            </div>
        </div>

        <div class="row g-2">
            <div class="col-12">
                <textarea id="message" class="form-control" placeholder="Type a message..." rows="3"></textarea>
            </div>
            <div class="col-12 d-flex justify-content-end">
                <button id="sendBtn" onclick="sendMsg()" class="btn btn-primary px-5 py-2 shadow-sm">
                    Send Message
                </button>
            </div>
        </div>
    </div>
</div>

<#noparse>
    <script>
        let stompClient = null;
        let currentSubscription = null;
        let currentPage = 0;
        let isFetching = false;

        const chatBox = document.getElementById("chatBox");
        const loadMoreBtn = document.getElementById("loadMoreBtn");
        const roomIdEl = document.getElementById("roomId");
        const senderEl = document.getElementById("sender");
        const messageEl = document.getElementById("message");

        async function fetchMessages(page = 0, isLoadMore = false) {
            if (isFetching) return;
            isFetching = true;
            loadMoreBtn.disabled = true;

            const room = roomIdEl.value.trim() || "general";
            const url = `http://localhost:8090/api/v1/chat-messages?roomId=${room}&page=${page}&size=10&sort=timestamp,desc`;

            try {
                const response = await fetch(url);
                const result = await response.json();

                if (result.data) {
                    const messages = result.data.content || [];
                    const pageInfo = result.data.page;
                    const hasMore = pageInfo.totalPages > (pageInfo.number + 1);

                    if (!isLoadMore) chatBox.innerHTML = "";
                    loadMoreBtn.style.display = hasMore ? "inline-block" : "none";

                    messages.reverse().forEach(msg => {
                        isLoadMore ? prependMessage(msg) : appendMessage(msg);
                    });

                    if (!isLoadMore) chatBox.scrollTop = chatBox.scrollHeight;
                }
            } catch (error) {
                console.error("Fetch error:", error);
            } finally {
                isFetching = false;
                loadMoreBtn.disabled = false;
            }
        }

        function loadMore() {
            currentPage++;
            fetchMessages(currentPage, true);
        }

        function connect() {
            const socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);
            stompClient.debug = null;
            stompClient.connect({}, () => subscribeToRoom());
        }

        function subscribeToRoom() {
            const room = roomIdEl.value.trim() || "general";
            if (currentSubscription) currentSubscription.unsubscribe();

            currentSubscription = stompClient.subscribe(`/topic/chat/${room}`, (msg) => {
                appendMessage(JSON.parse(msg.body));
                chatBox.scrollTop = chatBox.scrollHeight;
            });
        }

        function appendMessage(data) {
            chatBox.appendChild(createMsgElement(data));
        }

        function prependMessage(data) {
            chatBox.prepend(createMsgElement(data));
        }

        function createMsgElement(data) {
            const div = document.createElement("div");
            div.className = "d-flex justify-content-between align-items-start mb-2 border-bottom pb-2";
            div.innerHTML = `
                <div style="max-width: 80%;">
                    <span class="badge bg-secondary-subtle text-secondary small mb-1">${data.roomId}</span>
                    <div class="text-break">
                        <strong class="text-dark">${data.sender}:</strong>
                        <span class="text-muted">${data.content}</span>
                    </div>
                </div>
                <small class="text-body-tertiary ms-2" style="white-space: nowrap;">
                    ${data.timestamp ? new Date(data.timestamp).toLocaleTimeString([], {
                hour: '2-digit',
                minute: '2-digit'
            }) : ''}
                </small>
            `;
            return div;
        }

        function sendMsg() {
            const payload = {
                roomId: roomIdEl.value.trim(),
                sender: senderEl.value.trim(),
                content: messageEl.value.trim()
            };
            if(!payload.content) return;
            stompClient.send("/app/chat.send", {}, JSON.stringify(payload));
            messageEl.value = "";
        }

        roomIdEl.addEventListener("change", () => {
            currentPage = 0;
            fetchMessages(0, false);
            subscribeToRoom();
        });

        fetchMessages(0, false);
        connect();
    </script>
</#noparse>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>