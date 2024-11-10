async function fetchAllWines() {
    try {
        const response = await fetch('/api/wines/all'); // Call the /all endpoint
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        const wines = await response.json();
        displayWines(wines);
    } catch (error) {
        console.error('Fetch error:', error);
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
