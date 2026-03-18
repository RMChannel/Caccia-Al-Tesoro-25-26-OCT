document.addEventListener('DOMContentLoaded', function () {
    const searchInput = document.getElementById('luogoSearch');
    const regionFilter = document.getElementById('regionFilter');
    const tableRows = document.querySelectorAll('#luoghiTable tbody tr');

    function filterTable() {
        const searchTerm = searchInput.value.toLowerCase();
        const selectedRegion = regionFilter.value;

        tableRows.forEach(row => {
            const nome = row.cells[2].textContent.toLowerCase();
            const regione = row.cells[1].textContent;

            const matchesSearch = nome.includes(searchTerm);
            const matchesRegion = selectedRegion === "" || regione === selectedRegion;

            if (matchesSearch && matchesRegion) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    }

    searchInput.addEventListener('input', filterTable);
    regionFilter.addEventListener('change', filterTable);

    // Edit Modal Logic
    const editBtns = document.querySelectorAll('.edit-luogo-btn');
    const modal = document.getElementById('modal-edit-luogo');

    editBtns.forEach(btn => {
        btn.addEventListener('click', function () {
            const livello = this.getAttribute('data-livello');
            const regione = this.getAttribute('data-regione');
            const nome = this.getAttribute('data-nome');
            const codice = this.getAttribute('data-codice');
            const indizio = this.getAttribute('data-indizio');
            const description = this.getAttribute('data-description');
            const photo = this.getAttribute('data-photo');

            document.getElementById('edit-livello').value = livello;
            document.getElementById('edit-regione').value = regione;
            document.getElementById('edit-nome').value = nome;
            document.getElementById('edit-codice').value = codice;
            document.getElementById('edit-indizio').value = indizio;
            document.getElementById('edit-description').value = description;
            document.getElementById('edit-photo').value = photo;
            document.getElementById('edit-luogo-name-display').textContent = nome;

            modal.classList.add('show');
        });
    });
});

function openModal(id) {
    document.getElementById(id).classList.add('show');
}

function closeModal(id) {
    document.getElementById(id).classList.remove('show');
}
