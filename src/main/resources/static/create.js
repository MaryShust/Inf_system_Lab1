//document.addEventListener('DOMContentLoaded', () => {
    const createTab = document.getElementById('create-tab');
    const locationSelect = document.getElementById('create_location_select');
    const coordinateSelect = document.getElementById('create_coordinate_select');
    const xInputL = document.getElementById('create_location_x_input');
    const yInputL = document.getElementById('create_location_y_input');
    const zInputL = document.getElementById('create_location_z_input');
    const xInputC = document.getElementById('create_coordinate_x_input');
    const yInputC = document.getElementById('create_coordinate_y_input');

    // Обработчик выбора локации
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

    // Проверка активации вкладки
    const observer = new MutationObserver(mutations => {
        mutations.forEach(mutation => {
            if (mutation.type === 'attributes' && mutation.attributeName === 'class') {
                const isActive = createTab.classList.contains('active');
                if (isActive) {
                    loadLocations(locationSelect); // Загрузка данных при активации
                    loadCoordinates(coordinateSelect);
                }
            }
        });
    });

    // Наблюдение за изменениями класса вкладки
    observer.observe(createTab, {
        attributes: true
    });

    // Загрузка данных при первом открытии
    if (createTab.classList.contains('active')) {
        loadLocations(locationSelect);
        loadCoordinates(coordinateSelect);
    }
//});

document.getElementById('create-button').addEventListener('click', () => {
    // Получаем элементы формы
    const nameInput = document.getElementById('create_name_input');
    const birthdayInput = document.getElementById('create_birthday_input');
    const nationalitySelect = document.getElementById('create_nationality_input');
    const heightInput = document.getElementById('create_height_input');
    const hairColorSelect = document.getElementById('create_hair_color_input');
    const eyeColorSelect = document.getElementById('create_eye_color_input');
    const locationX = document.getElementById('create_location_x_input');
    const locationY = document.getElementById('create_location_y_input');
    const locationZ = document.getElementById('create_location_z_input');
    const coordX = document.getElementById('create_coordinate_x_input');
    const coordY = document.getElementById('create_coordinate_y_input');
    const messageDiv = document.getElementById('create-message');

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
        }
    };

    // Пример отправки данных
    fetch("/create_person", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (response.ok) {
                messageDiv.textContent = 'Объект успешно создан!';
                messageDiv.className = 'create-message success';
                messageDiv.style.display = 'block';

                setTimeout(() => {
                    messageDiv.style.display = 'none';
                    // Очистка формы
                    nameInput.value = '';
                    birthdayInput.value = '';
                    heightInput.value = '';
                    locationX.value = '';
                    locationY.value = '';
                    locationZ.value = '';
                    coordX.value = '';
                    coordY.value = '';
                    nationalitySelect.selectedIndex = 0;
                    hairColorSelect.selectedIndex = 0;
                    eyeColorSelect.selectedIndex = 0;
                }, 3000);
            } else {
                return response.text().then(errorMessage => {
                    messageDiv.textContent = errorMessage;
                    messageDiv.className = 'create-message error';
                    messageDiv.style.display = 'block';

                    setTimeout(() => {
                        messageDiv.style.display = 'none';
                    }, 5000);
                });
            }
        })
        .catch(error => {
            messageDiv.textContent = 'Произошла сетевая ошибка: ' + error.message;
            messageDiv.className = 'create-message error';
            messageDiv.style.display = 'block';

            setTimeout(() => {
                messageDiv.style.display = 'none';
            }, 5000);
        });
});