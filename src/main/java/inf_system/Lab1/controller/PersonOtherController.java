package inf_system.Lab1.controller;

import inf_system.Lab1.controller.dto.PersonDTO;
import inf_system.Lab1.db.entities.*;
import inf_system.Lab1.db.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@RestController
public class PersonOtherController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/average-height")
    public ResponseEntity<?> getAverageHeight() {
        try {
            List<Person> persons = personRepository.findAll();
            if (persons.isEmpty()) {
                return new ResponseEntity<>("объектов нет", HttpStatus.NOT_FOUND);
            } else {
                double result = persons.stream()
                        .mapToDouble(Person::getHeight)
                        .average()
                        .orElse(0.0);
                BigDecimal rounded = new BigDecimal(result)
                        .setScale(3, RoundingMode.HALF_UP);
                return new ResponseEntity<>(rounded, HttpStatus.OK);
            }
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/max-birthday")
    public ResponseEntity<?> getPersonWithMaxBirthday() {
        try {
            List<Person> persons = personRepository.findAll();
            if (persons.isEmpty()) {
                return new ResponseEntity<>("Объектов нет", HttpStatus.NOT_FOUND);
            } else {
                PersonDTO result = PersonDTO.map(persons.stream()
                        .max(Comparator.comparing(Person::getBirthday))
                        .orElse(null));
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tall-people")
    public ResponseEntity<?> getTallPeople(@RequestParam int minHeight) {
        try {
            List<PersonDTO> result = personRepository.findAll()
                    .stream()
                    .filter(p -> p.getHeight() > minHeight)
                    .map(PersonDTO::map)
                    .toList();
            if (result.isEmpty()) {
                return new ResponseEntity<>("Объектов нет", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count-by-hair-color")
    public ResponseEntity<?> countByHairColor(@RequestParam String hairColor) {
        try {
            List<Person> persons = personRepository.findAll();
            if (persons.isEmpty()) {
                return new ResponseEntity<>("объектов нет", HttpStatus.NOT_FOUND);
            } else {
                long result = persons.stream()
                        .filter(p -> p.getHairColor().equals(Color.valueOf(hairColor)))
                        .count();
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
            @RequestParam double zMax) {
        try {
            List<Person> persons = personRepository.findAll();
            if (persons.isEmpty()) {
                return new ResponseEntity<>("объектов нет", HttpStatus.NOT_FOUND);
            } else {
                long result = persons.stream()
                        .filter(p -> p.getHairColor().equals(Color.valueOf(hairColor)) &&
                                p.getLocation() != null &&
                                p.getLocation().getX() >= xMin &&
                                p.getLocation().getX() <= xMax &&
                                p.getLocation().getY() >= yMin &&
                                p.getLocation().getY() <= yMax &&
                                p.getLocation().getZ() >= zMin &&
                                p.getLocation().getZ() <= zMax)
                        .count();
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}