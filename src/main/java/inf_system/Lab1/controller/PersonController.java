package inf_system.Lab1.controller;

import inf_system.Lab1.controller.dto.PersonDTO;
import inf_system.Lab1.db.creators.PersonCreator;
import inf_system.Lab1.db.entities.*;
import inf_system.Lab1.db.repositories.CoordinatesRepository;
import inf_system.Lab1.db.repositories.LocationRepository;
import inf_system.Lab1.db.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import static inf_system.Lab1.db.entities.Person.getComparator;

@RestController
public class PersonController {

    @Autowired
    private PersonCreator personCreator;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private CoordinatesRepository coordinatesRepository;

    @PostMapping("/create_person")
    public ResponseEntity<?> createPerson(@RequestBody PersonDTO personDTO) {
        try {
            ResponseEntity<String> result = personDTO.validation();
            if (result != null) {
                return result;
            }

            Double locationX = null;
            Float locationY = null;
            Double locationZ = null;
            if (personDTO.getLocation() != null) {
                locationX = personDTO.getLocation().getX();
                locationY = personDTO.getLocation().getY();
                locationZ = personDTO.getLocation().getZ();
            }

            personCreator.createPerson(
                    null,
                    personDTO.getName(),
                    locationX,
                    locationY,
                    locationZ,
                    personDTO.getCoordinates().getX(),
                    personDTO.getCoordinates().getY(),
                    Color.valueOf(personDTO.getEyeColor()),
                    Color.valueOf(personDTO.getHairColor()),
                    personDTO.getHeight(),
                    Country.valueOf(personDTO.getNationality()),
                    personDTO.getBirthday().atStartOfDay(),
                    LocalDate.now()
            );
            return ResponseEntity.ok("Персона успешно создана");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Неверное значение enum: " + e.getMessage());
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update_person")
    public ResponseEntity<?> updatePerson(@RequestBody PersonDTO personDTO) {
        try {
            ResponseEntity<String> result = personDTO.validation();
            if (result != null) {
                return result;
            }
            personCreator.createPerson(
                    personDTO.getId(),
                    personDTO.getName(),
                    personDTO.getLocation().getX(),
                    personDTO.getLocation().getY(),
                    personDTO.getLocation().getZ(),
                    personDTO.getCoordinates().getX(),
                    personDTO.getCoordinates().getY(),
                    Color.valueOf(personDTO.getEyeColor()),
                    Color.valueOf(personDTO.getHairColor()),
                    personDTO.getHeight(),
                    Country.valueOf(personDTO.getNationality()),
                    personDTO.getBirthday().atStartOfDay(),
                    personDTO.getCreationDate()
            );
            return ResponseEntity.ok("Персона успешно создана");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Неверное значение enum: " + e.getMessage());
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/find_person")
    @Transactional(readOnly = true)
    public ResponseEntity<?> findPerson(
            @RequestParam(required = true) Long id
    ) {
        if (personRepository.existsById(id)) {
            try {
                Person person = personRepository.findById(id).get();
                PersonDTO personDTO = PersonDTO.map(person);
                return new ResponseEntity<>(personDTO, HttpStatus.OK);
            } catch (IllegalArgumentException ex) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Объекты с таким ID не существует", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/delete_person")
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ResponseEntity<String> deletePerson(
            @RequestParam(required = true) Long id
    ) {
        if (personRepository.existsById(id)) {
            try {
                Person person = personRepository.findById(id).orElseThrow();
                Location location = person.getLocation();
                Coordinates coordinates = person.getCoordinates();

                personRepository.deleteById(id);

                // Проверяем и удаляем Location, если больше не используется
                if (location != null && personRepository.countByLocationIdWithLock(location.getId()) == 0) {
                    locationRepository.deleteById(location.getId());
                }

                // Проверяем и удаляем Coordinates, если больше не используется
                if (coordinates != null && personRepository.countByCoordinatesIdWithLock(coordinates.getId()) == 0) {
                    coordinatesRepository.deleteById(coordinates.getId());
                }
                return new ResponseEntity<>("Person delete successfully", HttpStatus.OK);
            } catch (IllegalArgumentException ex) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Объекты с таким ID не существует", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/persons")
    public ResponseEntity<List<PersonDTO>> getAllPersons(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String search
    ) {
        List<Person> persons = filterPerson(search);

        // Сортировка
        Comparator<Person> comparator = getComparator(sortField, sortOrder);
        List<Person> sortedPersons = persons.stream()
                .sorted(comparator)
                .toList();

        // Пагинация
        int pageSize = 10;
        int totalItems = sortedPersons.size();
        int fromIndex = Math.min((page - 1) * pageSize, totalItems);
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        List<Person> paginatedPersons = sortedPersons.subList(fromIndex, toIndex);

        List<PersonDTO> personDTOs = paginatedPersons.stream()
                .map(PersonDTO::map)
                .toList();

        return new ResponseEntity<>(personDTOs, HttpStatus.OK);
    }

    @GetMapping("/all_pages")
    public ResponseEntity<Integer> getCountPersons(
            @RequestParam(required = false) String search
    ) {
        return new ResponseEntity<>((int) Math.ceil((double) filterPerson(search).size() / 10), HttpStatus.OK);
    }

    private List<Person> filterPerson(String search) {
        List<Person> persons;

        if (search == null) {
            persons = personRepository.findAll();
        } else {
            String searchText = search.trim().toLowerCase();
//            persons = personRepository.searchAll(search.toLowerCase());
            persons = personRepository.findAll()
                    .stream()
                    .filter(p -> {
                        // Проверка на null для getLocation()
                        Location location = p.getLocation();

                        // Проверка на null для coordinates
                        Coordinates coordinates = p.getCoordinates();

                        return p.getName().toLowerCase().contains(searchText) ||
                                // Проверка для coordinates.x
                                (coordinates != null && String.valueOf(coordinates.getX()).contains(searchText)) ||
                                // Проверка для coordinates.y
                                (coordinates != null && String.valueOf(coordinates.getY()).contains(searchText)) ||
                                // Проверка для location.x
                                (location != null && String.valueOf(location.getX()).contains(searchText)) ||
                                // Проверка для location.y
                                (location != null && String.valueOf(location.getY()).contains(searchText)) ||
                                // Проверка для location.z
                                (location != null && String.valueOf(location.getZ()).contains(searchText)) ||
                                String.valueOf(p.getHeight()).contains(searchText) ||
                                // Проверка для eyeColor
                                (p.getEyeColor() != null && p.getEyeColor().getTranslation().contains(search)) ||
                                // Проверка для hairColor
                                (p.getHairColor() != null && p.getHairColor().getTranslation().contains(search)) ||
                                // Проверка для nationality
                                (p.getNationality() != null && p.getNationality().getTranslation().contains(search)) ||
                                p.getCreationDate().toString().contains(search) ||
                                (p.getBirthday() != null && p.getBirthday().toLocalDate().toString().contains(search));
                    })
                    .toList();
        }
        return persons;
    }
}