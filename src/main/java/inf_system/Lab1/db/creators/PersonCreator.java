package inf_system.Lab1.db.creators;

import inf_system.Lab1.db.entities.Color;
import inf_system.Lab1.db.entities.Country;
import inf_system.Lab1.db.entities.Person;
import inf_system.Lab1.db.repositories.PersonRepository;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class PersonCreator {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private LocationCreator locationCreator;
    @Autowired
    private CoordinatesCreator coordinatesCreator;

    @Transactional
    public Person createPerson(
            Long id,
            @NotNull String name,
            Double locationX,
            Float locationY,
            Double locationZ,
            int coordinatesX,
            int coordinatesY,
            Color eyeColor,
            @NotNull Color hairColor,
            int height,
            @NotNull Country nationality,
            LocalDateTime birthday,
            LocalDate creationDate
    ) {
        Person person = new Person();
        person.setId(id);
        person.setName(name);
        person.setCoordinates(coordinatesCreator.createCoordinates(coordinatesX, coordinatesY));
        person.setEyeColor(eyeColor);
        person.setHairColor(hairColor);
        person.setLocation(locationCreator.createLocation(locationX, locationY, locationZ));
        person.setHeight(height);
        person.setBirthday(birthday);
        person.setNationality(nationality);
        person.setCreationDate(creationDate);
        return personRepository.save(person);
    }
}