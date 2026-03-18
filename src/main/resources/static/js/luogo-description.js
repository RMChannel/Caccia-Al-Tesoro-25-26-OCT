document.addEventListener('DOMContentLoaded', () => {
    console.log('Pagina descrizione luogo caricata');
    
    // Possibile aggiunta di animazioni all'ingresso
    const card = document.querySelector('.description-card');
    if (card) {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            card.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, 100);
    }
});
