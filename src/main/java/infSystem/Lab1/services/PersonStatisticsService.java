package infSystem.Lab1.services;

import infSystem.Lab1.db.entities.Color;
import infSystem.Lab1.db.entities.Person;
import infSystem.Lab1.db.repositories.PersonRepository;
import infSystem.Lab1.controller.exception.NotFoundException;
import org.springframework.stereotype.Service;
import infSystem.Lab1.controller.dto.PersonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@Service
public class PersonStatisticsService {

    @Autowired
    private PersonRepository personRepository;

    public BigDecimal getAverageHeight() {
        List<Person> people = personRepository.findAll();
        if (people.isEmpty()) {
            throw new NotFoundException("объектов нет");
        }
        double result =  people.stream()
                .mapToDouble(Person::getHeight)
                .average()
                .orElse(0.0);
        return new BigDecimal(result)
                .setScale(3, RoundingMode.HALF_UP);
    }

    public PersonDTO getPersonWithMaxBirthday() {
        List<Person> people = personRepository.findAll();
        if (people.isEmpty()) {
            throw new NotFoundException("Объектов нет");
        }
        Person person = people.stream()
                .max(Comparator.comparing(Person::getBirthday))
                .orElseThrow(() -> new NotFoundException("Объектов нет"));
        return PersonDTO.map(person);
    }

    public List<PersonDTO> getTallPeople(int minHeight) {
        List<PersonDTO> result = personRepository.findAll()
                .stream()
                .filter(p -> p.getHeight() > minHeight)
                .map(PersonDTO::map)
                .toList();
        if (result.isEmpty()) {
            throw new NotFoundException("Объектов нет");
        }
        return result;
    }

    public long countByHairColor(String hairColor) {
        List<Person> people = personRepository.findAll();
        if (people.isEmpty()) {
            throw new NotFoundException("объектов нет");
        }
        Color color = Color.valueOf(hairColor);
        return people.stream()
                .filter(p -> p.getHairColor().equals(color))
                .count();
    }

    public long countByHairColorInLocation(
            String hairColor,
            double xMin,
            double xMax,
            float yMin,
            float yMax,
            double zMin,
            double zMax
    ) {
        List<Person> people = personRepository.findAll();
        if (people.isEmpty()) {
            throw new NotFoundException("объектов нет");
        }
        Color color = Color.valueOf(hairColor);
        return people.stream()
                .filter(p -> p.getHairColor().equals(color) &&
                        p.getLocation() != null &&
                        p.getLocation().getX() >= xMin &&
                        p.getLocation().getX() <= xMax &&
                        p.getLocation().getY() >= yMin &&
                        p.getLocation().getY() <= yMax &&
                        p.getLocation().getZ() >= zMin &&
                        p.getLocation().getZ() <= zMax)
                .count();
    }
}