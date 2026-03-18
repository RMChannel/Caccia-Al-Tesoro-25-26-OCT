document.addEventListener("DOMContentLoaded", () => {
    const startBtn = document.getElementById("start-btn");
    const resetBtn = document.getElementById("reset-btn");
    const completeBtn = document.getElementById("complete-btn");
    const colorGrid = document.getElementById("color-grid");
    const colorBtns = document.querySelectorAll(".color-btn");
    const sequenceDots = document.querySelectorAll(".sequence-dot");
    const statusText = document.querySelector(".status-text");
    const completionArea = document.getElementById("completion-area");
    const livelloInput = document.getElementById("livello-input");

    const colors = ["red", "blue", "green", "yellow", "orange", "purple"];
    let sequence = [];
    let userSequence = [];
    let isShowingSequence = false;
    let currentStep = 2;
    const targetStep = 6;

    const P_B64 = "Rm9yemE0TGVzZ29za2lMZXNnbw==";

    function generateSequence() {
        sequence = [];
        for (let i = 0; i < currentStep; i++) {
            sequence.push(colors[Math.floor(Math.random() * colors.length)]);
        }
    }

    async function showSequence() {
        isShowingSequence = true;
        disableButtons();
        resetDots();
        startBtn.disabled = true;
        startBtn.style.opacity = "0.5";
        
        statusText.textContent = `Livello ${currentStep - 1}/5: Osserva la sequenza...`;
        statusText.className = "status-text";

        // Velocità progressiva: più il livello è alto, più è veloce
        // Livello 1 (currentStep 2): delay 800ms, highlight 600ms
        // Livello 5 (currentStep 6): delay 400ms, highlight 300ms
        const displayDelay = Math.max(800 - (currentStep - 2) * 100, 400);
        const highlightDuration = Math.max(600 - (currentStep - 2) * 75, 300);

        for (let i = 0; i < sequence.length; i++) {
            await sleep(displayDelay);
            const color = sequence[i];
            const btn = document.querySelector(`.color-btn.${color}`);
            
            // Highlight color
            btn.classList.add("highlight");
            await sleep(highlightDuration);
            btn.classList.remove("highlight");
        }

        await sleep(500);
        statusText.textContent = "Tocca a te! Ripeti la sequenza.";
        enableButtons();
        isShowingSequence = false;
        userSequence = [];
    }

    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    function disableButtons() {
        colorBtns.forEach(btn => btn.classList.add("disabled"));
    }

    function enableButtons() {
        colorBtns.forEach(btn => btn.classList.remove("disabled"));
    }

    function resetDots() {
        // Clear all dots
        sequenceDots.forEach(dot => {
            dot.style.backgroundColor = "#e0e0e0";
            dot.classList.remove("active");
            dot.style.display = "none";
        });
        
        // Show only necessary dots for current level
        for(let i = 0; i < currentStep; i++) {
            sequenceDots[i].style.display = "block";
        }
    }

    function updateDots() {
        for (let i = 0; i < userSequence.length; i++) {
            const colorName = userSequence[i];
            const colorMap = {
                'red': '#e54b4b',
                'blue': '#2d8cf0',
                'green': '#2bb673',
                'yellow': '#ffcc00',
                'orange': '#ff9900',
                'purple': '#9b51e0'
            };
            sequenceDots[i].style.backgroundColor = colorMap[colorName];
            sequenceDots[i].classList.add("active");
        }
    }

    colorBtns.forEach(btn => {
        btn.addEventListener("click", () => {
            if (isShowingSequence) return;

            const color = btn.getAttribute("data-color");
            userSequence.push(color);
            updateDots();

            const currentIndex = userSequence.length - 1;
            if (userSequence[currentIndex] !== sequence[currentIndex]) {
                handleFailure();
                return;
            }

            if (userSequence.length === sequence.length) {
                handleStepSuccess();
            }
        });
    });

    function handleFailure() {
        statusText.textContent = "Sbagliato! Si ricomincia da capo. Riprova.";
        statusText.className = "status-text error";
        currentStep = 2; // Reset difficulty on failure
        disableButtons();
        startBtn.disabled = false;
        startBtn.style.opacity = "1";
        startBtn.style.display = "inline-flex";
        startBtn.querySelector("span").textContent = "Riprova";
        resetBtn.style.display = "none";
        completionArea.style.display = "none";
    }

    async function handleStepSuccess() {
        if (currentStep < targetStep) {
            statusText.textContent = "Ottimo! Preparati per il prossimo livello...";
            statusText.className = "status-text success";
            disableButtons();
            currentStep++;
            await sleep(1500);
            generateSequence();
            showSequence();
        } else {
            handleFinalSuccess();
        }
    }

    function handleFinalSuccess() {
        statusText.textContent = "Incredibile! Hai completato l'ultima sequenza.";
        statusText.className = "status-text success";
        disableButtons();
        completionArea.style.display = "block";
        startBtn.style.display = "none";
        resetBtn.style.display = "inline-flex";
    }

    startBtn.addEventListener("click", () => {
        currentStep = 2;
        generateSequence();
        showSequence();
    });

    resetBtn.addEventListener("click", () => {
        currentStep = 2;
        resetDots();
        completionArea.style.display = "none";
        startBtn.style.display = "inline-flex";
        resetBtn.style.display = "none";
        startBtn.disabled = false;
        startBtn.style.opacity = "1";
        startBtn.querySelector("span").textContent = "Inizia Gioco";
        statusText.textContent = "Clicca su 'Inizia' per vedere la prima sequenza";
        statusText.className = "status-text";
    });

    async function completeRiddle() {
        const livello = livelloInput.value;
        const password = atob(P_B64);
        const btnText = completeBtn.querySelector("span");

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
                btnText.textContent = "Invia Soluzione";
            }, 2000);
        }
    }

    completeBtn.addEventListener("click", completeRiddle);
});
