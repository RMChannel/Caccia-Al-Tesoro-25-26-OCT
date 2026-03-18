document.addEventListener("DOMContentLoaded", () => {
    const solutionInput = document.getElementById("solution-input");
    const verifyBtn = document.getElementById("verify-btn");
    const errorMsg = document.getElementById("error-message");
    const livelloInput = document.getElementById("livello-input");

    const completeRiddle = async (solution) => {
        const livello = livelloInput.value;
        
        // Hide previous errors
        errorMsg.style.display = "none";
        verifyBtn.disabled = true;
        verifyBtn.textContent = "Verifica in corso...";

        try {
            const formData = new URLSearchParams();
            formData.append("livello", livello);
            formData.append("password", solution.trim());

            const response = await fetch("/caccia-al-tesoro/quesito", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: formData
            });

            if (response.redirected) {
                // If we get redirected to /quesito?error=err, it means the password was wrong
                if (response.url.includes("error=err")) {
                    errorMsg.textContent = "⚠️ Soluzione errata. Riprova!";
                    errorMsg.style.display = "block";
                    verifyBtn.disabled = false;
                    verifyBtn.textContent = "Verifica";
                    solutionInput.focus();
                } else {
                    // Redirect to the success destination (dashboard)
                    window.location.href = response.url;
                }
            } else {
                if (response.ok) {
                    window.location.href = "/caccia?unlockQuesito=true";
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
            errorMsg.textContent = "⚠️ Inserisci una soluzione!";
            errorMsg.style.display = "block";
        }
    });

    // Allow submission with Enter key
    solutionInput.addEventListener("keypress", (e) => {
        if (e.key === "Enter") {
            const solution = solutionInput.value;
            if (solution.trim().length > 0) {
                completeRiddle(solution);
            }
        }
    });
});
