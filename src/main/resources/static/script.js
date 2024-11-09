async function fetchWines() {
    try {
        const response = await fetch('/api/wines');
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        const wines = await response.json();
        displayWines(wines);
    } catch (error) {
        console.error('Fetch error:', error);
    }
}

function displayWines(wines) {
    const winesDiv = document.getElementById('wines');
    winesDiv.innerHTML = ''; // Clear any existing content

    wines.forEach(wine => {
        const wineDiv = document.createElement('div');
        wineDiv.className = 'wine-item';
        wineDiv.innerHTML = `
            <strong>Type:</strong> ${wine.type} <br>
            <strong>Variety:</strong> ${wine.variety} <br>
            <strong>Year:</strong> ${wine.year} <br>
            <strong>Region:</strong> ${wine.region} <br>
            <strong>Price:</strong> $${wine.price} <br>
            <strong>Top Note:</strong> ${wine.topnote} <br>
            <strong>Bottom Note:</strong> ${wine.bottomnote}
        `;
        winesDiv.appendChild(wineDiv);
    });
}
