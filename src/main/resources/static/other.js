document.addEventListener("DOMContentLoaded", function () {
    // Найти вкладку "Другое"
    const otherTab = document.querySelector('[data-tab="other"]');

    // Добавить обработчик клика
    otherTab.addEventListener("click", function () {
        // Проверить, активна ли вкладка (если нужно)
        if (!otherTab.classList.contains("active")) return;

        handleOtherTabClick();
    });
});

document.getElementById('max-birthday-button').addEventListener('click', () => {
    handleMaxBirthdayClick();
});
document.getElementById('tall-people-button').addEventListener('click', () => {
    handleTallPeopleClick();
});
document.getElementById('count-by-hair-color-button').addEventListener('click', () => {
    handleCountByHairColorClick();
});
document.getElementById('count-by-hair-color-in-location-button').addEventListener('click', () => {
    handleCountByHairColorInLocationClick();
});

function handleOtherTabClick() {
    const averageHeight = document.getElementById('average-height-message');

    fetch("/average-height")
        .then(response => {
            if (response.ok) {
                return response.json().then(result => {
                    averageHeight.textContent = result;
                    averageHeight.className = "average-height-message success";
                });
            } else {
                return response.text().then(errorMessage => {
                    averageHeight.textContent = errorMessage;
                    averageHeight.className = "average-height-message error";
                });
            }
        })
        .catch(error => {
            // Обработка сетевых ошибок
            averageHeight.textContent = "Произошла сетевая ошибка: " + error.message;
            averageHeight.className = "average-height-message error";
        });
}

function handleMaxBirthdayClick() {
    const table = document.getElementById('max-birthday-person-table');
    const body = document.getElementById('max-birthday-person-body');
    const maxBirthdayMessage = document.getElementById('max-birthday-message');

    fetch("/max-birthday")
        .then(response => {
            if (response.ok) {
                return response.json().then(person => {
                    body.innerHTML = '';
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${person.id}</td>
                        <td>${person.name}</td>
                        <td>${person.birthday}</td>
                        <td>${person.nationality}</td>
                        <td>${person.height}</td>
                        <td>${person.hairColor}</td>
                        <td>${person.eyeColor}</td>
                        <td>${person.coordinates.x}</td>
                        <td>${person.coordinates.y}</td>
                        <td>${person.location?.x ?? "-"}</td>
                        <td>${person.location?.y ?? "-"}</td>
                        <td>${person.location?.z ?? "-"}</td>
                        <td>${person.creationDate}</td>
                    `;
                    body.appendChild(row);

                    table.style.display = "block";
                    maxBirthdayMessage.style.display = "none";
                });
            } else {
                return response.text().then(errorMessage => {
                    maxBirthdayMessage.textContent = errorMessage;
                    maxBirthdayMessage.className = "max-birthday-message error";
                    maxBirthdayMessage.style.display = "block";
                });
            }
        })
        .catch(error => {
            // Обработка сетевых ошибок
            maxBirthdayMessage.textContent = "Произошла сетевая ошибка: " + error.message;
            maxBirthdayMessage.className = "max-birthday-message error";
            maxBirthdayMessage.style.display = "block";
        });
}

function handleTallPeopleClick() {
    const tallPeopleInput = document.getElementById('tall-people-input');
    const table = document.getElementById('tall-people-table');
    const body = document.getElementById('tall-people-body');
    const tallPeopleMessage = document.getElementById('tall-people-message');
    body.innerHTML = '';

    const height = tallPeopleInput.value.trim()

    if (height == "") {
        tallPeopleMessage.textContent = "Введите значение";
        tallPeopleMessage.className = "tall-people-message error";
        tallPeopleMessage.style.display = "block";
        table.style.display = "none";
        return;
    }
    const url = `/tall-people?minHeight=${height}`;
    fetch(url)
        .then(response => {
            if (response.ok) {
                return response.json().then(persons => {
                    persons.forEach(person => {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>${person.id}</td>
                            <td>${person.name}</td>
                            <td>${person.birthday}</td>
                            <td>${person.nationality}</td>
                            <td>${person.height}</td>
                            <td>${person.hairColor}</td>
                            <td>${person.eyeColor}</td>
                            <td>${person.coordinates.x}</td>
                            <td>${person.coordinates.y}</td>
                            <td>${person.location?.x ?? "-"}</td>
                            <td>${person.location?.y ?? "-"}</td>
                            <td>${person.location?.z ?? "-"}</td>
                            <td>${person.creationDate}</td>
                        `;
                        body.appendChild(row);
                    });

                    table.style.display = "block";
                    tallPeopleMessage.style.display = "none";
                });
            } else {
                return response.text().then(errorMessage => {
                    tallPeopleMessage.textContent = errorMessage;
                    tallPeopleMessage.className = "tall-people-message error";
                    tallPeopleMessage.style.display = "block";
                    table.style.display = "none";
                });
            }
        })
        .catch(error => {
            // Обработка сетевых ошибок
            tallPeopleMessage.textContent = "Произошла сетевая ошибка: " + error.message;
            tallPeopleMessage.className = "tall-people-message error";
            tallPeopleMessage.style.display = "block";
            table.style.display = "none";
        });
}

