const modal = document.getElementById('edit-modal');
let editPersonId = null;

document.addEventListener('DOMContentLoaded', () => {
    const tableBody = document.getElementById('persons-table-body');

    tableBody.addEventListener('click', (event) => {
        const editCell = event.target.closest('td[data-id]');
        if (editCell) {
            const personId = editCell.getAttribute('data-id');
            openEditModal(personId);
        }
    });
});

// Закрытие модального окна при клике вне контента (через backdrop)
modal.addEventListener('click', (event) => {
    // Проверяем, кликнули ли мы по backdrop
    if (event.target === modal) {
        modal.close();
    }
});

document.getElementById('close-modal-button').addEventListener('click', () => {
    modal.close();
});

function openEditModal(personId) {
    modal.showModal();
    editPersonId = personId;

    const url = `/find_person?id=${personId}`;
    fetch(url)
        .then(response => {
            if (response.ok) {
                return response.json().then(person => {
                    const locationSelect = document.getElementById('edit_location_select');
                    const coordinateSelect = document.getElementById('edit_coordinate_select');
                    const xInputL = document.getElementById('edit_location_x_input');
                    const yInputL = document.getElementById('edit_location_y_input');
                    const zInputL = document.getElementById('edit_location_z_input');
                    const xInputC = document.getElementById('edit_coordinate_x_input');
                    const yInputC = document.getElementById('edit_coordinate_y_input');

                    document.getElementById('edit_name_input').value = person.name || '';
                    document.getElementById('edit_birthday_input').value = person.birthday || '';
                    document.getElementById('edit_nationality_input').value = countryMapping[person.nationality] || '';
                    document.getElementById('edit_height_input').value = person.height || '';
                    document.getElementById('edit_hair_color_input').value = сolorMapping[person.hairColor] || '';
                    document.getElementById('edit_eye_color_input').value = сolorMapping[person.eyeColor] || '';

                    // Координаты
                    if (person.coordinates) {
                        xInputC.value = person.coordinates.x || '';
                        yInputC.value = person.coordinates.y || '';
                    }

                    // Локация
                    if (person.location) {
                        xInputL.value = person.location.x || '';
                        yInputL.value = person.location.y || '';
                        zInputL.value = person.location.z || '';
                    }
                    document.getElementById('edit_creation_date').textContent = person.creationDate || '';


                    loadLocations(locationSelect);
                    loadCoordinates(coordinateSelect);

                    locationSelect.addEventListener('change', () => {
                        const [x, y, z] = locationSelect.value.split(',').map(Number);
                        if (!isNaN(x)) xInputL.value = x;
                        if (!isNaN(y)) yInputL.value = y;
                        if (!isNaN(z)) zInputL.value = z;
                    });

                    // Обработчик выбора локации
                    coordinateSelect.addEventListener('change', () => {
                        const [x, y] = coordinateSelect.value.split(',').map(Number);
                        if (!isNaN(x)) xInputC.value = x;
                        if (!isNaN(y)) yInputC.value = y;
                    });
                });
            } else {
                console.error('Ошибка загрузки данных:', error)
                modal.close();
            }
        })
        .catch(error => {
            console.error('Ошибка загрузки данных:', error)
            modal.close();
        });
}

document.getElementById('edit-modal-button').addEventListener('click', () => {
    if (editPersonId == null) {
        return
    }
    // Получаем элементы формы
    const nameInput = document.getElementById('edit_name_input');
    const birthdayInput = document.getElementById('edit_birthday_input');
    const nationalitySelect = document.getElementById('edit_nationality_input');
    const heightInput = document.getElementById('edit_height_input');
    const hairColorSelect = document.getElementById('edit_hair_color_input');
    const eyeColorSelect = document.getElementById('edit_eye_color_input');
    const locationX = document.getElementById('edit_location_x_input');
    const locationY = document.getElementById('edit_location_y_input');
    const locationZ = document.getElementById('edit_location_z_input');
    const coordX = document.getElementById('edit_coordinate_x_input');
    const coordY = document.getElementById('edit_coordinate_y_input');
    const creationDate = document.getElementById('edit_creation_date');
    const messageDiv = document.getElementById('edit-message');



    const errors = form_person_validation(
        nameInput,
        birthdayInput,
        nationalitySelect,
        heightInput,
        hairColorSelect,
        eyeColorSelect,
        locationX,
        locationY,
        locationZ,
        coordX,
        coordY,
        messageDiv
    )

    if (errors.length > 0) {
        return;
    }

    // Если ошибок нет — можно отправлять данные
    const formData = {
        id: editPersonId,
        name: nameInput.value.trim(),
        birthday: birthdayInput.value,
        nationality: nationalitySelect.value,
        height: parseInt(heightInput.value),
        hairColor: hairColorSelect.value,
        eyeColor: eyeColorSelect.value,
        location: {
            x: parseFloat(locationX.value),
            y: parseFloat(locationY.value),
            z: parseFloat(locationZ.value)
        },
        coordinates: {
            x: parseInt(coordX.value),
            y: parseInt(coordY.value)
        },
        creationDate: creationDate.value
    };

    console.log(formData);

    const url = `/update_person?id=${editPersonId}`;
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (response.ok) {
                updatePersonsTable();
                modal.close();
            } else {
                console.error('Ошибка загрузки данных:', error)
                modal.close();
            }
        })
        .catch(error => {
            console.error('Ошибка загрузки данных:', error)
            modal.close();
        });
});