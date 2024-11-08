const apiBaseUrl = "http://localhost:8080/api/wines"; // Adjust as needed

// Function to GET all wines
function getAllWines() {
    fetch(apiBaseUrl)
        .then(response => response.json())
        .then(data => {
            document.getElementById("all-wines-output").innerText = JSON.stringify(data, null, 2);
        })
        .catch(error => console.error("Error fetching all wines:", error));
}

// Function to GET wine by ID
function getWineById() {
    const wineId = document.getElementById("wine-id").value;
    fetch(`${apiBaseUrl}/${wineId}`)
        .then(response => response.json())
        .then(data => {
            document.getElementById("wine-by-id-output").innerText = JSON.stringify(data, null, 2);
        })
        .catch(error => console.error("Error fetching wine by ID:", error));
}

// Function to POST create a new wine
function createWine() {
    const name = document.getElementById("new-wine-name").value;
    const year = document.getElementById("new-wine-year").value;
    const price = document.getElementById("new-wine-price").value;
    const region = document.getElementById("new-wine-region").value;

    fetch(apiBaseUrl, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ name, year, price, region })
    })
        .then(response => response.json())
        .then(data => {
            document.getElementById("create-wine-output").innerText = "Wine created: " + JSON.stringify(data, null, 2);
        })
        .catch(error => console.error("Error creating wine:", error));
}

// Function to DELETE a wine by ID
function deleteWine() {
    const wineId = document.getElementById("delete-wine-id").value;
    fetch(`${apiBaseUrl}/${wineId}`, {
        method: "DELETE"
    })
        .then(() => {
            document.getElementById("delete-wine-output").innerText = `Wine with ID ${wineId} deleted.`;
        })
        .catch(error => console.error("Error deleting wine:", error));
}

