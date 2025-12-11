package infs.lab.controller;

import infs.lab.db.entities.History;
import infs.lab.services.AuthService;
import infs.lab.services.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@Tag(name = "History Controller", description = "API для получения истории импортов из файла")
public class HistoryController {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private AuthService authService;

    @Operation(summary = "Получить историю импортов",
            description = "Возвращает историю импортов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение истории импортов"),
    })
    @GetMapping("/upload_history")
    public ResponseEntity<List<History>> getHistory(HttpServletRequest request) {
        String userName = authService.getUserName(request);
        List<History> history = historyService.getAllHistory(userName);
        return ResponseEntity.ok(history);
    }
}