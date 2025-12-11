package infs.lab.controller;

import infs.lab.controller.dto.PersonDTO;
import infs.lab.services.PersonStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.List;

@RestController
@Tag(name = "Person Statistics Controller", description = "API для получения статистики по людям")
public class PersonOtherController {

    @Autowired
    private PersonStatisticsService personStatisticsService;

    @GetMapping("/average-height")
    @Operation(summary = "Получить средний рост",
            description = "Возвращает средний рост всех людей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение среднего роста"),
            @ApiResponse(responseCode = "404", description = "Люди не найдены")
    })
    public ResponseEntity<?> getAverageHeight() {
        BigDecimal averageHeight = personStatisticsService.getAverageHeight();
        return ResponseEntity.ok(averageHeight);
    }

    @GetMapping("/max-birthday")
    @Operation(summary = "Получить человека с максимальной датой рождения",
            description = "Возвращает человека с самой поздней датой рождения")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение данных о человеке"),
            @ApiResponse(responseCode = "404", description = "Люди не найдены")
    })
    public ResponseEntity<?> getPersonWithMaxBirthday() {
        PersonDTO result = personStatisticsService.getPersonWithMaxBirthday();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/tall-people")
    @Operation(summary = "Получить список высоких людей",
            description = "Возвращает список людей, рост которых превышает указанный минимум")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "404", description = "Люди не найдены")
    })
    public ResponseEntity<?> getTallPeople(
            @Parameter(description = "Минимальный рост", example = "170", required = true)
            @RequestParam int minHeight) {
        List<PersonDTO> result = personStatisticsService.getTallPeople(minHeight);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/count-by-hair-color")
    @Operation(summary = "Посчитать количество людей по цвету волос",
            description = "Возвращает количество людей с указанным цветом волос")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный подсчет"),
            @ApiResponse(responseCode = "404", description = "Люди не найдены")
    })
    public ResponseEntity<?> countByHairColor(
            @Parameter(description = "Цвет волос", required = true,
                    example = "BLACK",
                    schema = @Schema(type = "string", allowableValues = {
                            "BLACK", "BLUE", "YELLOW", "ORANGE", "WHITE"
                    }))
            @RequestParam String hairColor) {
        long result = personStatisticsService.countByHairColor(hairColor);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/count-by-hair-color-in-location")
    @Operation(summary = "Посчитать количество людей по цвету волос в определенной локации",
            description = "Возвращает количество людей с указанным цветом волос в заданной области локации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный подсчет"),
            @ApiResponse(responseCode = "404", description = "Люди не найдены")
    })
    public ResponseEntity<?> countByHairColorInLocation(
            @Parameter(description = "Цвет волос", required = true,
                    example = "BLACK",
                    schema = @Schema(type = "string", allowableValues = {
                            "BLACK", "BLUE", "YELLOW", "ORANGE", "WHITE"
                    }))
            @RequestParam String hairColor,

            @Parameter(description = "Минимальная координата X локации", example = "0.0")
            @RequestParam double xMin,

            @Parameter(description = "Максимальная координата X локации", example = "100.0")
            @RequestParam double xMax,

            @Parameter(description = "Минимальная координата Y локации", example = "0.0")
            @RequestParam float yMin,

            @Parameter(description = "Максимальная координата Y локации", example = "100.0")
            @RequestParam float yMax,

            @Parameter(description = "Минимальная координата Z локации", example = "0.0")
            @RequestParam double zMin,

            @Parameter(description = "Максимальная координата Z локации", example = "100.0")
            @RequestParam double zMax
    ) {
        long result = personStatisticsService.countByHairColorInLocation(hairColor, xMin, xMax, yMin, yMax, zMin, zMax);
        return ResponseEntity.ok(result);
    }
}