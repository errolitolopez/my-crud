<nav class="navbar navbar-expand-lg border-bottom">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="/">My Crud</a>
        <div class="ms-auto d-flex gap-2">
            <button class="btn border-0" id="themeToggle">
                <i class="bi bi-sun-fill" id="themeIcon"></i>
            </button>

            <button class="btn border-0" id="logoutBtn" onclick="logout()">
                <i class="bi bi-box-arrow-right"></i>
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
            const logoutBtn = document.getElementById('logoutBtn');

            if (window.location.pathname === '/login') {
                logoutBtn.style.display = 'none';
            }


            const updateIcon = (theme) => {
                themeIcon.className = theme === 'dark' ? 'bi bi-sun-fill' : 'bi bi-moon-fill';
            };

            themeToggle.addEventListener('click', () => {
                const currentTheme = html.getAttribute('data-bs-theme');
                const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
                html.setAttribute('data-bs-theme', newTheme);
                localStorage.setItem('theme', newTheme);
                updateIcon(newTheme);
            });

            const savedTheme = localStorage.getItem('theme') || 'dark';
            html.setAttribute('data-bs-theme', savedTheme);
            updateIcon(savedTheme);
        });

        async function logout() {
            await fetch("/api/v1/auth/logout", { method: "POST" });
            window.location.replace("/login");
        }
    </script>
</#noparse>