package infs.lab.controller;

import infs.lab.controller.dto.HistoryDTO;
import infs.lab.controller.exception.ParsingException;
import infs.lab.controller.exception.UniqueViolationException;
import infs.lab.controller.exception.ValidationException;
import infs.lab.services.AuthService;
import infs.lab.services.HistoryService;
import infs.lab.services.MinioService;
import infs.lab.services.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@Tag(name = "History Controller", description = "API для получения истории импортов из файла")
public class HistoryController {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private PersonService personService;
    @Autowired
    private AuthService authService;
    @Autowired
    private MinioService minioService;

    @Operation(summary = "Получить историю импортов",
            description = "Возвращает историю импортов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение истории импортов"),
    })
    @GetMapping("/upload_history")
    public ResponseEntity<List<HistoryDTO>> getHistory(HttpServletRequest request) {
        String userName = authService.getUserName(request);
        List<HistoryDTO> history = historyService.getAllHistory(userName);
        return ResponseEntity.ok(history);
    }

    @Operation(
            summary = "Пакетная загрузка данных людей",
            description = "Загружает список людей из файла"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные людей успешно загружены",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации или нарушение уникальности",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
    })
    @PostMapping("/upload_from_file")
    public ResponseEntity<?> uploadPersonsV2(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ) {
        String userName = authService.getUserName(request);
        String fileObjectName = null;
        String originalFileName = null;
        long fileSize = 0L;
        int countPeople = 0;

        boolean failUploadFile = false;
        boolean failUpdateDB = false;
        try {
            fileObjectName = minioService.uploadFile(file);
            originalFileName = file.getOriginalFilename();
            fileSize = file.getSize();
        } catch (Exception e) {
            failUploadFile = true;
        }
        try {
            countPeople = personService.uploadPeople(file);
        } catch (ValidationException | UniqueViolationException | ParsingException e) {
            failUpdateDB = true;
        }
        if (!failUploadFile && !failUpdateDB) {
            historyService.updateHistory(
                    userName,
                    countPeople,
                    originalFileName,
                    fileObjectName,
                    fileSize
            );
            return ResponseEntity.ok("Успешно загружено: " + countPeople + " объектов и файл загружен");
        } else if (!failUploadFile) {
            historyService.updateHistory(
                    userName,
                    0,
                    originalFileName,
                    fileObjectName,
                    fileSize
            );
            return ResponseEntity.ok("Файл загружен, однако данные не загружены");
        } else if (!failUpdateDB) {
            historyService.updateHistory(
                    userName,
                    countPeople,
                    null,
                    null,
                    null
            );
            return ResponseEntity.ok("Данные загружены, однако файл не загружен");
        } else {
            historyService.updateHistory(
                    userName,
                    0,
                    null,
                    null,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ничего не загружено");
        }
    }
}