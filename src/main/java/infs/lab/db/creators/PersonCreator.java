package infs.lab.db.creators;

import infs.lab.controller.dto.PersonDTO;
import infs.lab.db.entities.Color;
import infs.lab.db.entities.Country;
import infs.lab.db.entities.Person;
import infs.lab.db.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersonCreator {

    private final PersonRepository personRepository;
    private final LocationCreator locationCreator;
    private final CoordinatesCreator coordinatesCreator;
    private final Clock clock;

    @Autowired
    public PersonCreator(
            PersonRepository personRepository,
            LocationCreator locationCreator,
            CoordinatesCreator coordinatesCreator,
            Clock clock
    ) {
        this.personRepository = personRepository;
        this.locationCreator = locationCreator;
        this.coordinatesCreator = coordinatesCreator;
        this.clock = clock;
    }

    private Person map(
            PersonDTO personDTO,
            Long id,
            LocalDate creationDate
    ) {
        Double locationX = null;
        Float locationY = null;
        Double locationZ = null;
        if (personDTO.getLocation() != null) {
            locationX = personDTO.getLocation().getX();
            locationY = personDTO.getLocation().getY();
            locationZ = personDTO.getLocation().getZ();
        }

        Person person = new Person();
        person.setId(id);
        person.setName(personDTO.getName());
        person.setCoordinates(
                coordinatesCreator.createCoordinates(
                        personDTO.getCoordinates().getX(),
                        personDTO.getCoordinates().getY()
                )
        );
        person.setEyeColor(Color.fromTranslation(personDTO.getEyeColor()));
        person.setHairColor(Color.fromTranslation(personDTO.getHairColor()));
        person.setLocation(locationCreator.createLocation(locationX, locationY, locationZ));
        person.setHeight(personDTO.getHeight());
        person.setBirthday(personDTO.getBirthday().atStartOfDay());
        person.setNationality(Country.fromTranslation(personDTO.getNationality()));
        person.setCreationDate(creationDate);

        return person;
    }

    @Transactional
    public Person createPerson(
            PersonDTO personDTO,
            Long id,
            LocalDate creationDate
    ) {
        return personRepository.save(map(personDTO, id, creationDate));
    }

    @Transactional
    public List<Person> createPeople(
            List<PersonDTO> peopleDTO
    ) {
        List<Person> people = peopleDTO.stream()
                .map(personDTO -> map(personDTO, null, LocalDate.now(clock)))
                .collect(Collectors.toList());
        return personRepository.saveAll(people);
    }
}