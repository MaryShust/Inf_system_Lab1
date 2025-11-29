package infSystem.Lab1.services;

import infSystem.Lab1.controller.dto.PersonDTO;
import infSystem.Lab1.controller.exception.NotFoundException;
import infSystem.Lab1.controller.exception.ValidationException;
import infSystem.Lab1.db.creators.PersonCreator;
import infSystem.Lab1.db.entities.Coordinates;
import infSystem.Lab1.db.entities.Location;
import infSystem.Lab1.db.entities.Person;
import infSystem.Lab1.db.repositories.CoordinatesRepository;
import infSystem.Lab1.db.repositories.LocationRepository;
import infSystem.Lab1.db.repositories.PersonRepository;
import infSystem.Lab1.helper.Validation;
import infSystem.Lab1.controller.exception.UniqueViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

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
        if (!personRepository.findByNameAndHeightWithLock(personDTO.getName(), personDTO.getHeight()).get().isEmpty()) {
            throw new UniqueViolationException("Объект должен быть уникальным по имени и росту");
        }

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
    public int uploadPeople(String author, List<PersonDTO> peopleDTO) throws IllegalArgumentException {
        try {
            peopleDTO.stream()
                    .forEach(personDTO -> {
                        boolean isDuplicate = personRepository.findByNameAndHeightWithLock(
                                        personDTO.getName(),
                                        personDTO.getHeight()
                                )
                                .get()
                                .stream()
                                .anyMatch(p -> true);

                        if (isDuplicate) {
                            throw new UniqueViolationException("Объект должен быть уникальным по имени и росту");
                        }

                        String validationResult = Validation.validation(personDTO);
                        if (validationResult != null) {
                            throw new ValidationException(validationResult);
                        }
                    });
            List<Person> people = personCreator.createPeople(peopleDTO);
            return people.size();
        } catch(Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @Transactional
    public void updatePerson(PersonDTO personDTO) {
        personRepository.findByIdWithLock(personDTO.getId())
                .orElseThrow(() -> new NotFoundException("Объекта с таким ID не существует"));

        if (!personRepository.findByNameAndHeightWithLock(personDTO.getName(), personDTO.getHeight()).get().isEmpty()) {
            throw new UniqueViolationException("Объект должен быть уникальным по имени и росту");
        }

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

    public List<PersonDTO> getPeople(int page, String sortField, String sortOrder, String search) {
        List<Person> people = filterPeople(search);

        Comparator<Person> comparator = Person.getComparator(sortField, sortOrder);
        List<Person> sortedPeople = people.stream()
                .sorted(comparator)
                .toList();

        int pageSize = 10;
        int totalItems = sortedPeople.size();
        int fromIndex = Math.min((page - 1) * pageSize, totalItems);
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        List<Person> paginatedPeople = sortedPeople.subList(fromIndex, toIndex);

        return paginatedPeople.stream()
                .map(PersonDTO::map)
                .toList();
    }

    public int getTotalPages(String search) {
        List<Person> people = filterPeople(search);
        return (int) Math.ceil((double) people.size() / 10);
    }

    private List<Person> filterPeople(String search) {
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