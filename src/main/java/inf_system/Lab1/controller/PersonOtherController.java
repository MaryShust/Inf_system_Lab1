package inf_system.Lab1.controller;

import inf_system.Lab1.controller.dto.PersonDTO;
import inf_system.Lab1.controller.exception.NotFoundException;
import inf_system.Lab1.services.PersonStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class PersonOtherController {

    @Autowired
    private PersonStatisticsService personStatisticsService;

    @GetMapping("/average-height")
    public ResponseEntity<?> getAverageHeight() {
        try {
            BigDecimal averageHeight = personStatisticsService.getAverageHeight();
            return ResponseEntity.ok(averageHeight);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/max-birthday")
    public ResponseEntity<?> getPersonWithMaxBirthday() {
        try {
            PersonDTO result = personStatisticsService.getPersonWithMaxBirthday();
            return ResponseEntity.ok(result);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/tall-people")
    public ResponseEntity<?> getTallPeople(@RequestParam int minHeight) {
        try {
            List<PersonDTO> result = personStatisticsService.getTallPeople(minHeight);
            return ResponseEntity.ok(result);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/count-by-hair-color")
    public ResponseEntity<?> countByHairColor(@RequestParam String hairColor) {
        try {
            long result = personStatisticsService.countByHairColor(hairColor);
            return ResponseEntity.ok(result);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/count-by-hair-color-in-location")
    public ResponseEntity<?> countByHairColorInLocation(
            @RequestParam String hairColor,
            @RequestParam double xMin,
            @RequestParam double xMax,
            @RequestParam float yMin,
            @RequestParam float yMax,
            @RequestParam double zMin,
            @RequestParam double zMax
    ) {
        try {
            long result = personStatisticsService.countByHairColorInLocation(
                    hairColor, xMin, xMax, yMin, yMax, zMin, zMax);
            return ResponseEntity.ok(result);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}