function handleCountByHairColorClick() {
    const countByHairColorMessage = document.getElementById('count-by-hair-color-message');
    const countByHairColorInput = document.getElementById('count-by-hair-color_input');

    const color = countByHairColorInput.value;

    if (!color) {
        countByHairColorMessage.textContent = "выберите цвет";
        countByHairColorMessage.className = "count-by-hair-color-message error";
        return;
    }
    const url = `/count-by-hair-color?hairColor=${color}`;
    fetch(url)
        .then(response => {
            if (response.ok) {
                return response.json().then(result => {
                    countByHairColorMessage.textContent = result;
                    countByHairColorMessage.className = "count-by-hair-color-message success";
                });
            } else {
                return response.text().then(errorMessage => {
                    countByHairColorMessage.textContent = errorMessage;
                    countByHairColorMessage.className = "count-by-hair-color-message error";
                });
            }
        })
        .catch(error => {
            countByHairColorMessage.textContent = "произошла сетевая ошибка: " + error.message;
            countByHairColorMessage.className = "count-by-hair-color-message error";
        });
}

function handleCountByHairColorInLocationClick() {
    const countByHairColorMessage = document.getElementById('count-by-hair-color-in-location-message');
    const color = document.getElementById('count-by-hair-color-in-location_input').value;
    const xMin = Number(document.getElementById('count-by-hair-color-in-location_min_location_x_input').value);
    const xMax = Number(document.getElementById('count-by-hair-color-in-location_max_location_x_input').value);
    const yMin = Number(document.getElementById('count-by-hair-color-in-location_min_location_y_input').value);
    const yMax = Number(document.getElementById('count-by-hair-color-in-location_max_location_y_input').value);
    const zMin = Number(document.getElementById('count-by-hair-color-in-location_min_location_z_input').value);
    const zMax = Number(document.getElementById('count-by-hair-color-in-location_max_location_z_input').value);

    if (!color || !xMin || !xMax || !yMin || !yMax || !zMin || !zMax) {
        countByHairColorMessage.textContent = "заполните поля";
        countByHairColorMessage.className = "count-by-hair-color-in-location-message error";
        return;
    }
    if (xMin > xMax) {
        countByHairColorMessage.textContent = "мин x должно быть меньше чем макс x";
        countByHairColorMessage.className = "count-by-hair-color-in-location-message error";
        return;
    }
    if (yMin > yMax) {
        countByHairColorMessage.textContent = "мин y должно быть меньше чем макс y";
        countByHairColorMessage.className = "count-by-hair-color-in-location-message error";
        return;
    }
    if (zMin > zMax) {
        countByHairColorMessage.textContent = "мин z должно быть меньше чем макс z";
        countByHairColorMessage.className = "count-by-hair-color-in-location-message error";
        return;
    }

    const url = `/count-by-hair-color-in-location?hairColor=${color}&xMin=${xMin}&xMax=${xMax}&yMin=${yMin}&yMax=${yMax}&zMin=${zMin}&zMax=${zMax}`;
    fetch(url)
        .then(response => {
            if (response.ok) {
                return response.json().then(result => {
                    countByHairColorMessage.textContent = result;
                    countByHairColorMessage.className = "count-by-hair-color-in-location-message success";
                });
            } else {
                return response.text().then(errorMessage => {
                    countByHairColorMessage.textContent = errorMessage;
                    countByHairColorMessage.className = "count-by-hair-color-in-location-message error";
                });
            }
        })
        .catch(error => {
            countByHairColorMessage.textContent = "произошла сетевая ошибка: " + error.message;
            countByHairColorMessage.className = "count-by-hair-color-in-location-message error";
        });
}
