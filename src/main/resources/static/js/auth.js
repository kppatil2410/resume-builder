// ===== API Helper =====
async function api(url, method = 'GET', body = null) {
    const token = localStorage.getItem('token');
    const headers = { 'Content-Type': 'application/json' };
    if (token) headers['Authorization'] = `Bearer ${token}`;

    const options = { method, headers };
    if (body) options.body = JSON.stringify(body);

    const res = await fetch(url, options);
    if (res.status === 401) {
        localStorage.clear();
        window.location.href = '/login.html';
        return;
    }
    return res.json();
}

// ===== Auth Guard =====
function requireAuth() {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = '/login.html';
        return false;
    }
    return true;
}

// ===== Logout =====
function logout() {
    localStorage.clear();
    window.location.href = '/login.html';
}

// ===== Alert Helper =====
function showAlert(message, type = 'error') {
    const alert = document.getElementById('alert');
    if (!alert) return;
    alert.textContent = message;
    alert.className = `alert ${type}`;
    alert.classList.remove('hidden');
    setTimeout(() => alert.classList.add('hidden'), 5000);
}

// ===== Modal Helpers =====
function openModal(id) {
    document.getElementById(id).classList.remove('hidden');
    document.body.style.overflow = 'hidden';
}

function closeModal(id) {
    document.getElementById(id).classList.add('hidden');
    document.body.style.overflow = '';
}

// ===== Date Formatter =====
function formatDate(dateStr) {
    if (!dateStr) return '';
    const d = new Date(dateStr);
    return d.toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
}

// ===== Score Color =====
function scoreClass(score) {
    if (score >= 75) return 'score-high';
    if (score >= 50) return 'score-mid';
    return 'score-low';
}

// Redirect if already logged in (for auth pages)
if (window.location.pathname === '/login.html' || window.location.pathname === '/register.html') {
    if (localStorage.getItem('token')) {
        window.location.href = '/dashboard.html';
    }
}
