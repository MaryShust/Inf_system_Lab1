package inf_system.Lab1.controller;

import inf_system.Lab1.controller.dto.PersonDTO;
import inf_system.Lab1.controller.exception.NotFoundException;
import inf_system.Lab1.controller.exception.ValidationException;
import inf_system.Lab1.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping("/create_person")
    public ResponseEntity<?> createPerson(@RequestBody PersonDTO personDTO) {
        try {
            personService.createPerson(personDTO);
            return ResponseEntity.ok("Персона успешно создана");
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Неверное значение enum: " + e.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/update_person")
    public ResponseEntity<?> updatePerson(@RequestBody PersonDTO personDTO) {
        try {
            personService.updatePerson(personDTO);
            return ResponseEntity.ok("Персона успешно обновлена");
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Неверное значение enum: " + e.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/find_person")
    public ResponseEntity<?> findPerson(@RequestParam Long id) {
        try {
            PersonDTO personDTO = personService.findPerson(id);
            return ResponseEntity.ok(personDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/delete_person")
    public ResponseEntity<String> deletePerson(@RequestParam Long id) {
        try {
            personService.deletePerson(id);
            return ResponseEntity.ok("Person deleted successfully");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/persons")
    public ResponseEntity<List<PersonDTO>> getAllPersons(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String search
    ) {
        try {
            List<PersonDTO> result = personService.getPersons(page, sortField, sortOrder, search);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all_pages")
    public ResponseEntity<Integer> getCountPersons(
            @RequestParam(required = false) String search
    ) {
        try {
            int totalPages = personService.getTotalPages(search);
            return ResponseEntity.ok(totalPages);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}