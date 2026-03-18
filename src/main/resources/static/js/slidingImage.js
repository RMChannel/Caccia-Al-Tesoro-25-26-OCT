document.addEventListener("DOMContentLoaded", () => {
    const board = document.getElementById("puzzle-board");
    const movesDisplay = document.getElementById("moves-count");
    const shuffleBtn = document.getElementById("shuffle-btn");
    const completeBtn = document.getElementById("complete-btn");
    const successMsg = document.getElementById("success-message");
    const livelloInput = document.getElementById("livello-input");
    const btnText = completeBtn.querySelector("span");

    // Password: SlidingImageQuadroDani
    const P_B64 = "U2xpZGluZ0ltYWdlUXVhZHJvRGFuaQ==";
    
    let tiles = [0, 1, 2, 3, 4, 5, 6, 7, 8]; // 8 is the empty tile
    let moves = 0;
    let isSolved = false;

    function initGame() {
        renderBoard();
        shuffle();
    }

    function renderBoard() {
        board.innerHTML = "";
        tiles.forEach((tileIndex, position) => {
            const tile = document.createElement("div");
            tile.classList.add("puzzle-tile");
            
            if (tileIndex === 8 && !isSolved) {
                tile.classList.add("empty");
                tile.style.backgroundImage = "none";
            } else {
                const row = Math.floor(tileIndex / 3);
                const col = tileIndex % 3;
                tile.style.backgroundPosition = `${col * 50}% ${row * 50}%`;
                
                // Add a small helper number (only if not solved)
                if (!isSolved) {
                    const number = document.createElement("span");
                    number.classList.add("tile-number");
                    number.textContent = tileIndex + 1;
                    tile.appendChild(number);
                }
            }

            tile.addEventListener("click", () => moveTile(position));
            board.appendChild(tile);
        });
    }

    function moveTile(position) {
        if (isSolved) return;

        const emptyPosition = tiles.indexOf(8);
        if (isAdjacent(position, emptyPosition)) {
            swapTiles(position, emptyPosition);
            moves++;
            movesDisplay.textContent = moves;
            renderBoard();
            checkWin();
        }
    }

    function isAdjacent(pos1, pos2) {
        const row1 = Math.floor(pos1 / 3);
        const col1 = pos1 % 3;
        const row2 = Math.floor(pos2 / 3);
        const col2 = pos2 % 3;

        const rowDiff = Math.abs(row1 - row2);
        const colDiff = Math.abs(col1 - col2);

        return (rowDiff === 1 && colDiff === 0) || (rowDiff === 0 && colDiff === 1);
    }

    function swapTiles(pos1, pos2) {
        const temp = tiles[pos1];
        tiles[pos1] = tiles[pos2];
        tiles[pos2] = temp;
    }

    function shuffle() {
        isSolved = false;
        moves = 0;
        movesDisplay.textContent = moves;
        successMsg.style.display = "none";
        completeBtn.disabled = true;
        completeBtn.classList.add("disabled");

        // Start from solved state
        tiles = [0, 1, 2, 3, 4, 5, 6, 7, 8];

        // Simple shuffle by making random valid moves to ensure solvability
        let currentEmptyPos = 8;
        for (let i = 0; i < 150; i++) {
            const adjacents = getAdjacents(currentEmptyPos);
            const randomPos = adjacents[Math.floor(Math.random() * adjacents.length)];
            swapTiles(currentEmptyPos, randomPos);
            currentEmptyPos = randomPos;
        }
        
        // Ensure it's not solved after shuffle
        if (isWinState()) {
            shuffle();
        } else {
            renderBoard();
        }
    }

    function getAdjacents(pos) {
        const adjs = [];
        const row = Math.floor(pos / 3);
        const col = pos % 3;

        if (row > 0) adjs.push(pos - 3);
        if (row < 2) adjs.push(pos + 3);
        if (col > 0) adjs.push(pos - 1);
        if (col < 2) adjs.push(pos + 1);

        return adjs;
    }

    function isWinState() {
        return tiles.every((tile, index) => tile === index);
    }

    function checkWin() {
        if (isWinState()) {
            isSolved = true;
            // Re-render to show full image and remove numbers
            setTimeout(() => {
                renderBoard();
                successMsg.style.display = "block";
                completeBtn.disabled = false;
                completeBtn.classList.remove("disabled");
            }, 200);
        }
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
                btnText.textContent = "Verifica Puzzle";
            }, 2000);
        }
    }

    shuffleBtn.addEventListener("click", shuffle);
    completeBtn.addEventListener("click", completeRiddle);

    initGame();
});
