package infs.lab.controller;

import infs.lab.db.entities.Location;
import infs.lab.services.LocationService;
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
@Tag(name = "Location Controller", description = "API для получения данных сохранненных локаций")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Operation(summary = "Получить сохраненные локации",
            description = "Возвращает сохраненные локации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение сохраненных локаций"),
    })
    @GetMapping("/locations")
    public ResponseEntity<List<Location>> getLocations() {
        List<Location> coordinates = locationService.getAllLocations();
        return ResponseEntity.ok(coordinates);
    }
}