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
                `;
                tableBody.appendChild(row);
            });
        });
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