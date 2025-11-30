document.getElementById('upload-button').addEventListener('click', () => {
    const uploadMessage = document.getElementById('upload-message');

    // Очистка сообщений
    uploadMessage.style.display = "none";

    // Создаем input элемент для выбора файла
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = '.txt,.json';

    // Обработчик выбора файла
    fileInput.addEventListener('change', (event) => {
        const file = event.target.files[0];
        if (file) {
            processFile(file);
        }
    });

    // Запускаем диалог выбора файла
    fileInput.click();
});

function processFile(file) {
    const uploadMessage = document.getElementById('upload-message');

    // Проверяем расширение файла
    if (!file.name.toLowerCase().endsWith('.txt') && !file.name.toLowerCase().endsWith('.json')) {
        uploadMessage.textContent = "Выберите файл с расширением .txt или .json";
        uploadMessage.className = "upload-message error";
        uploadMessage.style.display = "block";
        return;
    }

    const reader = new FileReader();

    reader.onload = function(e) {
        try {
            const fileContent = e.target.result;

            // Пытаемся распарсить JSON
            const personsData = JSON.parse(fileContent);

            // Проверяем, что это массив
            if (!Array.isArray(personsData)) {
                throw new Error("Файл должен содержать массив объектов");
            }

            uploadPersons(personsData);
        } catch (error) {
            uploadMessage.textContent = "Файл не удалось распарсить";
            uploadMessage.className = "upload-message error";
            uploadMessage.style.display = "block";
            console.error("Ошибка парсинга файла:", error);
        }
    };

    reader.onerror = function() {
        uploadMessage.textContent = "Ошибка чтения файла";
        uploadMessage.className = "upload-message error";
        uploadMessage.style.display = "block";
    };

    reader.readAsText(file);
}

function uploadPersons(personsData) {
    const uploadMessage = document.getElementById('upload-message');

    fetch('/upload_from_file', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(personsData)
    })
    .then(response => {

        if (response.ok) {
            uploadMessage.textContent = "Данные успешно загружены";
            uploadMessage.className = "upload-message success";
            uploadMessage.style.display = "block";

            setTimeout(() => {
                uploadMessage.style.display = "none";
            }, 3000);

            updateUploadTable();
        } else {
            uploadMessage.textContent = "Ошибка при загрузке данных на сервер";
            uploadMessage.className = "upload-message error";
            uploadMessage.style.display = "block";

            setTimeout(() => {
                uploadMessage.style.display = "none";
            }, 5000);
updateUploadTable();
        }
    });
}

document.addEventListener("DOMContentLoaded", function () {
    // Найти вкладку "Другое"
    const otherTab = document.querySelector('[data-tab="upload"]');

    // Добавить обработчик клика
    otherTab.addEventListener("click", function () {
        // Проверить, активна ли вкладка (если нужно)
        if (!otherTab.classList.contains("active")) return;

        updateUploadTable();
    });
});

function updateUploadTable() {
    fetch('/upload_history')
        .then(response => response.json())
        .then(data => {
            const tableBody = document.getElementById('upload-table-body');
            const uploadTable = document.getElementById('upload-table');

            console.log(!data);
            console.log(data.length === 0);

            // Проверяем, есть ли данные
            if (!data || data.length === 0) {
                // Скрываем таблицу, показываем сообщение
                if (uploadTable) uploadTable.style.display = 'none';

                tableBody.innerHTML = '';
                return;
            }

            // Показываем таблицу, скрываем сообщение
            if (uploadTable) uploadTable.style.display = 'table';


            // Заполняем таблицу
            tableBody.innerHTML = '';
            data.forEach(upload => {
                const row = document.createElement('tr');
                const statusText = upload.status ? 'Успешно' : 'Неуспешно';
                row.innerHTML = `
                    <td>${upload.id}</td>
                    <td>${statusText}</td>
                    <td>${upload.author}</td>
                    <td>${upload.countItems}</td>
                `;
                tableBody.appendChild(row);
            });
        })
        .catch(error => {
            console.error("Ошибка при получении истории загрузок:", error);
            const uploadTable = document.getElementById('upload-table');
            if (uploadTable) uploadTable.style.display = 'none';
        });
}