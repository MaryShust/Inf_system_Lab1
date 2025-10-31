package inf_system.Lab1.db.creators;

import inf_system.Lab1.controller.dto.PersonDTO;
import inf_system.Lab1.db.entities.Color;
import inf_system.Lab1.db.entities.Country;
import inf_system.Lab1.db.entities.Person;
import inf_system.Lab1.db.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonCreator {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private LocationCreator locationCreator;
    @Autowired
    private CoordinatesCreator coordinatesCreator;

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
        person.setEyeColor(Color.valueOf(personDTO.getEyeColor()));
        person.setHairColor(Color.valueOf(personDTO.getHairColor()));
        person.setLocation(locationCreator.createLocation(locationX, locationY, locationZ));
        person.setHeight(personDTO.getHeight());
        person.setBirthday(personDTO.getBirthday().atStartOfDay());
        person.setNationality(Country.valueOf(personDTO.getNationality()));
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
    public List<Person> createPersons(
            List<PersonDTO> personsDTO
    ) {
        List<Person> persons = new ArrayList<>();
        for (PersonDTO personDTO : personsDTO) {
            Person person = map(personDTO, null, LocalDate.now());
            persons.add(person);
        }
        return personRepository.saveAll(persons);
    }
}