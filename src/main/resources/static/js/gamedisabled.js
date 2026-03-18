document.addEventListener('DOMContentLoaded', function() {
    console.log('Game Disabled page loaded');

    // Poll server to check if game is active again
    async function checkStatus() {
        try {
            // We use a simple fetch to the home page or a specific status endpoint
            // If the game becomes active, the controller will redirect us automatically
            // but we can also check explicitly if we had an endpoint.
            // For now, let's just reload the page every 15 seconds to see if we get redirected
            setTimeout(() => {
                window.location.reload();
            }, 15000);
        } catch (err) {
            console.error('Error checking status:', err);
        }
    }

    checkStatus();
});
