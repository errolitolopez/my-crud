<nav class="navbar navbar-expand-lg border-bottom">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="/">Spring AI Chat</a>
        <div class="ms-auto d-flex gap-2">
            <button class="btn border-0" id="themeToggle">
                <i class="bi bi-sun-fill" id="themeIcon"></i>
            </button>
        </div>
    </div>
</nav>

<#noparse>
    <script>
        document.addEventListener('DOMContentLoaded', () => {
            const html = document.documentElement;
            const themeToggle = document.getElementById('themeToggle');
            const themeIcon = document.getElementById('themeIcon');

            const updateIcon = (theme) => {
                themeIcon.className = theme === 'dark' ? 'bi bi-sun-fill' : 'bi bi-moon-fill';
            };

            themeToggle.addEventListener('click', () => {
                const currentTheme = html.getAttribute('data-bs-theme');
                const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
                html.setAttribute('data-bs-theme', newTheme);
                localStorage.setItem('theme', newTheme);
                updateIcon(newTheme);

                updateHljsTheme(newTheme);
                const bubbles = document.querySelectorAll('.chat-bubble');
                bubbles.forEach(b => {
                    if (newTheme === 'dark') {
                        b.classList.replace('bg-light', 'bg-dark');
                        b.classList.replace('text-dark', 'text-white');
                    } else {
                        b.classList.replace('bg-dark', 'bg-light');
                        b.classList.replace('text-white', 'text-dark');
                    }
                });
            });

            const savedTheme = localStorage.getItem('theme') || 'dark';
            html.setAttribute('data-bs-theme', savedTheme);
            updateIcon(savedTheme);
        });


        function updateHljsTheme(theme) {
            const link = document.getElementById('hljsTheme');
            if (theme === 'light') {
                link.href = 'https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/styles/github.min.css';
            } else {
                link.href = 'https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/styles/github-dark.min.css';
            }
        }
    </script>
</#noparse>