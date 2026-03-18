document.addEventListener("DOMContentLoaded", () => {
    const sudokuGrid = document.getElementById("sudoku-grid");
    const completeBtn = document.getElementById("complete-btn");
    const btnText = completeBtn.querySelector("span");
    const timerDisplay = document.getElementById("timer-display");
    const countdownSpan = document.getElementById("countdown");
    const errorMsg = document.getElementById("error-message");
    const livelloInput = document.getElementById("livello-input");

    const P_B64 = "U3Vkb2t1RG9JdFllYWg=";

    // Sudoku Puzzle (38 clues)
    // 0 means empty
    const puzzle = [
        [9, 0, 0, 5, 0, 8, 0, 0, 7],
        [0, 8, 0, 3, 0, 2, 9, 0, 5],
        [0, 5, 4, 0, 0, 0, 0, 8, 0],
        [0, 7, 0, 6, 8, 0, 0, 3, 2],
        [1, 0, 0, 0, 0, 4, 0, 0, 8],
        [5, 0, 0, 2, 1, 9, 0, 6, 0],
        [0, 0, 0, 9, 0, 6, 0, 0, 1],
        [7, 2, 6, 0, 0, 1, 0, 4, 0],
        [0, 0, 1, 4, 7, 0, 0, 5, 6]
    ];

    const solution = [
        [9, 1, 3, 5, 6, 8, 4, 2, 7],
        [6, 8, 7, 3, 4, 2, 9, 1, 5],
        [2, 5, 4, 1, 9, 7, 6, 8, 3],
        [4, 7, 9, 6, 8, 5, 1, 3, 2],
        [1, 6, 2, 7, 3, 4, 5, 9, 8],
        [5, 3, 8, 2, 1, 9, 7, 6, 4],
        [3, 4, 5, 9, 2, 6, 8, 7, 1],
        [7, 2, 6, 8, 5, 1, 3, 4, 9],
        [8, 9, 1, 4, 7, 3, 2, 5, 6]
    ];

    let timerInterval = null;

    function initSudoku() {
        sudokuGrid.innerHTML = "";
        for (let r = 0; r < 9; r++) {
            for (let c = 0; c < 9; c++) {
                const cell = document.createElement("div");
                cell.classList.add("sudoku-cell");
                
                if (puzzle[r][c] !== 0) {
                    cell.textContent = puzzle[r][c];
                    cell.classList.add("fixed");
                } else {
                    const input = document.createElement("input");
                    input.type = "text";
                    input.inputMode = "numeric";
                    input.pattern = "[1-9]";
                    input.maxLength = 1;
                    input.dataset.row = r;
                    input.dataset.col = c;
                    
                    input.addEventListener("input", (e) => {
                        if (!/^[1-9]$/.test(e.target.value)) {
                            e.target.value = "";
                        }
                    });
                    
                    cell.appendChild(input);
                }
                sudokuGrid.appendChild(cell);
            }
        }
    }

    function checkSolution() {
        const inputs = sudokuGrid.querySelectorAll("input");
        let allFilled = true;
        
        // Create current grid
        const currentGrid = JSON.parse(JSON.stringify(puzzle));
        inputs.forEach(input => {
            const r = parseInt(input.dataset.row);
            const c = parseInt(input.dataset.col);
            const val = parseInt(input.value);
            if (isNaN(val)) {
                allFilled = false;
            } else {
                currentGrid[r][c] = val;
            }
        });

        if (!allFilled) {
            alert("Compila tutte le celle della griglia!");
            return false;
        }

        for (let r = 0; r < 9; r++) {
            for (let c = 0; c < 9; c++) {
                if (currentGrid[r][c] !== solution[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Gestione Timer
    function startTimer(seconds) {
        clearInterval(timerInterval);
        const endTime = Date.now() + seconds * 1000;
        localStorage.setItem("sudoku_timer_end", endTime);
        
        updateTimer();
        timerInterval = setInterval(updateTimer, 1000);
    }

    function updateTimer() {
        const endTime = localStorage.getItem("sudoku_timer_end");
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
            errorMsg.style.display = "none";
        }
    }

    function stopTimer() {
        clearInterval(timerInterval);
        localStorage.removeItem("sudoku_timer_end");
        timerDisplay.style.display = "none";
        completeBtn.disabled = false;
        completeBtn.classList.remove("disabled");
        btnText.textContent = "Verifica Risposta";
    }

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
        if (checkSolution()) {
            completeRiddle();
        } else {
            errorMsg.style.display = "block";
            startTimer(30);
        }
    });

    if (localStorage.getItem("sudoku_timer_end")) {
        updateTimer();
        timerInterval = setInterval(updateTimer, 1000);
    }

    initSudoku();
});
