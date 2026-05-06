requireAuth();

let skills = [];
let currentResumeId = null;
let currentContent = '';

// ===== Skills Input =====
const skillInput = document.getElementById('skillInput');
skillInput.addEventListener('keydown', (e) => {
    if (e.key === 'Enter' || e.key === ',') {
        e.preventDefault();
        addSkill(skillInput.value.trim().replace(/,$/, ''));
        skillInput.value = '';
    }
});

function addSkill(skill) {
    if (!skill || skills.includes(skill)) return;
    skills.push(skill);
    renderSkills();
}

function removeSkill(skill) {
    skills = skills.filter(s => s !== skill);
    renderSkills();
}

function renderSkills() {
    document.getElementById('skillTags').innerHTML = skills.map(s => `
        <span class="skill-tag">
            ${escapeHtml(s)}
            <button type="button" onclick="removeSkill('${escapeHtml(s)}')" title="Remove">×</button>
        </span>
    `).join('');
}

// ===== Experience =====
let expCount = 0;
function addExperience() {
    expCount++;
    const id = `exp_${expCount}`;
    const div = document.createElement('div');
    div.className = 'dynamic-item';
    div.id = id;
    div.innerHTML = `
        <div class="dynamic-item-header">
            <span class="dynamic-item-title">Experience #${expCount}</span>
            <button type="button" class="remove-btn" onclick="removeItem('${id}')">✕</button>
        </div>
        <div class="form-grid">
            <div class="form-group">
                <label>Company</label>
                <input type="text" name="exp_company_${expCount}" placeholder="Company Name">
            </div>
            <div class="form-group">
                <label>Position</label>
                <input type="text" name="exp_position_${expCount}" placeholder="Job Title">
            </div>
            <div class="form-group">
                <label>Start Date</label>
                <input type="text" name="exp_start_${expCount}" placeholder="Jan 2022">
            </div>
            <div class="form-group">
                <label>End Date</label>
                <input type="text" name="exp_end_${expCount}" placeholder="Present">
            </div>
            <div class="form-group form-full">
                <label>Description (one bullet per line)</label>
                <textarea name="exp_desc_${expCount}" rows="3" placeholder="• Led development of...&#10;• Improved performance by..."></textarea>
            </div>
        </div>
    `;
    document.getElementById('experienceList').appendChild(div);
}

// ===== Education =====
let eduCount = 0;
function addEducation() {
    eduCount++;
    const id = `edu_${eduCount}`;
    const div = document.createElement('div');
    div.className = 'dynamic-item';
    div.id = id;
    div.innerHTML = `
        <div class="dynamic-item-header">
            <span class="dynamic-item-title">Education #${eduCount}</span>
            <button type="button" class="remove-btn" onclick="removeItem('${id}')">✕</button>
        </div>
        <div class="form-grid">
            <div class="form-group">
                <label>Institution</label>
                <input type="text" name="edu_inst_${eduCount}" placeholder="University Name">
            </div>
            <div class="form-group">
                <label>Degree</label>
                <input type="text" name="edu_degree_${eduCount}" placeholder="Bachelor of Science">
            </div>
            <div class="form-group">
                <label>Field of Study</label>
                <input type="text" name="edu_field_${eduCount}" placeholder="Computer Science">
            </div>
            <div class="form-group">
                <label>Graduation Year</label>
                <input type="text" name="edu_year_${eduCount}" placeholder="2023">
            </div>
            <div class="form-group">
                <label>GPA (optional)</label>
                <input type="text" name="edu_gpa_${eduCount}" placeholder="3.8">
            </div>
        </div>
    `;
    document.getElementById('educationList').appendChild(div);
}

// ===== Projects =====
let projCount = 0;
function addProject() {
    projCount++;
    const id = `proj_${projCount}`;
    const div = document.createElement('div');
    div.className = 'dynamic-item';
    div.id = id;
    div.innerHTML = `
        <div class="dynamic-item-header">
            <span class="dynamic-item-title">Project #${projCount}</span>
            <button type="button" class="remove-btn" onclick="removeItem('${id}')">✕</button>
        </div>
        <div class="form-grid">
            <div class="form-group">
                <label>Project Name</label>
                <input type="text" name="proj_name_${projCount}" placeholder="My Awesome Project">
            </div>
            <div class="form-group">
                <label>Technologies</label>
                <input type="text" name="proj_tech_${projCount}" placeholder="React, Node.js, MongoDB">
            </div>
            <div class="form-group form-full">
                <label>Description</label>
                <textarea name="proj_desc_${projCount}" rows="2" placeholder="Brief description of what you built and its impact..."></textarea>
            </div>
            <div class="form-group form-full">
                <label>Link (optional)</label>
                <input type="text" name="proj_link_${projCount}" placeholder="https://github.com/...">
            </div>
        </div>
    `;
    document.getElementById('projectList').appendChild(div);
}

