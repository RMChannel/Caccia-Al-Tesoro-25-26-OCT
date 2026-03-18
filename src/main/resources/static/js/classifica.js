document.addEventListener('DOMContentLoaded', function() {
    const leaderboardBody = document.getElementById('leaderboard-body');
    const filterSelect = document.getElementById('regione-filter');
    const updateInterval = 10000; // Update every 10 seconds
    
    let currentData = null;

    function getRegionAcronym(regione) {
        if (!regione) return '';
        switch (regione) {
            case 'NORD-BRA': return 'BRA';
            case 'CENTRO-CIVITAVECCHIA': return 'CENTRO';
            case 'SUD-MELFI': return 'MELFI';
            case 'SUD-ENNA': return 'ENNA';
            default: return regione;
        }
    }

    function renderLeaderboard(data) {
        if (!leaderboardBody) return;

        const filterValue = filterSelect ? filterSelect.value : 'ALL';
        const filteredData = filterValue === 'ALL' ? data : data.filter(entry => entry.regione === filterValue);

        leaderboardBody.innerHTML = '';

        filteredData.forEach((entry, index) => {
            const rank = index + 1;
            const row = document.createElement('tr');
            row.className = 'leaderboard-row';
            row.setAttribute('data-regione', entry.regione || '');
            
            if (rank <= 3) {
                row.classList.add(`top-${rank}`);
            }

            const avatarChar = entry.username ? entry.username.charAt(0).toUpperCase() : '?';
            const regioneText = getRegionAcronym(entry.regione);

            row.innerHTML = `
                <td>
                    <div class="rank-badge">${rank}</div>
                </td>
                <td>
                    <div class="team-name">
                        <div class="team-avatar">${avatarChar}</div>
                        <span>${entry.username}</span>
                    </div>
                </td>
                <td>
                    <span class="regione-badge">${regioneText}</span>
                </td>
                <td>
                    <span class="score-value">${entry.score}/20</span>
                </td>
                <td>
                    <span class="time-value">${entry.completedAt || '--:--'}</span>
                </td>
                <td>
                    <span class="${entry.completed ? 'status-indicator status-online' : 'status-indicator status-offline'}"></span>
                    <span class="status-text">${entry.completed ? 'Completato' : 'In Corso'}</span>
                </td>
            `;
            leaderboardBody.appendChild(row);
        });
    }

    function updateLeaderboard() {
        fetch('/caccia-al-tesoro/update-classifica', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            currentData = data;
            renderLeaderboard(data);
        })
        .catch(error => {
            console.error('Error fetching leaderboard:', error);
        });
    }

    if (filterSelect) {
        filterSelect.addEventListener('change', () => {
            if (currentData) {
                renderLeaderboard(currentData);
            }
        });
    }

    // Initial fetch to populate currentData and allow immediate filtering
    updateLeaderboard();
    
    // Start the interval for subsequent updates
    setInterval(updateLeaderboard, updateInterval);
});
