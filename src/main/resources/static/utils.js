function createRowInTable(tableBody, key, value) {
    const row = document.createElement('tr');
    row.innerHTML = `
        <td>${key}</td>
        <td>${value}</td>
    `;
    tableBody.appendChild(row);
}

function loadLocations(locationSelect) {
    fetch('/locations')
        .then(response => {
            if (!response.ok) throw new Error('Ошибка загрузки локаций');
            return response.json();
        })
        .then(locations => {
            locationSelect.innerHTML = ''; // Очистка текущих опций

            locations.forEach(loc => {
                const option = document.createElement('option');
                option.value = `${loc.x},${loc.y},${loc.z}`;
                option.textContent = `x: ${loc.x}, y: ${loc.y}, z: ${loc.z}`;
                locationSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Ошибка:', error);
            const option = document.createElement('option');
            option.textContent = 'Не удалось загрузить локации';
            option.disabled = true;
            locationSelect.appendChild(option);
        });
}

function loadCoordinates(coordinateSelect) {
    fetch('/coordinates')
        .then(response => {
            if (!response.ok) throw new Error('Ошибка загрузки координат');
            return response.json();
        })
        .then(coordinates => {
            coordinateSelect.innerHTML = '';

            coordinates.forEach(coordinate => {
                const option = document.createElement('option');
                option.value = `${coordinate.x},${coordinate.y}`;
                option.textContent = `x: ${coordinate.x}, y: ${coordinate.y}`;
                coordinateSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Ошибка:', error);
            const option = document.createElement('option');
            option.textContent = 'Не удалось загрузить координаты';
            option.disabled = true;
            coordinateSelect.appendChild(option);
        });
}

function isValidEditText(id, message, className) {
    if (!id) {
        message.textContent = "Введите ID для удаления";
        message.className = `${className} error`;
        message.style.display = "block";
        return false;
    }
    if (!/^\d+$/.test(id)) {
        message.textContent = "ID должен быть целым положительным числом";
        message.className = `${className} error`;
        message.style.display = "block";
        return false;
    }
    return true;
}