function removeItem(id) {
    document.getElementById(id)?.remove();
}

// ===== Collect Form Data =====
function collectFormData() {
    const template = document.querySelector('input[name="template"]:checked')?.value || 'modern';

    // Experience
    const experience = [];
    for (let i = 1; i <= expCount; i++) {
        const company = document.querySelector(`[name="exp_company_${i}"]`)?.value;
        if (company) {
            experience.push({
                company,
                position: document.querySelector(`[name="exp_position_${i}"]`)?.value || '',
                startDate: document.querySelector(`[name="exp_start_${i}"]`)?.value || '',
                endDate: document.querySelector(`[name="exp_end_${i}"]`)?.value || 'Present',
                description: document.querySelector(`[name="exp_desc_${i}"]`)?.value || ''
            });
        }
    }

    // Education
    const education = [];
    for (let i = 1; i <= eduCount; i++) {
        const inst = document.querySelector(`[name="edu_inst_${i}"]`)?.value;
        if (inst) {
            education.push({
                institution: inst,
                degree: document.querySelector(`[name="edu_degree_${i}"]`)?.value || '',
                field: document.querySelector(`[name="edu_field_${i}"]`)?.value || '',
                graduationYear: document.querySelector(`[name="edu_year_${i}"]`)?.value || '',
                gpa: document.querySelector(`[name="edu_gpa_${i}"]`)?.value || ''
            });
        }
    }

    // Projects
    const projects = [];
    for (let i = 1; i <= projCount; i++) {
        const name = document.querySelector(`[name="proj_name_${i}"]`)?.value;
        if (name) {
            projects.push({
                name,
                technologies: document.querySelector(`[name="proj_tech_${i}"]`)?.value || '',
                description: document.querySelector(`[name="proj_desc_${i}"]`)?.value || '',
                link: document.querySelector(`[name="proj_link_${i}"]`)?.value || ''
            });
        }
    }

    return {
        title: document.getElementById('title').value,
        fullName: document.getElementById('fullName').value,
        email: document.getElementById('email').value,
        phone: document.getElementById('phone').value,
        location: document.getElementById('location').value,
        summary: document.getElementById('summary').value,
        experience,
        education,
        skills,
        projects,
        templateName: template
    };
}

// ===== Generate Resume =====
document.getElementById('resumeForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const btn = document.getElementById('generateBtn');
    btn.textContent = 'Generating...';
    btn.disabled = true;

    try {
        const data = collectFormData();
        const res = await api('/api/resume/generate', 'POST', data);

        if (res && res.success) {
            currentResumeId = res.data.id;
            currentContent = res.data.content;
            renderPreview(data, res.data.content);
            renderScore(res.data.score);
            document.getElementById('previewActions').style.display = 'flex';
        } else {
            alert(res?.message || 'Failed to generate resume.');
        }
    } catch (e) {
        alert('Something went wrong. Please try again.');
    } finally {
        btn.textContent = 'Generate Resume';
        btn.disabled = false;
    }
});

