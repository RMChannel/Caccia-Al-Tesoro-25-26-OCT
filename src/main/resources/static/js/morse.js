document.addEventListener("DOMContentLoaded", () => {
    const solutionInput = document.getElementById("solution-input");
    const verifyBtn = document.getElementById("verify-btn");
    const errorMsg = document.getElementById("error-message");
    const livelloInput = document.getElementById("livello-input");

    const completeRiddle = async (solution) => {
        const livello = livelloInput.value;
        const contextPath = "/caccia-al-tesoro";
        
        // Hide previous errors
        errorMsg.style.display = "none";
        verifyBtn.disabled = true;
        verifyBtn.textContent = "Verifica in corso...";

        try {
            const formData = new URLSearchParams();
            formData.append("livello", livello);
            formData.append("password", solution.trim());

            const response = await fetch(`${contextPath}/quesito`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: formData
            });

            if (response.redirected) {
                if (response.url.includes("error=err")) {
                    errorMsg.textContent = "⚠️ Codice errato. Riprova!";
                    errorMsg.style.display = "block";
                    verifyBtn.disabled = false;
                    verifyBtn.textContent = "Verifica";
                    solutionInput.focus();
                } else {
                    window.location.href = response.url;
                }
            } else {
                if (response.ok) {
                    window.location.href = `${contextPath}/caccia?unlockQuesito=true`;
                } else {
                    throw new Error("Errore di rete");
                }
            }
        } catch (error) {
            console.error("Errore:", error);
            errorMsg.textContent = "⚠️ Si è verificato un errore durante l'invio. Riprova.";
            errorMsg.style.display = "block";
            verifyBtn.disabled = false;
            verifyBtn.textContent = "Verifica";
        }
    };

    verifyBtn.addEventListener("click", () => {
        const solution = solutionInput.value;
        if (solution.trim().length > 0) {
            completeRiddle(solution);
        } else {
            errorMsg.textContent = "⚠️ Inserisci il codice!";
            errorMsg.style.display = "block";
        }
    });

    solutionInput.addEventListener("keypress", (e) => {
        if (e.key === "Enter") {
            const solution = solutionInput.value;
            if (solution.trim().length > 0) {
                completeRiddle(solution);
            }
        }
    });
});
