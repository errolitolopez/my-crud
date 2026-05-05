<#noparse>
    <script>
        const chatContainer = document.getElementById('chat-messages');
        const textarea = document.getElementById('message-input');
        const sendBtn = document.getElementById('send-btn');
        const stopBtn = document.getElementById('stop-btn');

        const STORAGE_KEY = 'spring_ai_conversation_ids';
        let currentId = null;
        let abortController = null;
        const convCache = {};

        const loadIds = () => JSON.parse(localStorage.getItem(STORAGE_KEY) || '[]');
        const saveIds = (ids) => localStorage.setItem(STORAGE_KEY, JSON.stringify(ids));

        window.addEventListener('DOMContentLoaded', init);

        function init() {
            renderSidebar();
            const ids = loadIds();
            if (ids.length === 0) newConversation();
            else switchConversation(ids[0]);
        }

        function renderSidebar() {
            const ids = loadIds();
            const sidebarLists = document.querySelectorAll('.conversation-list');
            const convCounts = document.querySelectorAll('.conv-count');

            convCounts.forEach(el => el.innerText = `${ids.length} conversations`);

            const html = ids.map(id => {
                const info = convCache[id] || { title: 'New Chat' };
                const activeClass = id === currentId ? 'bg-primary-subtle border-primary-subtle' : 'border-transparent';

                return `
                    <div class="list-group-item list-group-item-action d-flex justify-content-between align-items-center rounded border mb-1 small ${activeClass}"
                         style="cursor: pointer;"
                         onclick="selectConversation('${id}')">
                        <div class="text-truncate flex-grow-1">
                            <i class="bi bi-chat-left-text me-2"></i>
                            <span>${info.title}</span>
                        </div>
                        <button class="btn btn-link btn-sm p-0 text-secondary" onclick="event.stopPropagation(); deleteConversation('${id}')">
                            <i class="bi bi-x"></i>
                        </button>
                    </div>
                `;
            }).join('');

            sidebarLists.forEach(el => el.innerHTML = html);
        }

        function selectConversation(id) {
            switchConversation(id);
            // Close mobile offcanvas if open
            const offcanvasEl = document.getElementById('sidebarOffcanvas');
            const instance = bootstrap.Offcanvas.getInstance(offcanvasEl);
            if (instance) instance.hide();
        }

        function updateConvTitle(id, text) {
            if (!convCache[id] || convCache[id].title === 'New Chat') {
                convCache[id] = { title: text.length > 25 ? text.slice(0, 25) + '...' : text };
                renderSidebar();
            }
        }

        async function newConversation() {
            const res = await fetch('/api/v1/ai/chat/conversation-id', {method: 'POST'});
            const result = await res.json();
            currentId = result.data;

            const ids = loadIds();
            if (!ids.includes(currentId)) {
                ids.unshift(currentId);
                saveIds(ids);
            }

            convCache[currentId] = { title: 'New Chat' };
            chatContainer.innerHTML = '';
            renderSidebar();
            textarea.focus();
        }

        async function switchConversation(id) {
            currentId = id;
            renderSidebar();
            chatContainer.innerHTML = '<div class="text-center py-5 text-secondary small">Loading history...</div>';

            try {
                const res = await fetch(`/api/v1/ai/chat/history/${id}`);
                const result = await res.json();
                chatContainer.innerHTML = '';

                if (result.data && result.data.length > 0) {
                    const firstUserMsg = result.data.find(m => m.type.toLowerCase() === 'user');
                    if (firstUserMsg) updateConvTitle(id, firstUserMsg.content);

                    result.data.forEach(m => {
                        const role = m.type.toLowerCase() === 'user' ? 'user' : 'ai';
                        const bubble = appendMessage(role);
                        bubble.innerHTML = role === 'user' ? m.content : renderMarkdown(m.content);
                    });
                } else {
                    chatContainer.innerHTML = '<div class="text-center py-5 text-secondary small">No messages yet.</div>';
                }
            } catch (e) {
                chatContainer.innerHTML = '<div class="p-3 text-danger small">Error loading history.</div>';
            }
        }

        async function deleteConversation(id) {
            await fetch(`/api/v1/ai/chat/history/${id}`, { method: 'DELETE' });
            const remaining = loadIds().filter(x => x !== id);
            saveIds(remaining);
            delete convCache[id];

            if (currentId === id) remaining.length > 0 ? await switchConversation(remaining[0]) : await newConversation();
            else renderSidebar();
        }

        function stopGeneration() {
            if (abortController) {
                abortController.abort();
                abortController = null;
            }
        }

        async function sendMessage() {
            const text = textarea.value.trim();
            if (!text || !currentId) return;

            abortController = new AbortController();
            appendMessage('user').innerText = text;
            textarea.value = '';

            updateConvTitle(currentId, text);

            sendBtn.classList.add('d-none');
            stopBtn.classList.remove('d-none');

            const aiBubble = appendMessage('ai');
            let fullContent = '';

            try {
                const res = await fetch('/api/v1/ai/chat/stream', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({
                        message: text,
                        model: document.getElementById('model-select').value,
                        conversationId: currentId
                    }),
                    signal: abortController.signal
                });

                const reader = res.body.getReader();
                const decoder = new TextDecoder();
                let buffer = '';

                while (true) {
                    const {done, value} = await reader.read();
                    if (done) break;

                    buffer += decoder.decode(value, {stream: true});
                    const events = buffer.split('\n\n');
                    buffer = events.pop();

                    for (const event of events) {
                        for (const line of event.split('\n')) {
                            if (!line.startsWith('data:')) continue;
                            const raw = line.slice(5);
                            if (raw.trim() === '[DONE]') continue;
                            fullContent += raw === '' ? '\n' : raw;
                        }
                        aiBubble.innerHTML = renderMarkdown(fullContent);
                    }
                }
            } catch (e) {
                if (e.name === 'AbortError') {
                    aiBubble.innerHTML += '<div class="text-warning mt-2 small border-top pt-1"><i>Generation stopped.</i></div>';
                }
            } finally {
                sendBtn.classList.remove('d-none');
                stopBtn.classList.add('d-none');
                abortController = null;
            }
        }

        function appendMessage(role) {
            const isDark = document.documentElement.getAttribute('data-bs-theme') === 'dark';
            const wrapper = document.createElement('div');
            wrapper.className = `d-flex mb-3 ${role === 'user' ? 'justify-content-end' : 'justify-content-start'}`;

            const bubble = document.createElement('div');
            const colorClass = isDark ? 'bg-dark text-white border-secondary' : 'bg-light text-dark border-light-subtle';
            bubble.className = `chat-bubble px-3 py-2 rounded border ${colorClass}`;
            bubble.style.maxWidth = '85%';

            wrapper.appendChild(bubble);
            chatContainer.appendChild(wrapper);
            chatContainer.scrollTop = chatContainer.scrollHeight;
            return bubble;
        }

        function renderMarkdown(raw) {
            return marked.parse(raw.replace(/\n{4,}/g, '\n\n\n'));
        }

        textarea.addEventListener('keydown', e => {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                sendMessage();
            }
        });
    </script>
</#noparse>