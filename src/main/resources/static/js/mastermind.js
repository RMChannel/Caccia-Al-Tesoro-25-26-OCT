document.addEventListener("DOMContentLoaded", () => {
    const paletteBtns = document.querySelectorAll(".palette-btn");
    const submitBtn = document.getElementById("submit-guess");
    const clearBtn = document.getElementById("clear-guess");
    const statusMessage = document.getElementById("status-message");
    const completionArea = document.getElementById("completion-area");
    const completeBtn = document.getElementById("complete-btn");
    const livelloInput = document.getElementById("livello-input");

    const colors = ["red", "blue", "green", "yellow", "orange", "purple"];
    const colorHex = {
        'red': '#e54b4b',
        'blue': '#2d8cf0',
        'green': '#2bb673',
        'yellow': '#ffcc00',
        'orange': '#ff9900',
        'purple': '#9b51e0'
    };

    let secretCode = [];
    let currentAttempt = 1;
    let currentGuess = [];
    let isGameOver = false;
    const maxAttempts = 8;
    const slotsCount = 4;

    const P_B64 = "TWFzdGVyTWluZEZ1Y2tzTXlMaWZl";

    function initGame() {
        // Generate random secret code
        secretCode = [];
        for (let i = 0; i < slotsCount; i++) {
            secretCode.push(colors[Math.floor(Math.random() * colors.length)]);
        }
        console.log("Secret Code (for dev):", secretCode);
        isGameOver = false;
        currentAttempt = 1;
        currentGuess = [];
        updateActiveRow();
        setupSlotListeners();
    }

    function setupSlotListeners() {
        // Add listeners to ALL slots, but they will only be active for the current row
        const allSlots = document.querySelectorAll(".slot");
        allSlots.forEach(slot => {
            slot.addEventListener("click", () => {
                const rowId = slot.closest(".attempt-row").id;
                const rowNum = parseInt(rowId.split("-")[1]);
                
                if (rowNum === currentAttempt && !isGameOver) {
                    const slotIndex = parseInt(slot.getAttribute("data-slot"));
                    if (slotIndex < currentGuess.length) {
                        currentGuess.splice(slotIndex, 1);
                        updateCurrentRowUI();
                        updateControls();
                    }
                }
            });
        });
    }

    function updateActiveRow() {
        document.querySelectorAll(".attempt-row").forEach(row => row.classList.remove("active"));
        const activeRow = document.getElementById(`row-${currentAttempt}`);
        if (activeRow) {
            activeRow.classList.add("active");
        }
    }

    function updateControls() {
        if (currentGuess.length === slotsCount) {
            submitBtn.disabled = false;
            submitBtn.classList.remove("disabled");
            statusMessage.textContent = "Tentativo pronto. Conferma?";
        } else {
            submitBtn.disabled = true;
            submitBtn.classList.add("disabled");
            statusMessage.textContent = currentGuess.length > 0 ? "Completa la sequenza..." : "Seleziona i colori per il tuo tentativo";
        }
    }

    paletteBtns.forEach(btn => {
        btn.addEventListener("click", () => {
            if (!isGameOver && currentGuess.length < slotsCount && currentAttempt <= maxAttempts) {
                const color = btn.getAttribute("data-color");
                currentGuess.push(color);
                updateCurrentRowUI();
                updateControls();
            }
        });
    });

    function updateCurrentRowUI() {
        const activeRow = document.getElementById(`row-${currentAttempt}`);
        if (!activeRow) return;
        const slots = activeRow.querySelectorAll(".slot");
        
        slots.forEach((slot, index) => {
            if (currentGuess[index]) {
                slot.style.backgroundColor = colorHex[currentGuess[index]];
                slot.classList.add("filled");
            } else {
                slot.style.backgroundColor = "";
                slot.classList.remove("filled");
            }
        });
    }

    clearBtn.addEventListener("click", () => {
        if (isGameOver && currentAttempt > maxAttempts) {
            window.location.reload();
            return;
        }
        currentGuess = [];
        updateCurrentRowUI();
        updateControls();
    });

    submitBtn.addEventListener("click", () => {
        if (currentGuess.length === slotsCount && !isGameOver) {
            const feedback = checkGuess(currentGuess);
            displayFeedback(feedback);
            
            if (feedback.black === slotsCount) {
                handleWin();
            } else {
                currentAttempt++;
                if (currentAttempt > maxAttempts) {
                    handleLoss();
                } else {
                    currentGuess = [];
                    updateActiveRow();
                    updateControls();
                    statusMessage.textContent = "Tentativo errato. Riprova!";
                    statusMessage.style.color = "var(--ink-soft)";
                }
            }
        }
    });

    function checkGuess(guess) {
        let black = 0;
        let white = 0;
        let secretCopy = [...secretCode];
        let guessCopy = [...guess];

        // First pass: check for correct position and color (black)
        for (let i = 0; i < slotsCount; i++) {
            if (guessCopy[i] === secretCopy[i]) {
                black++;
                secretCopy[i] = null;
                guessCopy[i] = null;
            }
        }

        // Second pass: check for correct color, wrong position (white)
        for (let i = 0; i < slotsCount; i++) {
            if (guessCopy[i] !== null) {
                let foundIndex = secretCopy.indexOf(guessCopy[i]);
                if (foundIndex !== -1) {
                    white++;
                    secretCopy[foundIndex] = null;
                }
            }
        }

        return { black, white };
    }

    function displayFeedback(result) {
        const activeRow = document.getElementById(`row-${currentAttempt}`);
        const dots = activeRow.querySelectorAll(".feedback-dot");
        
        let dotIndex = 0;
        for (let i = 0; i < result.black; i++) {
            dots[dotIndex++].classList.add("correct");
        }
        for (let i = 0; i < result.white; i++) {
            dots[dotIndex++].classList.add("partial");
        }
    }

    function handleWin() {
        isGameOver = true;
        statusMessage.textContent = "Bravo! Hai indovinato il codice segreto!";
        statusMessage.className = "status-message success";
        document.querySelectorAll(".attempt-row").forEach(row => row.classList.remove("active"));
        submitBtn.style.display = "none";
        clearBtn.style.display = "none";
        completionArea.style.display = "block";
    }

    function handleLoss() {
        isGameOver = true;
        statusMessage.textContent = `Hai esaurito i tentativi! Il codice era: ${secretCode.join(", ")}.`;
        statusMessage.className = "status-message error";
        submitBtn.disabled = true;
        submitBtn.classList.add("disabled");
        
        clearBtn.querySelector("span").textContent = "Ricomincia";
        clearBtn.style.background = "var(--accent)";
    }

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

    initGame();
});

