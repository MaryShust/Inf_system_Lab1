document.getElementById('delete-input').addEventListener('keydown', (e) => {
    if (e.key === 'Enter') {
        document.getElementById('delete-button').click();
    }
});

document.getElementById('delete-button').addEventListener('click', () => {
    const deleteInput = document.getElementById('delete-input');
    const deleteMessage = document.getElementById('delete-message');
    const id = deleteInput.value.trim();

    // Очистка сообщений
    deleteMessage.style.display = "none";

    if (!isValidEditText(id, deleteMessage, "delete-message")) {
        return;
    }

    // Вызов функции удаления
    deletePerson(id);
    deleteInput.value = ''; // Очистка поля после успешной отправки
});

function deletePerson(id) {
    const url = `/delete_person?id=${id}`;
    const deleteMessage = document.getElementById('delete-message');

    fetch(url)
        .then(response => {
            if (response.ok) {
                // Успешное удаление
                deleteMessage.textContent = "Объект успешно удален";
                deleteMessage.className = "delete-message success";
                deleteMessage.style.display = "block";

                setTimeout(() => {
                    deleteMessage.style.display = "none";
                }, 3000);

                updatePersonsTable(); // Обновить таблицу
            } else {
                // Обработка ошибки
                return response.text().then(errorMessage => {
                    deleteMessage.textContent = errorMessage;
                    deleteMessage.className = "delete-message error";
                    deleteMessage.style.display = "block";

                    setTimeout(() => {
                        deleteMessage.style.display = "none";
                    }, 5000);
                });
            }
        })
        .catch(error => {
            // Обработка сетевых ошибок
            deleteMessage.textContent = "Произошла сетевая ошибка: " + error.message;
            deleteMessage.className = "delete-message error";
            deleteMessage.style.display = "block";

            setTimeout(() => {
                deleteMessage.style.display = "none";
            }, 5000);
        });
}