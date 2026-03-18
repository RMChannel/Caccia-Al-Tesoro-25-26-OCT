document.addEventListener('DOMContentLoaded', function() {
    const tabs = document.querySelectorAll('.tab-btn');
    const contents = document.querySelectorAll('.tab-content');

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            const target = tab.dataset.tab;

            tabs.forEach(t => t.classList.remove('active'));
            contents.forEach(c => c.classList.remove('active'));

            tab.classList.add('active');
            document.getElementById(target).classList.add('active');
            
            // Update URL hash without jumping
            history.pushState(null, null, `#${target}`);
        });
    });

    // Handle initial tab from URL hash
    const hash = window.location.hash.substring(1);
    if (hash) {
        const initialTab = document.querySelector(`.tab-btn[data-tab="${hash}"]`);
        if (initialTab) {
            initialTab.click();
        }
    }

    // Event delegation for dynamically created buttons
    document.addEventListener('click', function(e) {
        // Gestione eliminazione utente
        if (e.target.classList.contains('delete-user-btn')) {
            const username = e.target.dataset.username;
            if (confirm(`Sei sicuro di voler eliminare l'utente "${username}"? Questa azione non può essere annullata.`)) {
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = '/caccia-al-tesoro/admin/remove-user';
                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'username';
                input.value = username;
                form.appendChild(input);
                document.body.appendChild(form);
                form.submit();
            }
        }

        // Gestione Modifica Utente
        if (e.target.classList.contains('edit-user-btn')) {
            const username = e.target.dataset.username;
            const regione = e.target.dataset.regione;
            const role = e.target.dataset.role;

            document.getElementById('edit-username-display').textContent = username;
            document.getElementById('edit-old-username').value = username;
            document.getElementById('edit-username').value = username;
            document.getElementById('edit-regione').value = regione;
            document.getElementById('edit-admin').checked = (role === 'ADMIN');

            openModal('modal-edit-user');
        }

        // Gestione Dettagli Utente
        if (e.target.classList.contains('details-user-btn')) {
            const username = e.target.dataset.username;
            const detailsHtml = e.target.closest('td').querySelector('.user-details-data').innerHTML;

            document.getElementById('details-username-display').textContent = username;
            document.getElementById('details-content').innerHTML = detailsHtml;

            openModal('modal-user-details');
        }
    });

    // Filtro e Ricerca Utenti
    const userSearch = document.getElementById('userSearch');
    const regionFilter = document.getElementById('regionFilter');
    const usersTable = document.getElementById('usersTable');
    
    const filterTable = () => {
        if (!usersTable) return;
        const rows = usersTable.querySelectorAll('tbody tr:not(.no-results-row)');
        const searchText = userSearch.value.toLowerCase();
        const selectedRegion = regionFilter.value;
        let visibleCount = 0;

        rows.forEach(row => {
            const usernameElement = row.querySelector('td:first-child div[style*="font-weight: 700"]');
            const realNameElement = row.querySelector('td:first-child div[style*="font-size: 0.8rem"]');
            const regionElement = row.querySelector('td:nth-child(2)');

            if (!usernameElement || !realNameElement || !regionElement) return;

            const username = usernameElement.textContent.toLowerCase();
            const realName = realNameElement.textContent.toLowerCase();
            const region = regionElement.textContent;

            const matchesSearch = username.includes(searchText) || realName.includes(searchText);
            const matchesRegion = selectedRegion === "" || region === selectedRegion;

            if (matchesSearch && matchesRegion) {
                row.style.display = "";
                visibleCount++;
            } else {
                row.style.display = "none";
            }
        });

        // Gestione riga "nessun risultato"
        let emptyRow = usersTable.querySelector('.no-results-row');
        if (visibleCount === 0 && rows.length > 0) {
            if (!emptyRow) {
                emptyRow = document.createElement('tr');
                emptyRow.className = 'no-results-row';
                emptyRow.innerHTML = `<td colspan="7" style="text-align: center; padding: 40px; color: var(--ink-soft);">Nessun utente corrisponde ai filtri.</td>`;
                usersTable.querySelector('tbody').appendChild(emptyRow);
            }
            emptyRow.style.display = "";
        } else if (emptyRow) {
            emptyRow.style.display = "none";
        }
    };

    if (userSearch && regionFilter) {
        userSearch.addEventListener('input', filterTable);
        regionFilter.addEventListener('change', filterTable);
    }

    // Auto-update every 10 seconds
    async function refreshData() {
        try {
            const [usersRes, onlineRes] = await Promise.all([
                fetch('/caccia-al-tesoro/admin/update'),
                fetch('/caccia-al-tesoro/admin/updateonlineusers')
            ]);

            if (!usersRes.ok || !onlineRes.ok) throw new Error('Update failed');

            const allUsers = await usersRes.json();
            const onlineUsers = await onlineRes.json();

            updateUI(allUsers, onlineUsers);
        } catch (err) {
            console.error('Error refreshing data:', err);
        }
    }

    function updateUI(allUsers, onlineUsers) {
        // Update counts in tabs
        document.querySelector('.tab-btn[data-tab="gestione-utenti"] span').textContent = allUsers.length;
        document.querySelector('.tab-btn[data-tab="utenti-online"] span').textContent = onlineUsers.length;

        // Update Users Table
        const tbody = document.querySelector('#usersTable tbody');
        if (tbody) {
            tbody.innerHTML = '';
            if (allUsers.length === 0) {
                tbody.innerHTML = `<tr><td colspan="7" style="text-align: center; padding: 40px; color: var(--ink-soft);">Nessun utente trovato.</td></tr>`;
            } else {
                allUsers.forEach(u => {
                    const isOnline = onlineUsers.includes(u.username);
                    const isCompleted = u.completed !== null;
                    const completionTime = isCompleted ? formatDate(u.completed) : '—';
                    
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>
                            <div class="user-badge">
                                <div class="user-avatar-small">${u.username.substring(0, 1)}</div>
                                <div>
                                    <div style="font-weight: 700;">${u.username}</div>
                                    <div style="font-size: 0.8rem; color: var(--ink-soft);">${u.realName || ''}</div>
                                </div>
                            </div>
                        </td>
                        <td>${u.regione}</td>
                        <td>${u.role}</td>
                        <td>
                            <span class="status-indicator ${isOnline ? 'status-online' : 'status-offline'}"></span>
                            <span>${isOnline ? 'Online' : 'Offline'}</span>
                        </td>
                        <td>
                            <span class="status-indicator ${isCompleted ? 'status-online' : 'status-offline'}"></span>
                            <span>${isCompleted ? 'Completato' : 'In Corso'}</span>
                        </td>
                        <td>
                            <span style="${!isCompleted ? 'color: var(--ink-soft);' : ''}">${completionTime}</span>
                        </td>
                        <td>
                            <button class="action-btn details-user-btn" data-username="${u.username}">Dettagli</button>
                            <button class="action-btn edit-user-btn" data-username="${u.username}" data-regione="${u.regione}" data-role="${u.role}">Modifica</button>
                            <button class="action-btn danger delete-user-btn" data-username="${u.username}">Elimina</button>
                            
                            <div class="user-details-data" style="display:none;">
                                <div class="details-section">
                                    <h4><i class="icon">📍</i> Luoghi Sbloccati</h4>
                                    <ul class="details-list">
                                        ${u.unlockLuogoEntities.map(l => `
                                            <li class="details-item">
                                                <span class="item-label">Livello ${l.luogo.livello}: ${l.luogo.nome}</span>
                                                <span class="badge ${l.completed ? 'badge-success' : 'badge-warning'}">${l.completed ? 'Completato' : 'In Corso'}</span>
                                            </li>
                                        `).join('')}
                                        ${u.unlockLuogoEntities.length === 0 ? '<li class="details-empty">Nessun luogo sbloccato</li>' : ''}
                                    </ul>
                                </div>
                                <div class="details-section">
                                    <h4><i class="icon">🧩</i> Quesiti Sbloccati</h4>
                                    <ul class="details-list">
                                        ${u.unlockQuesitoEntities.map(q => `
                                            <li class="details-item">
                                                <span class="item-label">Livello ${q.quesito.livello}</span>
                                                <span class="badge ${q.completed ? 'badge-success' : 'badge-warning'}">${q.completed ? 'Completato' : 'In Corso'}</span>
                                            </li>
                                        `).join('')}
                                        ${u.unlockQuesitoEntities.length === 0 ? '<li class="details-empty">Nessun quesito sbloccato</li>' : ''}
                                    </ul>
                                </div>
                            </div>
                        </td>
                    `;
                    tbody.appendChild(row);
                });
                filterTable(); // Re-apply current filters
            }
        }

        // Update Online Users List
        const onlineList = document.querySelector('.online-users-list');
        if (onlineList) {
            onlineList.innerHTML = '';
            if (onlineUsers.length === 0) {
                onlineList.innerHTML = `<li style="grid-column: 1/-1; text-align: center; padding: 20px; color: var(--ink-soft);">Nessun utente online al momento.</li>`;
            } else {
                onlineUsers.forEach(username => {
                    const li = document.createElement('li');
                    li.className = 'online-user-item';
                    li.innerHTML = `
                        <div class="user-avatar-small">${username.substring(0, 1)}</div>
                        <span>${username}</span>
                        <span class="status-indicator status-online" style="margin-left: auto;"></span>
                    `;
                    onlineList.appendChild(li);
                });
            }
        }
    }

    function formatDate(dateStr) {
        if (!dateStr) return '—';
        const d = new Date(dateStr);
        return d.toLocaleString('it-IT', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' });
    }

    // Run every 10 seconds
    setInterval(refreshData, 10000);
});

function openModal(id) {
    document.getElementById(id).classList.add('show');
}

function closeModal(id) {
    document.getElementById(id).classList.remove('show');
    if (id === 'modal-confirm-password') {
        const url = new URL(window.location);
        url.searchParams.delete('password');
        url.searchParams.delete('confirm');
        window.history.replaceState({}, '', url);
    }
}

function copyPassword() {
    const password = document.querySelector('#generated-password-box code').textContent;
    navigator.clipboard.writeText(password).then(() => {
        const btn = document.querySelector('.copy-btn');
        const originalText = btn.textContent;
        btn.textContent = 'Copiato!';
        btn.style.background = '#10b981';
        setTimeout(() => {
            btn.textContent = originalText;
            btn.style.background = '';
        }, 2000);
    });
}

async function toggleGame() {
    const btn = document.getElementById('toggle-game-btn');
    const span = btn.querySelector('span');
    
    // Disable button during request
    btn.disabled = true;
    const originalText = span.textContent;
    span.textContent = 'Caricamento...';

    try {
        const response = await fetch('/caccia-al-tesoro/admin/activate-game', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) throw new Error('Errore nella richiesta');

        const isActive = await response.json();
        
        // Update button appearance
        if (isActive) {
            btn.className = 'cta danger';
            span.textContent = 'Disattiva Gioco';
        } else {
            btn.className = 'cta success';
            span.textContent = 'Attiva Gioco';
        }
    } catch (err) {
        console.error('Error toggling game:', err);
        alert('Si è verificato un errore durante la modifica dello stato del gioco.');
        span.textContent = originalText;
    } finally {
        btn.disabled = false;
    }
}
