package infs.lab.services;

import infs.lab.aop.CacheLogging;
import infs.lab.controller.dto.PersonDTO;
import infs.lab.controller.exception.NotFoundException;
import infs.lab.controller.exception.ParsingException;
import infs.lab.controller.exception.ValidationException;
import infs.lab.db.creators.PersonCreator;
import infs.lab.db.entities.Coordinates;
import infs.lab.db.entities.Location;
import infs.lab.db.entities.Person;
import infs.lab.db.repositories.CoordinatesRepository;
import infs.lab.db.repositories.LocationRepository;
import infs.lab.db.repositories.PersonRepository;
import infs.lab.helper.FileParser;
import infs.lab.helper.Validation;
import infs.lab.controller.exception.UniqueViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class PersonService {

    private final PersonCreator personCreator;
    private final PersonRepository personRepository;
    private final LocationRepository locationRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final Clock clock;
    private final FileParser fileParser;

    @Autowired
    public PersonService(
            PersonCreator personCreator,
            PersonRepository personRepository,
            LocationRepository locationRepository,
            CoordinatesRepository coordinatesRepository,
            Clock clock,
            FileParser fileParser
    ) {
        this.personCreator = personCreator;
        this.personRepository = personRepository;
        this.locationRepository = locationRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.clock = clock;
        this.fileParser = fileParser;
    }

    @Transactional
    public void createPerson(PersonDTO personDTO) {
        String validationResult = Validation.validation(personDTO);
        if (validationResult != null) {
            throw new ValidationException(validationResult);
        }

        personCreator.createPerson(
                personDTO,
                /* id */ null,
                LocalDate.now(clock)
        );
    }

    @Transactional
    public int uploadPeople(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            List<PersonDTO> peopleDTO = fileParser.parseFileContent(inputStream, file.getOriginalFilename());

            peopleDTO.stream()
                    .forEach(personDTO -> {
                        boolean isDuplicate = personRepository.findByNameAndHeightWithLock(
                                        personDTO.name(),
                                        personDTO.height()
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
        } catch (Exception ex) {
            throw new ParsingException(ex.getMessage());
        }
    }

    @Transactional
    public void updatePerson(PersonDTO personDTO) {
        personRepository.findByIdWithLock(personDTO.id())
                .orElseThrow(() -> new NotFoundException("Объекта с таким ID не существует"));

        if (!personRepository.findOtherPeopleWithNameAndHeightWithLock(
                personDTO.id(),
                personDTO.name(),
                personDTO.height()
        ).get().isEmpty()) {
            throw new UniqueViolationException("Объект должен быть уникальным по имени и росту");
        }

        String validationResult = Validation.validation(personDTO);
        if (validationResult != null) {
            throw new ValidationException(validationResult);
        }
        personCreator.createPerson(
                personDTO,
                personDTO.id(),
                personDTO.creationDate()
        );
    }

    @CacheLogging
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

        if (locationId != null) {
            locationRepository.deleteIfUnused(locationId);
        }

        if (coordinatesId != null) {
            coordinatesRepository.deleteIfUnused(coordinatesId);
        }
    }

    @CacheLogging
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

    @CacheLogging
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