document.addEventListener("DOMContentLoaded", () => {
  const modal = document.querySelector("[data-login-modal]");
  const openButtons = document.querySelectorAll("[data-open-login]");
  const closeButtons = document.querySelectorAll("[data-close-login]");

  const openModal = () => {
    if (!modal) return;
    modal.classList.add("show");
    modal.setAttribute("aria-hidden", "false");
    document.body.style.overflow = "hidden";
  };

  const closeModal = () => {
    if (!modal) return;
    modal.classList.remove("show");
    modal.setAttribute("aria-hidden", "true");
    document.body.style.overflow = "";
  };

  openButtons.forEach((button) => button.addEventListener("click", openModal));
  closeButtons.forEach((button) => button.addEventListener("click", closeModal));

  window.addEventListener("keydown", (event) => {
    if (event.key === "Escape") closeModal();
  });

  if (new URLSearchParams(window.location.search).has("error")) {
    openModal();
  }

  // Password Toggle Logic
  const togglePassword = document.querySelector("#togglePassword");
  const passwordInput = document.querySelector("#password");
  const eyeIcon = document.querySelector("#eyeIcon");

  if (togglePassword && passwordInput && eyeIcon) {
    togglePassword.addEventListener("click", () => {
      const isPassword = passwordInput.type === "password";
      passwordInput.type = isPassword ? "text" : "password";
      
      const currentSrc = eyeIcon.src;
      if (isPassword) {
        eyeIcon.src = currentSrc.replace("eye.png", "hidden.png");
        togglePassword.setAttribute("aria-label", "Nascondi password");
      } else {
        eyeIcon.src = currentSrc.replace("hidden.png", "eye.png");
        togglePassword.setAttribute("aria-label", "Mostra password");
      }
    });
  }
});
