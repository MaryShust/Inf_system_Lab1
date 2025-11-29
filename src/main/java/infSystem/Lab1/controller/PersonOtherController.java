package infSystem.Lab1.controller;

import infSystem.Lab1.controller.dto.PersonDTO;
import infSystem.Lab1.services.PersonStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
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
        BigDecimal averageHeight = personStatisticsService.getAverageHeight();
        return ResponseEntity.ok(averageHeight);
    }

    @GetMapping("/max-birthday")
    public ResponseEntity<?> getPersonWithMaxBirthday() {
        PersonDTO result = personStatisticsService.getPersonWithMaxBirthday();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/tall-people")
    public ResponseEntity<?> getTallPeople(@RequestParam int minHeight) {
        List<PersonDTO> result = personStatisticsService.getTallPeople(minHeight);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/count-by-hair-color")
    public ResponseEntity<?> countByHairColor(@RequestParam String hairColor) {
        long result = personStatisticsService.countByHairColor(hairColor);
        return ResponseEntity.ok(result);
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
        long result = personStatisticsService.countByHairColorInLocation(hairColor, xMin, xMax, yMin, yMax, zMin, zMax);
        return ResponseEntity.ok(result);
    }
}