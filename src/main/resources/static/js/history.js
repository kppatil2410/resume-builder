requireAuth();

let currentResume = null;

async function loadHistory() {
    const grid = document.getElementById('historyGrid');
    try {
        const res = await api('/api/resume/history');
        if (!res || !res.success) {
            grid.innerHTML = '<div class="loading">Failed to load resumes.</div>';
            return;
        }

        const resumes = res.data || [];
        if (resumes.length === 0) {
            grid.innerHTML = `
                <div style="grid-column:1/-1;text-align:center;padding:60px;color:var(--gray-400)">
                    <div style="font-size:48px;margin-bottom:12px">📄</div>
                    <p style="margin-bottom:16px">No resumes yet.</p>
                    <a href="/resume-builder.html" class="btn btn-primary">Create your first resume</a>
                </div>
            `;
            return;
        }

        grid.innerHTML = resumes.map(r => `
            <div class="history-card" onclick="openResume(${r.id})">
                <div class="history-card-header">
                    <div>
                        <div class="history-card-title">${escapeHtml(r.title)}</div>
                        <div class="history-card-date">${formatDate(r.createdAt)}</div>
                    </div>
                    ${r.score != null ? `<span class="score-chip ${scoreClass(r.score)}">${r.score}/100</span>` : ''}
                </div>
                <div class="history-card-preview">${escapeHtml(r.content || '')}</div>
                <div class="history-card-footer">
                    <span style="font-size:12px;color:var(--gray-400)">${r.templateName || 'modern'} template</span>
                    <div style="display:flex;gap:6px">
                        ${r.improvedContent ? '<span style="font-size:12px;color:var(--success)">✅ AI Improved</span>' : ''}
                    </div>
                </div>
            </div>
        `).join('');
    } catch (e) {
        grid.innerHTML = '<div class="loading">Error loading resumes.</div>';
    }
}

async function openResume(id) {
    try {
        const res = await api(`/api/resume/${id}`);
        if (!res || !res.success) return;

        currentResume = res.data;
        document.getElementById('modalTitle').textContent = currentResume.title;
        document.getElementById('modalOriginal').textContent = currentResume.content || '';

        const improvedCol = document.getElementById('improvedCol');
        if (currentResume.improvedContent) {
            document.getElementById('modalImproved').textContent = currentResume.improvedContent;
            improvedCol.style.display = 'block';
        } else {
            improvedCol.style.display = 'none';
        }

        openModal('resumeModal');
    } catch (e) {
        alert('Failed to load resume.');
    }
}

async function improveFromModal() {
    if (!currentResume) return;
    const btn = document.getElementById('modalImproveBtn');
    btn.textContent = '⏳ Improving...';
    btn.disabled = true;

    try {
        const res = await api('/api/resume/improve', 'POST', {
            content: currentResume.content,
            resumeId: currentResume.id
        });

        if (res && res.success && res.data.improvedContent) {
            currentResume.improvedContent = res.data.improvedContent;
            document.getElementById('modalImproved').textContent = res.data.improvedContent;
            document.getElementById('improvedCol').style.display = 'block';
        } else {
            alert(res?.message || 'Improvement failed.');
        }
    } catch (e) {
        alert('AI improvement failed.');
    } finally {
        btn.textContent = '🤖 AI Improve';
        btn.disabled = false;
    }
}

async function downloadFromModal() {
    if (!currentResume) return;
    const btn = document.getElementById('modalDownloadBtn');
    btn.textContent = '⏳...';
    btn.disabled = true;

    try {
        const token = localStorage.getItem('token');
        const content = currentResume.improvedContent || currentResume.content;

        const res = await fetch(`/api/resume/${currentResume.id}/download`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (res.ok) {
            const blob = await res.blob();
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `${currentResume.title.replace(/\s+/g, '_')}.pdf`;
            a.click();
            URL.revokeObjectURL(url);
        } else {
            alert('PDF download failed.');
        }
    } catch (e) {
        alert('PDF download failed.');
    } finally {
        btn.textContent = '📥 PDF';
        btn.disabled = false;
    }
}

function escapeHtml(str) {
    if (!str) return '';
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}

loadHistory();
