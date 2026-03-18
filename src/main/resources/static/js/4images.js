document.addEventListener("DOMContentLoaded", () => {
    const wordDisplay = document.getElementById("word-display");
    const solutionInput = document.getElementById("solution-input");
    const verifyBtn = document.getElementById("verify-btn");
    const errorMsg = document.getElementById("error-message");
    const timerDisplay = document.getElementById("timer-display");
    const countdownSpan = document.getElementById("countdown");
    const livelloInput = document.getElementById("livello-input");

    const TARGET_WORD = "CUMULO";
    const P_B64 = "NGltYWdlc0RhbmlDdW11bG9XdGY=";
    
    let timerInterval = null;
    let revealedLetters = new Array(TARGET_WORD.length).fill(false);

    // Initialize word display
    function initWordDisplay() {
        wordDisplay.innerHTML = "";
        for (let i = 0; i < TARGET_WORD.length; i++) {
            const box = document.createElement("div");
            box.classList.add("letter-box");
            if (revealedLetters[i]) {
                box.textContent = TARGET_WORD[i];
                box.classList.add("revealed");
            } else {
                box.textContent = "?";
            }
            wordDisplay.appendChild(box);
        }
    }

    // Timer Logic
    function startTimer(seconds) {
        clearInterval(timerInterval);
        const endTime = Date.now() + seconds * 1000;
        localStorage.setItem("4images_timer_end", endTime);
        
        updateTimer();
        timerInterval = setInterval(updateTimer, 1000);
    }

    function updateTimer() {
        const endTime = localStorage.getItem("4images_timer_end");
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
            verifyBtn.disabled = true;
            solutionInput.disabled = true;
            errorMsg.style.display = "none";
        }
    }

    function stopTimer() {
        clearInterval(timerInterval);
        localStorage.removeItem("4images_timer_end");
        timerDisplay.style.display = "none";
        verifyBtn.disabled = false;
        solutionInput.disabled = false;
        solutionInput.focus();
    }

    // Final completion to backend
    async function completeLevel() {
        const livello = livelloInput.value;
        const password = atob(P_B64);

        verifyBtn.disabled = true;
        verifyBtn.textContent = "Sblocco in corso...";

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
            errorMsg.textContent = "⚠️ Errore di rete. Riprova.";
            errorMsg.style.display = "block";
            verifyBtn.disabled = false;
            verifyBtn.textContent = "Verifica";
        }
    }

    function checkSolution() {
        const userInput = solutionInput.value.trim().toUpperCase();
        
        if (userInput.length === 0) {
            errorMsg.textContent = "⚠️ Inserisci una parola!";
            errorMsg.style.display = "block";
            return;
        }

        if (userInput === TARGET_WORD) {
            // Success! Reveal all and complete
            revealedLetters.fill(true);
            initWordDisplay();
            completeLevel();
        } else {
            // Failure: reveal correct letters in correct positions
            let matchFound = false;
            for (let i = 0; i < TARGET_WORD.length; i++) {
                if (userInput[i] === TARGET_WORD[i]) {
                    revealedLetters[i] = true;
                    matchFound = true;
                }
            }
            
            initWordDisplay();
            errorMsg.textContent = "⚠️ Parola errata! Abbiamo rivelato le lettere corrette.";
            errorMsg.style.display = "block";
            
            // Start 30s penalty
            startTimer(30);
            solutionInput.value = "";
        }
    }

    verifyBtn.addEventListener("click", checkSolution);

    solutionInput.addEventListener("keypress", (e) => {
        if (e.key === "Enter" && !verifyBtn.disabled) {
            checkSolution();
        }
    });

    // Check for active timer on load
    if (localStorage.getItem("4images_timer_end")) {
        updateTimer();
        timerInterval = setInterval(updateTimer, 1000);
    }

    initWordDisplay();

    // Scroll to input on focus to help mobile visibility
    solutionInput.addEventListener("focus", () => {
        setTimeout(() => {
            solutionInput.scrollIntoView({ behavior: "smooth", block: "center" });
        }, 300);
    });
});
