<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, interactive-widget=resizes-content">
    <title>Spring AI Chat</title>

    <#include "*/component/css.ftl">
    <#include "*/component/marked.ftl">

    <style>
        html, body {
            height: 100dvh;
            overflow: hidden;
        }

        body {
            display: flex;
            flex-direction: column;
        }

        #main-layout {
            flex: 1 1 0;
            min-height: 0;
            display: flex;
            overflow: hidden;
        }

        #input-area {
            flex-shrink: 0;
        }

        #model-select {
            min-width: 0;
            max-width: 160px;
        }

        .action-btns {
            flex-shrink: 0;
        }

        @media (max-width: 360px) {
            #model-select {
                max-width: 110px;
                font-size: 0.75rem;
            }
        }
    </style>
</head>
<body class="bg-body">

<#include "*/component/spring-ai-chat-navbar.ftl">

<!-- Mobile Toggle Header -->
<div class="d-md-none border-bottom p-2 bg-body-tertiary d-flex align-items-center flex-shrink-0">
    <button class="btn btn-sm btn-outline-secondary me-2" type="button" data-bs-toggle="offcanvas" data-bs-target="#sidebarOffcanvas">
        <i class="bi bi-layout-sidebar-inset"></i>
    </button>
    <span class="small fw-bold">Chat History</span>
</div>

<div id="main-layout">

    <!-- Desktop Sidebar (Hidden on mobile) -->
    <nav class="bg-body-tertiary border-end d-none d-md-flex flex-column overflow-hidden flex-shrink-0" style="min-width: 280px; max-width: 280px;">
        <@sidebarContent />
    </nav>

    <!-- Mobile Sidebar (Offcanvas) -->
    <div class="offcanvas offcanvas-start bg-body" tabindex="-1" id="sidebarOffcanvas" style="width: 280px;">
        <div class="offcanvas-header border-bottom">
            <h5 class="offcanvas-title">History</h5>
            <button type="button" class="btn-close" data-bs-dismiss="offcanvas"></button>
        </div>
        <div class="offcanvas-body p-0 d-flex flex-column overflow-hidden">
            <@sidebarContent />
        </div>
    </div>

    <!-- Reusable Sidebar Template -->
    <#macro sidebarContent>
        <div class="p-3 border-bottom flex-shrink-0">
            <button class="btn btn-outline-secondary btn-sm w-100" onclick="newConversation()">
                <i class="bi bi-pencil-square"></i> New Chat
            </button>
        </div>
        <div class="flex-grow-1 overflow-y-auto p-2 list-group list-group-flush conversation-list"></div>
        <div class="p-2 border-top small text-center text-secondary conv-count flex-shrink-0">0 conversations</div>
    </#macro>

    <main class="flex-grow-1 d-flex flex-column bg-body overflow-hidden">
        <div class="flex-grow-1 overflow-y-auto px-3 py-4" id="chat-messages"></div>

        <div id="input-area" class="border-top bg-body-tertiary py-3 px-3">
            <div class="container-md">
                <div class="mb-2">
                    <textarea
                            class="form-control"
                            id="message-input"
                            placeholder="Ask anything"
                            rows="1"
                            style="resize: none; overflow-y: hidden;"
                    ></textarea>
                </div>

                <div class="d-flex align-items-center gap-2 flex-wrap">
                    <select id="model-select" class="form-select form-select-sm">
                        <#list models?keys as key>
                            <option value="${models[key]}">${key}</option>
                        </#list>
                    </select>

                    <div class="ms-auto d-flex gap-2 action-btns">
                        <button id="stop-btn" class="btn btn-sm bg-danger-subtle border-danger-subtle d-none" type="button" onclick="stopGeneration()">
                            <i class="bi bi-stop-fill"></i> Stop
                        </button>
                        <button id="send-btn" class="btn btn-sm bg-primary-subtle border-primary-subtle" type="button" onclick="sendMessage()">
                            <i class="bi bi-send-fill"></i> Send
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<#include "*/component/js.ftl">
<#include "*/component/spring-ai-chat-js.ftl">
</body>
</html>