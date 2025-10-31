document.getElementById('upload-button').addEventListener('click', () => {
    const uploadMessage = document.getElementById('upload-message');

    uploadMessage.style.display = "none";

    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = '.txt'; // Только txt файлы

    fileInput.addEventListener('change', (event) => {
        const file = event.target.files[0];
        if (file) {
            processFile(file);
        }
    });

    fileInput.click();
});

function processFile(file) {
    const uploadMessage = document.getElementById('upload-message');

    if (!file.name.toLowerCase().endsWith('.txt')) {
        uploadMessage.textContent = "Выберите файл с расширением .txt";
        uploadMessage.className = "upload-message error";
        uploadMessage.style.display = "block";
        return;
    }

    const reader = new FileReader();

    reader.onload = function(e) {
        try {
            const fileContent = e.target.result;

            const personsData = JSON.parse(fileContent);

            if (!Array.isArray(personsData)) {
                throw new Error("Файл должен содержать массив объектов");
            }

            // Обрабатываем данные - заменяем цвета используя colorMapping
            const processedData = personsData.map(person => {
                const updatedPerson = { ...person };

                if (updatedPerson.eyeColor && сolorMapping[updatedPerson.eyeColor]) {
                    updatedPerson.eyeColor = сolorMapping[updatedPerson.eyeColor];
                }

                if (updatedPerson.hairColor && сolorMapping[updatedPerson.hairColor]) {
                    updatedPerson.hairColor = сolorMapping[updatedPerson.hairColor];
                }

                if (updatedPerson.nationality && countryMapping[updatedPerson.nationality]) {
                    updatedPerson.nationality = countryMapping[updatedPerson.nationality];
                }

                return updatedPerson;
            });

            uploadPersons(processedData);

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

    otherTab.addEventListener("click", function () {
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

            if (!data || data.length === 0) {
                // Скрываем таблицу, показываем сообщение
                if (uploadTable) uploadTable.style.display = 'none';

                tableBody.innerHTML = '';
                return;
            }

            // Показываем таблицу, скрываем сообщение
            if (uploadTable) uploadTable.style.display = 'table';


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
