document.getElementById('find-input').addEventListener('keydown', (e) => {
    if (e.key === 'Enter') {
        document.getElementById('find-button').click();
    }
});

document.getElementById('find-button').addEventListener('click', () => {
    const findInput = document.getElementById('find-input');
    const table = document.getElementById('person-table');
    const tableBody = document.getElementById('person-table-body');
    const findMessage = document.getElementById('find-message');
    const id = findInput.value.trim();

    findMessage.style.display = "none";
    table.style.display = "none";

    if (!isValidEditText(id, findMessage, "find-message")) {
        return;
    }

    findInput.value = ''; // Очистка поля после успешной отправки

    const url = `/find_person?id=${id}`;
    fetch(url)
        .then(response => {
            if (response.ok) {
                return response.json().then(person => {
                    table.style.display = "block";
                    tableBody.innerHTML = '';
                    createRowInTable(tableBody, "ID", person.id)
                    createRowInTable(tableBody, "Имя", person.name)
                    createRowInTable(tableBody, "Дата рождения", person.birthday)
                    createRowInTable(tableBody, "Национальность", person.nationality)
                    createRowInTable(tableBody, "Рост", person.height)
                    createRowInTable(tableBody, "Цвет волос", person.hairColor)
                    createRowInTable(tableBody, "Цвет глаз", person.eyeColor)
                    createRowInTable(tableBody, "Координата x", person.coordinates.x)
                    createRowInTable(tableBody, "Координата y", person.coordinates.y)
                    createRowInTable(tableBody, "Локация x", person.location?.x ?? "-")
                    createRowInTable(tableBody, "Локация y", person.location?.y ?? "-")
                    createRowInTable(tableBody, "Локация z", person.location?.z ?? "-")
                    createRowInTable(tableBody, "Дата создания", person.creationDate)
                });
            } else {
                // Обработка ошибки
                return response.text().then(errorMessage => {
                    findMessage.textContent = errorMessage;
                    findMessage.className = "find-message error";
                    findMessage.style.display = "block";

                    setTimeout(() => {
                        findMessage.style.display = "none";
                    }, 5000);
                });
            }
        })
        .catch(error => {
            // Обработка сетевых ошибок
            findMessage.textContent = "Произошла сетевая ошибка: " + error.message;
            findMessage.className = "find-message error";
            findMessage.style.display = "block";

            setTimeout(() => {
                findMessage.style.display = "none";
            }, 5000);
        });
});