package infs.lab.controller;

import infs.lab.db.entities.Coordinates;
import infs.lab.services.CoordinatesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@Tag(name = "Coordinates Controller", description = "API для получения данных сохранненных координат")
public class CoordinatesController {

    @Autowired
    private CoordinatesService coordinatesService;

    @Operation(summary = "Получить сохраненные координаты",
            description = "Возвращает сохраненные координаты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение сохраненных координат"),
    })
    @GetMapping("/coordinates")
    public ResponseEntity<List<Coordinates>> getCoordinates() {
        List<Coordinates> coordinates = coordinatesService.getAllCoordinates();
        return ResponseEntity.ok(coordinates);
    }
}