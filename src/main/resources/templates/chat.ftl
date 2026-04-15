<!DOCTYPE html>
<html lang="en">
<head>
    <title>Chat App</title>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background: #f5f5f5;
        }

        #chatContainer {
            position: relative;
            border: 1px solid #ccc;
            border-radius: 6px;
            background: white;
        }

        #chatBox {
            height: 320px;
            overflow-y: auto;
            padding: 10px;
            display: flex;
            flex-direction: column;
        }

        #loadMoreBtn {
            display: none;
            margin: 5px auto;
            padding: 5px 15px;
            font-size: 0.75rem;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 20px;
            cursor: pointer;
        }

        #loadMoreBtn:disabled {
            background: #ccc;
            cursor: not-allowed;
        }

        .msg-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
            border-bottom: 1px solid #f0f0f0;
        }

        .msg-content {
            word-break: break-word;
            max-width: 75%;
        }

        .room-badge {
            font-size: 0.65rem;
            background: #e0e0e0;
            padding: 2px 5px;
            border-radius: 4px;
            margin-right: 5px;
            color: #555;
        }

        .timestamp {
            font-size: 0.75rem;
            color: #888;
        }

        .row {
            margin-top: 10px;
        }

        input, textarea {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            width: 100%;
            box-sizing: border-box;
        }

        button#sendBtn {
            padding: 10px 15px;
            border: none;
            border-radius: 5px;
            background: #007bff;
            color: white;
            cursor: pointer;
        }
    </style>
</head>
<body>

<h2>Simple Chat</h2>
<div id="chatContainer">
    <button id="loadMoreBtn" onclick="loadMore()">Load Previous Messages</button>
    <div id="chatBox"></div>
</div>

<div class="row"><input id="roomId" placeholder="Room" value="general"/></div>
<div class="row"><input id="sender" placeholder="Sender"/></div>
<div class="row"><textarea id="message" placeholder="Message"></textarea></div>
<div class="row">
    <button id="sendBtn" onclick="sendMsg()">Send</button>
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

                    loadMoreBtn.style.display = hasMore ? "block" : "none";

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
            div.className = "msg-row";
            div.innerHTML = `
            <span class="msg-content">
                <span class="room-badge">${data.roomId}</span>
                <strong>${data.sender}:</strong> ${data.content}
            </span>
            <span class="timestamp">${data.timestamp ? new Date(data.timestamp).toLocaleTimeString([], {
                hour: '2-digit',
                minute: '2-digit'
            }) : ''}</span>
        `;
            return div;
        }

        function sendMsg() {
            const payload = {
                roomId: roomIdEl.value.trim(),
                sender: senderEl.value.trim(),
                content: messageEl.value.trim()
            };
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
</body>
</html>