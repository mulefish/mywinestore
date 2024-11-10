

let page = 0; // Keeps track of the current page for pagination

async function fetchAllWines() {
    page = 0; // Reset page counter when fetching all wines
    try {
        const response = await fetch('/api/wines/all');
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        const wines = await response.json();
        displayWines(wines);
    } catch (error) {
        console.error('Fetch error:', error);
    }
}

async function fetchNext10Wines() {
    try {
        const response = await fetch(`/api/wines/next10?page=${page}`);
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        const wines = await response.json();
        displayWines(wines);
        page++; // Increment page for the next "next 10" call
    } catch (error) {
        console.error('Fetch error:', error);
    }
}

async function searchWinesByTopnote() {
    const topnote = document.getElementById('searchTopnote').value;
    try {
        const response = await fetch(`/api/wines/search?topnote=${encodeURIComponent(topnote)}`);
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        const wines = await response.json();
        displayWines(wines);
    } catch (error) {
        console.error('Search error:', error);
    }
}

async function fetchTop10Wines() {
    try {
        const response = await fetch('/api/wines/top10'); // Call the /top10 endpoint
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        const wines = await response.json();
        displayWines(wines);
    } catch (error) {
        console.error('Fetch error:', error);
    }
}

async function submitWineForm() {
    const wineId = document.getElementById('wineId').value;
    const wine = {
        type: document.getElementById('wineType').value,
        variety: document.getElementById('wineVariety').value,
        year: parseInt(document.getElementById('wineYear').value),
        region: document.getElementById('wineRegion').value,
        price: parseInt(document.getElementById('winePrice').value),
        topnote: document.getElementById('wineTopnote').value,
        bottomnote: document.getElementById('wineBottomnote').value,
    };

    try {
        const response = await fetch(wineId ? `/api/wines/${wineId}` : '/api/wines', {
            method: wineId ? 'PUT' : 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(wine),
        });
        if (!response.ok) throw new Error('Failed to save wine');

        clearForm();
        fetchAllWines();
    } catch (error) {
        console.error('Save error:', error);
    }
}

async function deleteWine(id) {
    try {
        const response = await fetch(`/api/wines/${id}`, { method: 'DELETE' });
        if (!response.ok) throw new Error('Failed to delete wine');
        fetchAllWines();
    } catch (error) {
        console.error('Delete error:', error);
    }
}

function editWine(id) {
    fetch(`/api/wines/${id}`)
        .then(response => response.json())
        .then(wine => {
            document.getElementById('wineId').value = wine.id;
            document.getElementById('wineType').value = wine.type;
            document.getElementById('wineVariety').value = wine.variety;
            document.getElementById('wineYear').value = wine.year;
            document.getElementById('wineRegion').value = wine.region;
            document.getElementById('winePrice').value = wine.price;
            document.getElementById('wineTopnote').value = wine.topnote;
            document.getElementById('wineBottomnote').value = wine.bottomnote;
        });
}

function clearForm() {
    document.getElementById('wineForm').reset();
    document.getElementById('wineId').value = '';
}

function displayWines(wines) {
    const winesDiv = document.getElementById('wines');
    winesDiv.innerHTML = ''; // Clear any existing content

    wines.forEach(wine => {
        const wineDiv = document.createElement('div');
        wineDiv.className = 'wine-item';
        wineDiv.innerHTML = `
         <strong>ID:</strong> ${wine.id} <br>
            <strong>Type:</strong> ${wine.type} <br>
            <strong>Variety:</strong> ${wine.variety} <br>
            <strong>Year:</strong> ${wine.year} <br>
            <strong>Region:</strong> ${wine.region} <br>
            <strong>Price:</strong> $${wine.price} <br>
            <strong>Top Note:</strong> ${wine.topnote} <br>
            <strong>Bottom Note:</strong> ${wine.bottomnote} <br>
            <button onclick="editWine(${wine.id})">Edit</button>
            <button onclick="deleteWine(${wine.id})">Delete</button>
        `;
        winesDiv.appendChild(wineDiv);
    });
}
