let sortState = {
    key: null,
    direction: 1 // 1 для asc, -1 для desc
};

function getNestedValue(obj, keyPath) {
    return keyPath.split('.').reduce((acc, part) => acc && acc[part], obj);
}

function sortPersons(persons, key, direction) {
    return [...persons].sort((a, b) => {
        let valA = getNestedValue(a, key);
        let valB = getNestedValue(b, key);

        // Обработка разных типов данных
        if (typeof valA === 'string' && typeof valB === 'string') {
            // Для дат
            if (valA.includes('-') && valB.includes('-')) {
                return (new Date(valA) - new Date(valB)) * direction;
            }
            // Для обычных строк
            return valA.localeCompare(valB) * direction;
        }
        // Для чисел
        if (typeof valA === 'number' && typeof valB === 'number') {
            return (valA - valB) * direction;
        }
        return 0;
    });
}

function updateTableHeaders(key, direction) {
    document.querySelectorAll('th').forEach(th => {
        th.classList.remove('asc', 'desc');
    });

    if (key) {
        const header = document.querySelector(`th[data-key="${key}"]`);
        if (header) {
            header.classList.add(direction === 1 ? 'asc' : 'desc');
        }
    }
}

let currentSearchQuery = "";
let currentPage = 1;
let allPages = 1;

function updatePageNavigator() {
    const navContainer = document.getElementById('navigation-container')
    const page = document.getElementById('page')
    let url = `/all_pages`;
    if (currentSearchQuery != "") {
        url += `?search=${encodeURIComponent(currentSearchQuery)}`;
    }
    fetch(url)
        .then(response => response.json())
        .then(pages => {
            allPages = pages;
            if (pages > 1) {
                navContainer.style.display = "flex";
                page.textContent = `${currentPage} из ${pages}`;
            } else {
                navContainer.style.display = "none";
            }
        });
}