// ===== Render Preview =====
function renderPreview(formData, content) {
    const template = formData.templateName || 'modern';
    const preview = document.getElementById('resumePreview');
    preview.className = `resume-preview-area resume-${template}`;

    let html = '';

    // Header
    html += `<div class="r-header">
        <div class="r-name">${escapeHtml(formData.fullName)}</div>
        <div class="r-contact">
            ${escapeHtml(formData.email)}
            ${formData.phone ? ' · ' + escapeHtml(formData.phone) : ''}
            ${formData.location ? ' · ' + escapeHtml(formData.location) : ''}
        </div>
    </div>`;

    // Summary
    if (formData.summary) {
        html += `<div class="r-section">
            <div class="r-section-title">Professional Summary</div>
            <p style="font-size:13px;color:var(--gray-600)">${escapeHtml(formData.summary)}</p>
        </div>`;
    }

    // Experience
    if (formData.experience && formData.experience.length > 0) {
        html += `<div class="r-section"><div class="r-section-title">Work Experience</div>`;
        formData.experience.forEach(exp => {
            html += `<div class="r-item">
                <div class="r-item-header">
                    <span class="r-item-title">${escapeHtml(exp.position)}</span>
                    <span class="r-item-date">${escapeHtml(exp.startDate)} – ${escapeHtml(exp.endDate || 'Present')}</span>
                </div>
                <div class="r-item-sub">${escapeHtml(exp.company)}</div>
                ${exp.description ? `<ul class="r-item-desc">${exp.description.split('\n').filter(l => l.trim()).map(l => `<li>${escapeHtml(l.replace(/^[•\-]\s*/, ''))}</li>`).join('')}</ul>` : ''}
            </div>`;
        });
        html += `</div>`;
    }

    // Education
    if (formData.education && formData.education.length > 0) {
        html += `<div class="r-section"><div class="r-section-title">Education</div>`;
        formData.education.forEach(edu => {
            html += `<div class="r-item">
                <div class="r-item-header">
                    <span class="r-item-title">${escapeHtml(edu.degree)} in ${escapeHtml(edu.field)}</span>
                    <span class="r-item-date">${escapeHtml(edu.graduationYear || '')}</span>
                </div>
                <div class="r-item-sub">${escapeHtml(edu.institution)}${edu.gpa ? ' · GPA: ' + escapeHtml(edu.gpa) : ''}</div>
            </div>`;
        });
        html += `</div>`;
    }

    // Skills
    if (formData.skills && formData.skills.length > 0) {
        html += `<div class="r-section">
            <div class="r-section-title">Skills</div>
            <div class="r-skills">${formData.skills.map(s => `<span class="r-skill-tag">${escapeHtml(s)}</span>`).join('')}</div>
        </div>`;
    }

    // Projects
    if (formData.projects && formData.projects.length > 0) {
        html += `<div class="r-section"><div class="r-section-title">Projects</div>`;
        formData.projects.forEach(proj => {
            html += `<div class="r-item">
                <div class="r-item-header">
                    <span class="r-item-title">${escapeHtml(proj.name)}</span>
                </div>
                ${proj.technologies ? `<div class="r-item-sub">Tech: ${escapeHtml(proj.technologies)}</div>` : ''}
                ${proj.description ? `<div class="r-item-desc">${escapeHtml(proj.description)}</div>` : ''}
                ${proj.link ? `<div class="r-item-desc"><a href="${escapeHtml(proj.link)}" style="color:var(--primary)">${escapeHtml(proj.link)}</a></div>` : ''}
            </div>`;
        });
        html += `</div>`;
    }

    preview.innerHTML = html;
}

// ===== Render Score =====
function renderScore(score) {
    if (score == null) return;
    const panel = document.getElementById('scorePanel');
    panel.classList.remove('hidden');
    document.getElementById('scoreBadge').textContent = score + '/100';
}

// ===== AI Improve =====
async function improveResume() {
    if (!currentContent) { alert('Generate a resume first.'); return; }
    const btn = document.getElementById('improveBtn');
    btn.textContent = '⏳ Improving...';
    btn.disabled = true;

    try {
        const res = await api('/api/resume/improve', 'POST', {
            content: currentContent,
            resumeId: currentResumeId
        });

        if (res && res.success && res.data.improvedContent) {
            const preview = document.getElementById('resumePreview');
            preview.innerHTML = `
                <div style="background:#F0FDF4;border:1px solid #BBF7D0;border-radius:8px;padding:16px;margin-bottom:16px;font-size:13px;color:#166534">
                    ✅ AI-improved version shown below
                </div>
                <pre style="white-space:pre-wrap;font-family:var(--font);font-size:13px;line-height:1.7;color:var(--gray-700)">${escapeHtml(res.data.improvedContent)}</pre>
            `;
            currentContent = res.data.improvedContent;
        } else {
            alert(res?.message || 'Improvement failed.');
        }
    } catch (e) {
        alert('AI improvement failed. Please try again.');
    } finally {
        btn.textContent = '🤖 AI Improve';
        btn.disabled = false;
    }
}

// ===== Download PDF =====
async function downloadPdf() {
    if (!currentContent) { alert('Generate a resume first.'); return; }
    const btn = document.getElementById('downloadBtn');
    btn.textContent = '⏳ Generating...';
    btn.disabled = true;

    try {
        const token = localStorage.getItem('token');
        const title = document.getElementById('title').value || 'My Resume';

        const res = await fetch('/api/resume/download-pdf', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ content: currentContent, title })
        });

        if (res.ok) {
            const blob = await res.blob();
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `${title.replace(/\s+/g, '_')}.pdf`;
            a.click();
            URL.revokeObjectURL(url);
        } else {
            alert('PDF generation failed.');
        }
    } catch (e) {
        alert('PDF download failed. Please try again.');
    } finally {
        btn.textContent = '📥 Download PDF';
        btn.disabled = false;
    }
}

function escapeHtml(str) {
    if (!str) return '';
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}

// Add one experience and education by default
addExperience();
addEducation();
