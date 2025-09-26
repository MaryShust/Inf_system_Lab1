const countryMapping = {
    "Германия": "GERMANY",
    "Испания": "SPAIN",
    "Ватикан": "VATICAN",
    "Северная корея": "SOUTH_KOREA",
    "Япония": "JAPAN"
};

const сolorMapping = {
    "Черный": "BLACK",
    "Синий": "BLUE",
    "Желтый": "YELLOW",
    "Оранжевый": "ORANGE",
    "Белый": "WHITE"
};

function createRowInTable(tableBody, key, value) {
    const row = document.createElement('tr');
    row.innerHTML = `
        <td>${key}</td>
        <td>${value}</td>
    `;
    tableBody.appendChild(row);
}

// Функция загрузки локаций
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

// Функция загрузки координат
function loadCoordinates(coordinateSelect) {
    fetch('/coordinates')
        .then(response => {
            if (!response.ok) throw new Error('Ошибка загрузки координат');
            return response.json();
        })
        .then(coordinates => {
            coordinateSelect.innerHTML = ''; // Очистка текущих опций

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

    // Проверка на целое число
    if (!/^\d+$/.test(id)) {
        message.textContent = "ID должен быть целым положительным числом";
        message.className = `${className} error`;
        message.style.display = "block";
        return false;
    }
    return true;
}

function form_person_validation(nameInput, birthdayInput, nationalitySelect, heightInput, hairColorSelect, eyeColorSelect, locationX, locationY, locationZ, coordX, coordY, messageDiv) {
    const errors = [];

    // Очистка предыдущих ошибок
    messageDiv.textContent = '';
    messageDiv.className = 'create-message';

    // Проверка имени
    if (!nameInput.value.trim()) {
        errors.push('Имя не может быть пустым');
    }

    // Проверка даты рождения
    if (!birthdayInput.value) {
        errors.push('Дата рождения обязательна');
    }

    // Проверка национальности
    if (!nationalitySelect.value) {
        errors.push('Выберите национальность');
    }

    // Проверка роста
    if (!heightInput.value || parseInt(heightInput.value) < 1) {
        errors.push('Рост должен быть целым числом ≥ 1');
    }

    // Проверка цвета волос
    if (!hairColorSelect.value) {
        errors.push('Выберите цвет волос');
    }

    // Проверка координат
    if (!coordX.value || !coordY.value) {
        errors.push('Все координаты (X, Y) обязательны');
    } else {
        if (!Number.isInteger(Number(coordX.value))) {
            errors.push('Координата X должна быть целочисленным числом');
        } else {
            if (!Number.isInteger(Number(coordX.value)) || Number(coordX.value) > 674) {
                errors.push('Координата X должна быть не больше 674');
            }
        }
        if (!Number.isInteger(Number(coordY.value))) {
            errors.push('Координата Y должна быть целочисленным числом');
        } else {
            if (!Number.isInteger(Number(coordY.value)) || Number(coordY.value) < -554) {
                errors.push('Координата Y не должна быть меньше -554');
            }
        }
    }

    if (
        (locationX.value && (!locationY.value || !locationZ.value)) ||
        (locationY.value && (!locationX.value || !locationZ.value)) ||
        (locationZ.value && (!locationX.value || !locationY.value))
    ) {
        errors.push('Локация не может быть частичной');
    }

    // Обработка ошибок
    if (errors.length > 0) {
        messageDiv.textContent = errors.join('.\n');
        messageDiv.className = 'create-message error';
        messageDiv.style.display = 'block';

        setTimeout(() => {
            messageDiv.style.display = 'none';
        }, 5000);
    }
    return errors
}
