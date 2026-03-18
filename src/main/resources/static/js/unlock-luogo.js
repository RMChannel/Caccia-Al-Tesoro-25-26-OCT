document.addEventListener("DOMContentLoaded", function () {
    var qrScanner = new Html5QrcodeScanner("qr-reader", {
        fps: 10,
        qrbox: { width: 250, height: 250 },
        rememberLastUsedCamera: true,
        showTorchButtonIfSupported: true,
        supportedScanTypes: [Html5QrcodeScanType.SCAN_TYPE_CAMERA]
    });

    function onScanSuccess(decodedText) {
        document.getElementById("qr-result").value = decodedText.toUpperCase();
        qrScanner.clear();
    }

    function onScanFailure() { }

    qrScanner.render(onScanSuccess, onScanFailure);

    const qrResultInput = document.getElementById("qr-result");
    qrResultInput.addEventListener("input", function() {
        this.value = this.value.toUpperCase();
    });

    document.getElementById("unlock-form").addEventListener("submit", function (e) {
        var code = document.getElementById("qr-result").value.trim();
        if (!code) {
            e.preventDefault();
            alert("Scansiona un codice QR prima di verificare.");
            return;
        }
    });
});

function initLocationMap() {
    var mapEl = document.getElementById("location-map");
    if (!mapEl) return;

    var lat = parseFloat(mapEl.getAttribute("data-lat"));
    var lng = parseFloat(mapEl.getAttribute("data-lng"));
    var name = mapEl.getAttribute("data-name") || "Luogo";

    var center = { lat: lat, lng: lng };

    var map = new google.maps.Map(mapEl, {
        zoom: 15,
        center: center,
        mapTypeControl: false,
        streetViewControl: false,
        styles: [
            {
                featureType: "poi",
                elementType: "labels",
                stylers: [{ visibility: "off" }]
            }
        ]
    });

    new google.maps.Marker({
        position: center,
        map: map,
        title: name,
        icon: {
            path: google.maps.SymbolPath.CIRCLE,
            scale: 12,
            fillColor: "#1d75d0",
            fillOpacity: 1,
            strokeWeight: 3,
            strokeColor: "#ffffff"
        }
    });
}