function updatePersonsTable() {
    updatePageNavigator();
    let url = `/persons?page=${currentPage}`;
    if (currentSearchQuery != "") {
        url += `&search=${encodeURIComponent(currentSearchQuery)}`;
    }
    if (sortState.key) {
        url += `&sortField=${sortState.key}`;
        if (sortState.direction == 1) {
            url += `&sortOrder=asc`;
        } else {
            url += `&sortOrder=desc`;
        }
    }

    fetch(url)
        .then(response => response.json())
        .then(persons => {
            const tableBody = document.getElementById('persons-table-body');
            tableBody.innerHTML = '';

            persons.forEach(person => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td data-id="${person.id}">✎</td>
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
                    <td class="photo-cell" data-person-id="${person.id}">
                        <div class="photo-container" id="photo-container-${person.id}">
                            <!-- Будет заполнено динамически -->
                        </div>
                    </td>
                `;
                tableBody.appendChild(row);
                loadPersonPhoto(person.id, person.photoId);
            });

            // Добавляем обработчики событий для загрузки фото
            document.querySelectorAll('.photo-cell').forEach(cell => {
                const personId = cell.getAttribute('data-person-id');
                const container = document.getElementById(`photo-container-${personId}`);

                // Показываем иконку загрузки если фото еще не загружено
                if (!container.querySelector('.photo-loaded')) {
                    container.innerHTML = `
                        <div class="upload-icon" title="Загрузить фото" onclick="openPhotoUploadModal(${personId})">
                            📤
                        </div>
                        <div class="photo-preview" id="photo-preview-${personId}"></div>
                    `;
                }
            });
        });
}

function loadPersonPhoto(personId, photoId) {
    const container = document.getElementById(`photo-container-${personId}`);
    const preview = document.getElementById(`photo-preview-${personId}`);

    if (!container) return;

    if (!photoId) {
        // Если photoId не пришел - показываем иконку загрузки
        container.innerHTML = `
            <div class="upload-icon" title="Загрузить фото" onclick="openPhotoUploadModal(${personId})">
                📤
            </div>
            <div class="photo-preview" id="photo-preview-${personId}"></div>
        `;
        return;
    }

    // Если photoId есть - загружаем фото
    fetch(`/persons/photo/${photoId}`)
        .then(response => response.json())
        .then(photoData => {
            if (photoData && photoData.photoUrl) {
                // Помечаем контейнер как загруженный
                container.classList.add('photo-loaded');

                container.innerHTML = `
                    <div class="photo-loaded-content">
                        <img src="${photoData.photoUrl}"
                             alt="Фото ${personId}"
                             class="person-photo"
                             onclick="openPhotoViewer('${photoData.photoUrl}')">
                        <div class="photo-actions">
                            <button onclick="deletePhoto(${personId})"
                                    class="delete-photo-btn"
                                    title="Удалить фото">🗑️</button>
                        </div>
                    </div>
                `;
            } else {
                // Если фото не найдено - показываем иконку загрузки
                container.innerHTML = `
                    <div class="upload-icon" title="Загрузить фото" onclick="openPhotoUploadModal(${personId})">
                        📤
                    </div>
                    <div class="photo-preview" id="photo-preview-${personId}"></div>
                `;
            }
        })
        .catch(error => {
            console.error('Error loading photo:', error);
            // При ошибке показываем иконку загрузки
            container.innerHTML = `
                <div class="upload-icon" title="Загрузить фото" onclick="openPhotoUploadModal(${personId})">
                    📤
                </div>
                <div class="photo-preview" id="photo-preview-${personId}"></div>
            `;
        });
}

function openPhotoUploadModal(personId) {
    const modal = document.createElement('div');
    modal.className = 'modal';
    modal.id = 'photo-upload-modal';
    modal.innerHTML = `
        <div class="modal-content">
            <h3>Загрузить фото для человека ID: ${personId}</h3>
            <span class="close-modal">&times;</span>
            <div class="upload-area" id="drop-area-${personId}">
                <p>Перетащите фото сюда или</p>
                <input type="file" id="file-input-${personId}" accept="image/*" style="display: none;">
                <label for="file-input-${personId}" class="browse-btn">Выберите файл</label>
                <p class="file-info" id="file-info-${personId}"></p>
                <div class="preview-container" id="preview-container-${personId}"></div>
            </div>
            <div class="modal-actions">
                <button id="upload-photo-btn-${personId}" class="upload-btn" disabled>Загрузить</button>
                <button id="cancel-btn-${personId}" class="cancel-btn">Отмена</button>
            </div>
            <div id="upload-status-${personId}" class="upload-status"></div>
        </div>
    `;

    document.body.appendChild(modal);

    const dropArea = document.getElementById(`drop-area-${personId}`);
    const fileInput = document.getElementById(`file-input-${personId}`);
    const uploadBtn = document.getElementById(`upload-photo-btn-${personId}`);
    const cancelBtn = document.getElementById(`cancel-btn-${personId}`);
    const closeBtn = modal.querySelector('.close-modal');
    let selectedFile = null;

    // Функции для drag & drop
    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
        dropArea.addEventListener(eventName, preventDefaults, false);
    });

    function preventDefaults(e) {
        e.preventDefault();
        e.stopPropagation();
    }

    ['dragenter', 'dragover'].forEach(eventName => {
        dropArea.addEventListener(eventName, highlight, false);
    });

    ['dragleave', 'drop'].forEach(eventName => {
        dropArea.addEventListener(eventName, unhighlight, false);
    });

    function highlight() {
        dropArea.classList.add('highlight');
    }

    function unhighlight() {
        dropArea.classList.remove('highlight');
    }

    // Обработка сброса файла
    dropArea.addEventListener('drop', handleDrop, false);

    function handleDrop(e) {
        const dt = e.dataTransfer;
        const file = dt.files[0];
        handleFile(file);
    }

    fileInput.addEventListener('change', function(e) {
        // Останавливаем всплытие события
        e.stopPropagation();
        if (this.files[0]) {
            handleFile(this.files[0]);
        }
    });

    // Обработчик для кнопки "Выберите файл"
    const browseBtn = dropArea.querySelector('.browse-btn');
    browseBtn.addEventListener('click', (e) => {
        e.stopPropagation(); // Останавливаем всплытие
        e.preventDefault(); // Предотвращаем поведение по умолчанию
        fileInput.click();
    });

    // Добавляем обработчик непосредственно к input для предотвращения закрытия
    fileInput.addEventListener('click', (e) => {
        e.stopPropagation();
    });

    function handleFile(file) {
        if (file && file.type.startsWith('image/')) {
            selectedFile = file;

            // Показываем информацию о файле
            document.getElementById(`file-info-${personId}`).textContent =
                `Файл: ${file.name} (${(file.size / 1024).toFixed(2)} KB)`;

            // Показываем предпросмотр
            const previewContainer = document.getElementById(`preview-container-${personId}`);
            previewContainer.innerHTML = '';

            const reader = new FileReader();
            reader.onload = function(e) {
                const img = document.createElement('img');
                img.src = e.target.result;
                img.style.maxWidth = '200px';
                img.style.maxHeight = '200px';
                previewContainer.appendChild(img);
            };
            reader.readAsDataURL(file);

            // Активируем кнопку загрузки
            uploadBtn.disabled = false;
        } else {
            alert('Пожалуйста, выберите файл изображения (JPG, PNG, GIF)');
        }
    }

    uploadBtn.addEventListener('click', async (e) => {
        e.stopPropagation();
        if (!selectedFile) return;

        uploadBtn.disabled = true;
        uploadBtn.textContent = 'Загружается...';

        const formData = new FormData();
        formData.append('file', selectedFile);

        try {
            const response = await fetch(`/persons/${personId}/photo`, {
                method: 'POST',
                body: formData
            });

            const result = await response.json();

            if (response.ok) {
                document.getElementById(`upload-status-${personId}`).innerHTML =
                    '<div class="success">Фото успешно загружено!</div>';

                // Обновляем фото в таблице с новым photoId
                setTimeout(() => {
                    // Загружаем фото по новому photoId
                    loadPersonPhoto(personId, result.photoId);
                    closeModal();
                }, 1500);
            } else {
                throw new Error(result.message || 'Ошибка загрузки');
            }
        } catch (error) {
            document.getElementById(`upload-status-${personId}`).innerHTML =
                `<div class="error">Ошибка: ${error.message}</div>`;
            uploadBtn.disabled = false;
            uploadBtn.textContent = 'Загрузить';
        }
    });

    function closeModal() {
        modal.remove();
    }

    cancelBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        closeModal();
    });

    closeBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        closeModal();
    });

    // Закрытие по клику вне модального окна
    modal.addEventListener('click', (e) => {
        if (e.target === modal) {
            closeModal();
        }
    });

    // Предотвращаем закрытие при клике внутри модального окна
    modal.querySelector('.modal-content').addEventListener('click', (e) => {
        e.stopPropagation();
    });
}

async function deletePhoto(personId) {
    try {
        const response = await fetch(`/persons/${personId}/photo`, {
            method: 'DELETE'
        });

        if (response.ok) {
            // После удаления показываем иконку загрузки
            const container = document.getElementById(`photo-container-${personId}`);
            if (container) {
                container.innerHTML = `
                    <div class="upload-icon" title="Загрузить фото" onclick="openPhotoUploadModal(${personId})">
                        📤
                    </div>
                    <div class="photo-preview" id="photo-preview-${personId}"></div>
                `;
            }
        } else {
            throw new Error('Ошибка удаления');
        }
    } catch (error) {
        alert(`Ошибка: ${error.message}`);
    }
}

// Добавляем обработчики событий для заголовков таблицы
document.querySelectorAll('th[data-key]').forEach(header => {
    header.addEventListener('click', () => {
        const key = header.getAttribute('data-key');

        // Если клик по той же колонке - меняем направление сортировки
        if (sortState.key === key) {
            sortState.direction *= -1;
        } else {
            // Иначе устанавливаем новую колонку сортировки
            sortState.key = key;
            sortState.direction = 1;
        }

        updateTableHeaders(key, sortState.direction);
        currentPage = 1;
        updatePersonsTable();
    });
});

document.getElementById('prev-button').addEventListener('click', () => {
    if (currentPage > 1) {
        currentPage = currentPage - 1;
        updatePersonsTable();
    }
});

document.getElementById('next-button').addEventListener('click', () => {
    if (currentPage < allPages) {
        currentPage = currentPage + 1;
        updatePersonsTable();
    }
});

document.getElementById('search-input').addEventListener('keydown', (e) => {
    if (e.key === 'Enter') {
        document.getElementById('search-button').click();
    }
});

document.getElementById('search-button').addEventListener('click', () => {
    const searchInput = document.getElementById('search-input');
    const searchQuery = searchInput.value.trim();
    if (searchQuery != "") {
        currentSearchQuery = searchQuery
        currentPage = 1;
        updatePersonsTable();
    }
});

document.getElementById('clear-button').addEventListener('click', () => {
    currentSearchQuery = "";
    currentPage = 1;
    document.getElementById('search-input').value = '';
    updatePersonsTable();
});

document.querySelectorAll('.tab').forEach(tab => {
    tab.addEventListener('click', () => {
        // Сброс активного состояния у всех табов
        document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
        tab.classList.add('active');

        // Скрытие всех вкладок
        document.querySelectorAll('.tab-pane').forEach(panel => panel.classList.remove('active'));

        // Отображение нужной вкладки
        const tabId = tab.dataset.tab;
        const activePanel = document.getElementById(`${tabId}-tab`);
        if (activePanel) {
            activePanel.classList.add('active');
        }
    });
});

// Инициализация
updatePersonsTable();
setInterval(updatePersonsTable, 10000); // обновлять каждые 10 секунд

document.querySelector('[data-tab="main"]').addEventListener("click", function () {
    updatePersonsTable();
});