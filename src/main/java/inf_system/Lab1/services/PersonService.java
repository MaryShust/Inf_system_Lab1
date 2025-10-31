package inf_system.Lab1.services;

import inf_system.Lab1.controller.dto.PersonDTO;
import inf_system.Lab1.controller.exception.NotFoundException;
import inf_system.Lab1.controller.exception.ValidationException;
import inf_system.Lab1.db.creators.PersonCreator;
import inf_system.Lab1.db.entities.*;
import inf_system.Lab1.db.repositories.CoordinatesRepository;
import inf_system.Lab1.db.repositories.LocationRepository;
import inf_system.Lab1.db.repositories.PersonRepository;
import inf_system.Lab1.helper.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import static inf_system.Lab1.db.entities.Person.getComparator;

@Service
public class PersonService {

    @Autowired
    private PersonCreator personCreator;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private CoordinatesRepository coordinatesRepository;

    @Transactional
    public void createPerson(PersonDTO personDTO) {
        String validationResult = Validation.validation(personDTO);
        if (validationResult != null) {
            throw new ValidationException(validationResult);
        }

        personCreator.createPerson(
                personDTO,
                null,
                LocalDate.now()
        );
    }

    @Transactional
    public int uploadPersons(String author, List<PersonDTO> personsDTO) throws IllegalArgumentException {
        try {
            for (PersonDTO personDTO : personsDTO) {
                String validationResult = Validation.validation(personDTO);
                if (validationResult != null) {
                    System.out.println("ERROOOOR");
                    throw new ValidationException(validationResult);
                }
            }
            List<Person> persons = personCreator.createPersons(personsDTO);
            return persons.size();
        } catch(Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @Transactional
    public void updatePerson(PersonDTO personDTO) {
        // 🔒 Блокируем Person для обновления
        personRepository.findByIdWithLock(personDTO.getId())
                .orElseThrow(() -> new NotFoundException("Объекта с таким ID не существует"));

        String validationResult = Validation.validation(personDTO);
        if (validationResult != null) {
            throw new ValidationException(validationResult);
        }
        personCreator.createPerson(
                personDTO,
                personDTO.getId(),
                personDTO.getCreationDate()
        );
    }

    @Transactional(readOnly = true)
    public PersonDTO findPerson(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Объекты с таким ID не существует"));
        return PersonDTO.map(person);
    }

    @Transactional
    public void deletePerson(Long id) {
        Person person = personRepository.findByIdWithLock(id)
                .orElseThrow(() -> new NotFoundException("Объекта с таким ID не существует"));
        Location location = person.getLocation();
        Coordinates coordinates = person.getCoordinates();
        Long locationId = location != null ? location.getId() : null;
        Long coordinatesId = coordinates != null ? coordinates.getId() : null;

        personRepository.deleteById(id);

        // Проверяем и удаляем Location, если больше не используется
        if (locationId != null) {
            locationRepository.deleteIfUnused(locationId);
        }

        // Проверяем и удаляем Coordinates, если больше не используется
        if (coordinatesId != null) {
            coordinatesRepository.deleteIfUnused(coordinatesId);
        }
    }

    public List<PersonDTO> getPersons(int page, String sortField, String sortOrder, String search) {
        List<Person> persons = filterPersons(search);

        Comparator<Person> comparator = getComparator(sortField, sortOrder);
        List<Person> sortedPersons = persons.stream()
                .sorted(comparator)
                .toList();

        int pageSize = 10;
        int totalItems = sortedPersons.size();
        int fromIndex = Math.min((page - 1) * pageSize, totalItems);
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        List<Person> paginatedPersons = sortedPersons.subList(fromIndex, toIndex);

        return paginatedPersons.stream()
                .map(PersonDTO::map)
                .toList();
    }

    public int getTotalPages(String search) {
        List<Person> persons = filterPersons(search);
        return (int) Math.ceil((double) persons.size() / 10);
    }

    private List<Person> filterPersons(String search) {
        if (search == null) {
            return personRepository.findAll();
        }

        String searchText = search.trim().toLowerCase();
        return personRepository.findAll()
                .stream()
                .filter(p -> matchesSearch(p, searchText))
                .toList();
    }

    private boolean matchesSearch(Person person, String searchText) {
        return matchesBasicFields(person, searchText)
                || matchesCoordinates(person.getCoordinates(), searchText)
                || matchesLocation(person.getLocation(), searchText)
                || matchesColorFields(person, searchText)
                || matchesDateFields(person, searchText);
    }

    private boolean matchesBasicFields(Person person, String searchText) {
        return person.getName().toLowerCase().contains(searchText)
                || String.valueOf(person.getHeight()).contains(searchText);
    }

    private boolean matchesCoordinates(Coordinates coordinates, String searchText) {
        return coordinates != null
                && (String.valueOf(coordinates.getX()).contains(searchText)
                || String.valueOf(coordinates.getY()).contains(searchText));
    }

    private boolean matchesLocation(Location location, String searchText) {
        return location != null
                && (String.valueOf(location.getX()).contains(searchText)
                || String.valueOf(location.getY()).contains(searchText)
                || String.valueOf(location.getZ()).contains(searchText));
    }

    private boolean matchesColorFields(Person person, String searchText) {
        return (person.getEyeColor() != null && person.getEyeColor().getTranslation().contains(searchText))
                || (person.getHairColor() != null && person.getHairColor().getTranslation().contains(searchText))
                || (person.getNationality() != null && person.getNationality().getTranslation().contains(searchText));
    }

    private boolean matchesDateFields(Person person, String searchText) {
        return person.getCreationDate().toString().contains(searchText)
                || (person.getBirthday() != null && person.getBirthday().toLocalDate().toString().contains(searchText));
    }
}