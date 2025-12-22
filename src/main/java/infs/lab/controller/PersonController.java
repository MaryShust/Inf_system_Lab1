package infs.lab.controller;

import infs.lab.controller.dto.PersonDTO;
import infs.lab.services.AuthService;
import infs.lab.services.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Tag(name = "Person Controller", description = "API для управления данными людей")
public class PersonController {

    @Autowired
    private PersonService personService;
    @Autowired
    private AuthService authService;

    @Operation(
            summary = "Создать нового человека",
            description = "Создает нового человека с валидацией уникальности и корректности данных"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Персона успешно создана",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации или нарушение уникальности",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
    })
    @PostMapping("/create_person")
    public ResponseEntity<?> createPerson(@RequestBody PersonDTO personDTO) {
        personService.createPerson(personDTO);
        return ResponseEntity.ok("Персона успешно создана");
    }

    @Operation(
            summary = "Обновить данные человека",
            description = "Обновляет данные существующего человека по ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные человека успешно обновлены",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации или нарушение уникальности",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
            @ApiResponse(responseCode = "404", description = "Человек не найден",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
    })
    @PutMapping("/update_person")
    public ResponseEntity<?> updatePerson(@RequestBody PersonDTO personDTO) {
        personService.updatePerson(personDTO);
        return ResponseEntity.ok("Персона успешно обновлена");
    }

    @Operation(
            summary = "Найти человека по ID",
            description = "Возвращает данные человека по указанному идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Человек найден",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PersonDTO.class))),
            @ApiResponse(responseCode = "404", description = "Человек не найдена",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
    })
    @GetMapping("/find_person")
    public ResponseEntity<?> findPerson(
            @Parameter(description = "ID человека", required = true, example = "1")
            @RequestParam Long id) {
        PersonDTO personDTO = personService.findPerson(id);
        return ResponseEntity.ok(personDTO);
    }

    @Operation(
            summary = "Удалить данные человека",
            description = "Удаляет данные человека по ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные человека успешно удалены",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
            @ApiResponse(responseCode = "404", description = "Человек не найдена",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
    })
    @DeleteMapping("/delete_person")
    public ResponseEntity<String> deletePerson(
            @Parameter(description = "ID человека для удаления", required = true, example = "1")
            @RequestParam Long id) {
        personService.deletePerson(id);
        return ResponseEntity.ok("Person deleted successfully");
    }

    @Operation(
            summary = "Получить список людей",
            description = "Возвращает список людей с пагинацией, сортировкой и поиском"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список людей получен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping("/persons")
    public ResponseEntity<List<PersonDTO>> getAllPeople(
            @Parameter(description = "Номер страницы", example = "1")
            @RequestParam(defaultValue = "1") int page,

            @Parameter(description = "Поле для сортировки",
                    schema = @Schema(allowableValues = {"id", "name", "height", "creationDate"}),
                    example = "id")
            @RequestParam(defaultValue = "id") String sortField,

            @Parameter(description = "Порядок сортировки",
                    schema = @Schema(allowableValues = {"asc", "desc"}),
                    example = "asc")
            @RequestParam(defaultValue = "asc") String sortOrder,

            @Parameter(description = "Поисковый запрос", example = "Иван")
            @RequestParam(required = false) String search) {
        List<PersonDTO> result = personService.getPeople(page, sortField, sortOrder, search);
        return ResponseEntity.ok(result);
    }
    @Operation(
            summary = "Получить количество страниц",
            description = "Возвращает общее количество страниц с учетом поиска"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Количество страниц получено",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping("/all_pages")
    public ResponseEntity<Integer> getCountPeople(
            @Parameter(description = "Поисковый запрос", example = "Иван")
            @RequestParam(required = false) String search) {
        int totalPages = personService.getTotalPages(search);
        return ResponseEntity.ok(totalPages);
    }
}