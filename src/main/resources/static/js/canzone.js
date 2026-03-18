document.addEventListener("DOMContentLoaded", () => {
    const artistSelect = document.getElementById("artist-select");
    const songSelect = document.getElementById("song-select");
    const completeBtn = document.getElementById("complete-btn");
    const btnText = completeBtn.querySelector("span");
    const timerDisplay = document.getElementById("timer-display");
    const countdownSpan = document.getElementById("countdown");
    const errorMsg = document.getElementById("error-message");
    const livelloInput = document.getElementById("livello-input");

    // Dati canzoni
    const musicData = {
        "ed sheeran": ["bad habits", "shape of you", "perfect", "photograph", "thinking out loud"],
        "the weeknd": ["blinding lights", "starboy", "save your tears", "the hills", "can't feel my face"],
        "harry styles": ["as it was", "watermelon sugar", "adore you", "sign of the times", "late night talking"],
        "dua lipa": ["levitating", "physical", "don't start now", "one kiss", "new rules"],
        "bruno mars": ["uptown funk", "24k magic", "grenade", "just the way you are", "locked out of heaven"]
    };

    const P_B64 = "RWRTaGVyYWFuWWVhaDIxMDlyZmVqbndqb2RudjEyMDgzMg==";
    const CORRECT_ARTIST = "ed sheeran";
    const CORRECT_SONG = "bad habits";

    let timerInterval = null;

    // Inizializza artisti
    function initArtists() {
        for (let artist in musicData) {
            let option = document.createElement("option");
            option.value = artist;
            option.textContent = artist.charAt(0).toUpperCase() + artist.slice(1);
            artistSelect.appendChild(option);
        }
    }

    // Aggiorna canzoni in base all'artista
    artistSelect.addEventListener("change", () => {
        const selectedArtist = artistSelect.value;
        songSelect.innerHTML = '<option value="">-- Scegli canzone --</option>';
        
        if (!selectedArtist) {
            songSelect.disabled = true;
            songSelect.innerHTML = '<option value="">-- Scegli prima artista --</option>';
            return;
        }

        songSelect.disabled = false;
        musicData[selectedArtist].forEach(song => {
            let option = document.createElement("option");
            option.value = song;
            option.textContent = song.charAt(0).toUpperCase() + song.slice(1);
            songSelect.appendChild(option);
        });
    });

    // Gestione Timer
    function startTimer(seconds) {
        clearInterval(timerInterval);
        const endTime = Date.now() + seconds * 1000;
        localStorage.setItem("canzone_timer_end", endTime);
        
        updateTimer();
        timerInterval = setInterval(updateTimer, 1000);
    }

    function updateTimer() {
        const endTime = localStorage.getItem("canzone_timer_end");
        if (!endTime) {
            stopTimer();
            return;
        }

        const remaining = Math.round((endTime - Date.now()) / 1000);
        
        if (remaining <= 0) {
            stopTimer();
        } else {
            timerDisplay.style.display = "flex";
            countdownSpan.textContent = remaining;
            completeBtn.disabled = true;
            completeBtn.classList.add("disabled");
            btnText.textContent = "Riprova tra poco";
            errorMsg.style.display = "none"; // Hide error while timer is counting down
        }
    }

    function stopTimer() {
        clearInterval(timerInterval);
        localStorage.removeItem("canzone_timer_end");
        timerDisplay.style.display = "none";
        completeBtn.disabled = false;
        completeBtn.classList.remove("disabled");
        btnText.textContent = "Verifica Risposta";
    }

    // Invio soluzione
    async function completeRiddle() {
        const livello = livelloInput.value;
        const password = atob(P_B64);

        completeBtn.disabled = true;
        completeBtn.classList.add("disabled");
        btnText.textContent = "Verifica in corso...";

        try {
            const formData = new URLSearchParams();
            formData.append("livello", livello);
            formData.append("password", password);

            const response = await fetch("/caccia-al-tesoro/quesito", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: formData
            });

            if (response.redirected) {
                window.location.href = response.url;
            } else if (response.ok) {
                window.location.href = "/caccia-al-tesoro/caccia?unlockQuesito=true";
            }
        } catch (error) {
            console.error("Errore:", error);
            btnText.textContent = "Errore di rete";
            setTimeout(() => {
                completeBtn.disabled = false;
                completeBtn.classList.remove("disabled");
                btnText.textContent = "Verifica Risposta";
            }, 2000);
        }
    }

    completeBtn.addEventListener("click", () => {
        const selectedArtist = artistSelect.value;
        const selectedSong = songSelect.value;

        if (!selectedArtist || !selectedSong) {
            alert("Seleziona sia l'artista che la canzone!");
            return;
        }

        if (selectedArtist === CORRECT_ARTIST && selectedSong === CORRECT_SONG) {
            completeRiddle();
        } else {
            errorMsg.style.display = "block";
            startTimer(60);
        }
    });

    // Controllo timer all'avvio
    if (localStorage.getItem("canzone_timer_end")) {
        updateTimer();
        timerInterval = setInterval(updateTimer, 1000);
    }

    initArtists();
});
