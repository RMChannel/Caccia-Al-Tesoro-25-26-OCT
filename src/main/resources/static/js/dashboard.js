document.addEventListener("DOMContentLoaded", function () {
    const progressFill = document.querySelector(".progress-fill");
    if (progressFill) {
        const target = progressFill.getAttribute("data-percent");
        requestAnimationFrame(function () {
            progressFill.style.width = target + "%";
        });
    }

    // Gestione parametro unlockLuogo per feedback utente
    if (new URLSearchParams(window.location.search).has("unlockLuogo")) {
        showToast("Ottimo lavoro! Hai sbloccato un nuovo quesito.");
    }
});

function showToast(message) {
    const toast = document.createElement("div");
    toast.className = "toast-notification";
    toast.innerHTML = `
        <span class="toast-icon">✨</span>
        <span class="toast-message">${message}</span>
    `;
    document.body.appendChild(toast);

    // Trigger animation
    setTimeout(() => toast.classList.add("show"), 100);

    // Remove after 4 seconds
    setTimeout(() => {
        toast.classList.remove("show");
        setTimeout(() => toast.remove(), 500);
    }, 4000);
}

function initMap() {
    const mapEl = document.getElementById("map");
    if (!mapEl) return;

    const locations = JSON.parse(mapEl.getAttribute("data-locations"));

    const bounds = new google.maps.LatLngBounds();
    locations.forEach(function (loc) {
        bounds.extend({ lat: loc.lat, lng: loc.lng });
    });

    const map = new google.maps.Map(mapEl, {
        zoom: 12,
        center: bounds.getCenter(),
        mapTypeControl: false,
        streetViewControl: false,
        styles: [
            {
                featureType: "poi",
                elementType: "labels",
                stylers: [{ visibility: "off" }],
            },
        ],
    });

    map.fitBounds(bounds);

    locations.forEach(function (loc) {
        var pinColor = loc.completed ? "#2bb673" : "#e54b4b";

        var marker = new google.maps.Marker({
            position: { lat: loc.lat, lng: loc.lng },
            map: map,
            title: loc.name,
            icon: {
                path: google.maps.SymbolPath.CIRCLE,
                scale: 10,
                fillColor: pinColor,
                fillOpacity: 1,
                strokeWeight: 2.5,
                strokeColor: "#ffffff",
            },
        });

        var directionsUrl =
            "https://www.google.com/maps/dir/?api=1&destination=" +
            loc.lat +
            "," +
            loc.lng;

        var unlockBtnHtml = loc.completed
            ? ""
            : '<a class="iw-btn iw-btn-unlock" href="/caccia-al-tesoro/unlock-luogo?livello=' +
            loc.livello +
            '">🔓 Sblocca</a>';

        var contentString =
            '<div class="info-window-content">' +
            "<h4>" +
            loc.name +
            "</h4>" +
            '<div class="iw-buttons">' +
            unlockBtnHtml +
            '<a class="iw-btn iw-btn-directions" href="' +
            directionsUrl +
            '" target="_blank" rel="noopener">📍 Indicazioni</a>' +
            "</div></div>";

        var infoWindow = new google.maps.InfoWindow({ content: contentString });

        marker.addListener("click", function () {
            infoWindow.open(map, marker);
        });
    });
}
