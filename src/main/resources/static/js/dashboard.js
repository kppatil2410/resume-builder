// Require auth
requireAuth();

// Set user name
document.getElementById('userName').textContent = localStorage.getItem('userName') || 'User';

// Load dashboard data
async function loadDashboard() {
    try {
        const res = await api('/api/resume/history');
        if (!res || !res.success) return;

        const resumes = res.data || [];
        document.getElementById('resumeCount').textContent = resumes.length;

        if (resumes.length > 0) {
            const scores = resumes.filter(r => r.score != null).map(r => r.score);
            if (scores.length > 0) {
                const avg = Math.round(scores.reduce((a, b) => a + b, 0) / scores.length);
                const best = Math.max(...scores);
                document.getElementById('avgScore').textContent = avg;
                document.getElementById('bestScore').textContent = best;
            }

            // Recent resumes (top 5)
            const recent = resumes.slice(0, 5);
            const container = document.getElementById('recentResumes');
            container.innerHTML = recent.map(r => `
                <div class="resume-item" onclick="window.location.href='/history.html'">
                    <div>
                        <div class="resume-item-title">${escapeHtml(r.title)}</div>
                        <div class="resume-item-meta">${formatDate(r.createdAt)}</div>
                    </div>
                    ${r.score != null ? `<span class="score-chip ${scoreClass(r.score)}">${r.score}/100</span>` : ''}
                </div>
            `).join('');
        }
    } catch (e) {
        console.error('Failed to load dashboard:', e);
    }
}

// ATS Check
function openAtsModal() { openModal('atsModal'); }

async function runAtsCheck() {
    const resume = document.getElementById('atsResume').value.trim();
    const job = document.getElementById('atsJob').value.trim();

    if (!resume || !job) {
        alert('Please fill in both fields.');
        return;
    }

    const btn = document.getElementById('atsBtn');
    btn.textContent = 'Checking...';
    btn.disabled = true;

    try {
        const res = await api('/api/resume/ats-check', 'POST', {
            resumeContent: resume,
            jobDescription: job
        });

        if (res && res.success) {
            const d = res.data;
            const resultEl = document.getElementById('atsResult');
            resultEl.className = 'ats-result';
            resultEl.innerHTML = `
                <div class="ats-score-circle">
                    <div class="ats-score-num">${d.matchScore}%</div>
                    <div class="ats-score-label">ATS Match Score</div>
                </div>
                <div class="keyword-section">
                    <h4>✅ Matched Keywords (${d.matchedKeywords.length})</h4>
                    <div class="keyword-tags">
                        ${d.matchedKeywords.map(k => `<span class="keyword-tag keyword-matched">${escapeHtml(k)}</span>`).join('')}
                    </div>
                </div>
                <div class="keyword-section">
                    <h4>❌ Missing Keywords (${d.missingKeywords.length})</h4>
                    <div class="keyword-tags">
                        ${d.missingKeywords.map(k => `<span class="keyword-tag keyword-missing">${escapeHtml(k)}</span>`).join('')}
                    </div>
                </div>
                <div class="ats-recommendation">${escapeHtml(d.recommendation)}</div>
            `;
        }
    } catch (e) {
        alert('ATS check failed. Please try again.');
    } finally {
        btn.textContent = 'Run ATS Check';
        btn.disabled = false;
    }
}

// Score Check
function openScoreModal() { openModal('scoreModal'); }

async function runScoreCheck() {
    const content = document.getElementById('scoreContent').value.trim();
    if (!content) { alert('Please enter resume content.'); return; }

    const btn = document.getElementById('scoreBtn');
    btn.textContent = 'Calculating...';
    btn.disabled = true;

    try {
        const res = await api('/api/resume/score', 'POST', { content });
        if (res && res.success) {
            const d = res.data;
            const resultEl = document.getElementById('scoreResult');
            resultEl.className = 'ats-result';

            const breakdownHtml = Object.entries(d.breakdown).map(([key, val]) => {
                const maxMap = { 'Keywords & Action Verbs': 25, 'Structure & Sections': 25, 'Content Length': 20, 'Technical Skills': 20, 'Contact Information': 10 };
                const max = maxMap[key] || 25;
                const pct = Math.round((val / max) * 100);
                return `
                    <div class="score-row">
                        <span class="score-row-label">${key}</span>
                        <div class="score-bar-wrap"><div class="score-bar" style="width:${pct}%"></div></div>
                        <span class="score-row-val">${val}/${max}</span>
                    </div>
                `;
            }).join('');

            resultEl.innerHTML = `
                <div class="ats-score-circle">
                    <div class="ats-score-num">${d.totalScore}</div>
                    <div class="ats-score-label">out of 100</div>
                </div>
                <div class="score-breakdown">${breakdownHtml}</div>
                <p class="score-feedback">${escapeHtml(d.feedback)}</p>
            `;
        }
    } catch (e) {
        alert('Score check failed. Please try again.');
    } finally {
        btn.textContent = 'Calculate Score';
        btn.disabled = false;
    }
}

function escapeHtml(str) {
    if (!str) return '';
    return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}

loadDashboard();
