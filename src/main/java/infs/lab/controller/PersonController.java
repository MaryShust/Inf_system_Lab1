package infs.lab.controller;

import infs.lab.controller.dto.PersonDTO;
import infs.lab.controller.exception.ValidationException;
import infs.lab.controller.exception.UniqueViolationException;
import infs.lab.services.AuthService;
import infs.lab.services.HistoryService;
import infs.lab.services.PersonService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;
    @Autowired
    private AuthService authService;
    @Autowired
    private HistoryService historyService;

    @PostMapping("/create_person")
    public ResponseEntity<?> createPerson(@RequestBody PersonDTO personDTO) {
        personService.createPerson(personDTO);
        return ResponseEntity.ok("Персона успешно создана");
    }

    @PostMapping("/upload_from_file")
    public ResponseEntity<?> uploadPeople(HttpServletRequest request, @RequestBody List<PersonDTO> people) {
        String userName = authService.getUserName(request);
        try {
            int size = personService.uploadPeople(userName, people);
            historyService.updateHistory(userName, size);
            return ResponseEntity.ok("Персоны успешно загружены и созданы");
        } catch (ValidationException | UniqueViolationException e) {
            historyService.updateHistory(userName, 0);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception ex) {
            historyService.updateHistory(userName, 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/update_person")
    public ResponseEntity<?> updatePerson(@RequestBody PersonDTO personDTO) {
        personService.updatePerson(personDTO);
        return ResponseEntity.ok("Персона успешно обновлена");
    }

    @GetMapping("/find_person")
    public ResponseEntity<?> findPerson(@RequestParam Long id) {
        PersonDTO personDTO = personService.findPerson(id);
        return ResponseEntity.ok(personDTO);
    }
    @GetMapping("/delete_person")
    public ResponseEntity<String> deletePerson(@RequestParam Long id) {
        personService.deletePerson(id);
        return ResponseEntity.ok("Person deleted successfully");
    }

    @GetMapping("/persons")
    public ResponseEntity<List<PersonDTO>> getAllPeople(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String search
    ) {
        List<PersonDTO> result = personService.getPeople(page, sortField, sortOrder, search);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all_pages")
    public ResponseEntity<Integer> getCountPeople(
            @RequestParam(required = false) String search
    ) {
        int totalPages = personService.getTotalPages(search);
        return ResponseEntity.ok(totalPages);
    }
}