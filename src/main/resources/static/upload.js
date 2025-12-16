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

    uploadPersons(file);
}

function uploadPersons(file) {
    const uploadMessage = document.getElementById('upload-message');

    const formData = new FormData();
    formData.append('file', file);

    fetch('/upload_from_file', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok) {
            response.text().then(successMessage => {
                uploadMessage.textContent = successMessage;
                uploadMessage.className = "upload-message success";
                uploadMessage.style.display = "block";

                setTimeout(() => {
                    uploadMessage.style.display = "none";
                }, 3000);

                updateUploadTable();
            });
        } else {
            response.text().then(errorMessage => {
                uploadMessage.textContent = errorMessage;
                uploadMessage.className = "upload-message error";
                uploadMessage.style.display = "block";

                setTimeout(() => {
                    uploadMessage.style.display = "none";
                }, 5000);

                updateUploadTable();
            });
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

            // Проверяем, есть ли данные
            if (!data || data.length === 0) {
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
                const statusClass = upload.status ? 'success' : 'error';

                // Создаем кнопку для скачивания файла, если есть URL
                let downloadButton = '';
                if (upload.downloadUrl && upload.originalFilename) {
                    downloadButton = `
                        <button onclick="downloadFile('${upload.downloadUrl}', '${upload.originalFilename}')"
                                class="download-button"
                                title="Скачать файл: ${upload.originalFilename}">
                            📥 Скачать
                        </button>
                    `;
                }

                row.innerHTML = `
                    <td>${upload.id}</td>
                    <td><span class="status-badge ${statusClass}">${statusText}</span></td>
                    <td>${upload.author}</td>
                    <td>${upload.countItems}</td>
                    <td>${upload.originalFilename || '-'}</td>
                    <td>${upload.formattedFileSize || '-'}</td>
                    <td>${downloadButton}</td>
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

function downloadFile(url, filename) {
    const link = document.createElement('a');
    link.href = url;
    link.download = filename || 'download';
    link.style.display = 'none';

    // Добавляем обработчик события load для удаления ссылки после скачивания
    link.onload = function() {
        // Небольшая задержка перед удалением
        setTimeout(() => {
            if (link.parentNode) {
                link.parentNode.removeChild(link);
            }
        }, 100);
    };

    // Добавляем обработчик ошибок
    link.onerror = function() {
        console.error('Ошибка при скачивании файла');
        alert('Не удалось скачать файл');
        if (link.parentNode) {
            link.parentNode.removeChild(link);
        }
    };

    // Добавляем ссылку в DOM и кликаем по ней
    document.body.appendChild(link);
    link.click();
}