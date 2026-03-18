document.addEventListener("DOMContentLoaded", () => {
    const towers = document.querySelectorAll(".tower");
    const moveCountDisplay = document.getElementById("move-count");
    const resetBtn = document.getElementById("reset-btn");
    const completeBtn = document.getElementById("complete-btn");

    let selectedTower = null;
    let moveCount = 0;
    const DISK_COUNT = 6;

    // Initialize the game
    function initGame() {
        // Clear all towers
        towers.forEach(tower => {
            const disks = tower.querySelectorAll(".disk");
            disks.forEach(disk => disk.remove());
        });

        // Add disks to the first tower
        const firstTower = document.getElementById("tower-1");
        for (let i = DISK_COUNT; i >= 1; i--) {
            const disk = document.createElement("div");
            disk.className = `disk disk-${i}`;
            disk.dataset.size = i;
            firstTower.appendChild(disk);
        }

        // Reset stats
        moveCount = 0;
        moveCountDisplay.textContent = moveCount;
        selectedTower = null;
        towers.forEach(t => t.classList.remove("selected"));
        completeBtn.disabled = true;
        completeBtn.classList.add("disabled");
    }

    function selectTower(tower) {
        if (selectedTower === null) {
            // No tower selected, try to select the top disk of this tower
            const topDisk = tower.querySelector(".disk:last-of-type");
            if (topDisk) {
                selectedTower = tower;
                tower.classList.add("selected");
                topDisk.classList.add("moving");
            }
        } else if (selectedTower === tower) {
            // Deselect the current tower
            const topDisk = tower.querySelector(".disk:last-of-type");
            if (topDisk) topDisk.classList.remove("moving");
            tower.classList.remove("selected");
            selectedTower = null;
        } else {
            // Try to move disk to this tower
            moveDisk(selectedTower, tower);
        }
    }

    function moveDisk(fromTower, toTower) {
        const diskToMove = fromTower.querySelector(".disk:last-of-type");
        const topDiskOnTarget = toTower.querySelector(".disk:last-of-type");

        const sizeToMove = parseInt(diskToMove.dataset.size);
        const targetSize = topDiskOnTarget ? parseInt(topDiskOnTarget.dataset.size) : Infinity;

        if (sizeToMove < targetSize) {
            // Valid move
            diskToMove.classList.remove("moving");
            toTower.appendChild(diskToMove);
            moveCount++;
            moveCountDisplay.textContent = moveCount;

            // Check win condition
            checkWin();
        } else {
            // Invalid move
            alert("Non puoi mettere un disco più grande sopra uno più piccolo!");
            diskToMove.classList.remove("moving");
        }

        // Clear selection
        fromTower.classList.remove("selected");
        selectedTower = null;
    }

    function checkWin() {
        const tower3 = document.getElementById("tower-3");
        const disksOnTower3 = tower3.querySelectorAll(".disk");

        if (disksOnTower3.length === DISK_COUNT) {
            // Verify that they are in correct order (though it should be impossible otherwise given the rules)
            let isCorrect = true;
            disksOnTower3.forEach((disk, index) => {
                if (parseInt(disk.dataset.size) !== DISK_COUNT - index) {
                    isCorrect = false;
                }
            });

            if (isCorrect) {
                completeBtn.disabled = false;
                completeBtn.classList.remove("disabled");
                alert("Complimenti! Hai risolto la Torre di Hanoi!");
            }
        }
    }

    // Final verification function for the button
    function verifyCompletion() {
        const tower3 = document.getElementById("tower-3");
        const disksOnTower3 = tower3.querySelectorAll(".disk");

        if (disksOnTower3.length === DISK_COUNT) {
            let isCorrect = true;
            disksOnTower3.forEach((disk, index) => {
                if (parseInt(disk.dataset.size) !== DISK_COUNT - index) {
                    isCorrect = false;
                }
            });
            return isCorrect;
        }
        return false;
    }

    towers.forEach(tower => {
        tower.addEventListener("click", () => selectTower(tower));
    });

    resetBtn.addEventListener("click", initGame);

    // Obfuscated password in Base64
    const P_B64 = "SGFub2lUb3dlckNvZGUxNTYzMjk0MTlPQ1Q=";

    async function completeRiddle() {
        const livello = document.getElementById("livello-input").value;
        const password = atob(P_B64);
        const errorMsg = document.getElementById("error-message");

        // Hide previous errors
        errorMsg.style.display = "none";
        completeBtn.disabled = true;
        completeBtn.classList.add("disabled");
        completeBtn.textContent = "Salvataggio...";

        try {
            const formData = new URLSearchParams();
            formData.append("livello", livello);
            formData.append("password", password);

            const response = await fetch("/caccia-al-tesoro/quesito", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: formData
            });

            if (response.redirected) {
                // Check if we were redirected back to the riddle with an error
                if (response.url.includes("error=err")) {
                    errorMsg.textContent = "⚠️ Risposta errata o sessione scaduta. Riprova.";
                    errorMsg.style.display = "block";
                    completeBtn.disabled = false;
                    completeBtn.classList.remove("disabled");
                    completeBtn.textContent = "Completato";
                } else {
                    // Redirect to the final destination (usually /caccia)
                    window.location.href = response.url;
                }
            } else {
                // If not redirected but response is OK, it might be a weird case
                if (response.ok) {
                    window.location.href = "/caccia-al-tesoro/caccia?unlockQuesito=true";
                } else {
                    throw new Error("Errore di rete");
                }
            }
        } catch (error) {
            console.error("Errore:", error);
            errorMsg.textContent = "⚠️ Si è verificato un errore durante l'invio. Riprova tra poco.";
            errorMsg.style.display = "block";
            completeBtn.disabled = false;
            completeBtn.classList.remove("disabled");
            completeBtn.textContent = "Completato";
        }
    }

    completeBtn.addEventListener("click", () => {
        if (verifyCompletion()) {
            completeRiddle();
        } else {
            alert("Il gioco non è ancora completato!");
        }
    });

    // Start game
    initGame();
});
