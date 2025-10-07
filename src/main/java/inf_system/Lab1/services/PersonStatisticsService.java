package inf_system.Lab1.services;

import inf_system.Lab1.controller.exception.NotFoundException;
import org.springframework.stereotype.Service;
import inf_system.Lab1.controller.dto.PersonDTO;
import inf_system.Lab1.db.entities.*;
import inf_system.Lab1.db.repositories.PersonRepository;
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
        List<Person> persons = personRepository.findAll();
        if (persons.isEmpty()) {
            throw new NotFoundException("объектов нет");
        }
        double result =  persons.stream()
                .mapToDouble(Person::getHeight)
                .average()
                .orElse(0.0);
        return new BigDecimal(result)
                .setScale(3, RoundingMode.HALF_UP);
    }

    public PersonDTO getPersonWithMaxBirthday() {
        List<Person> persons = personRepository.findAll();
        if (persons.isEmpty()) {
            throw new NotFoundException("Объектов нет");
        }
        Person person = persons.stream()
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
        List<Person> persons = personRepository.findAll();
        if (persons.isEmpty()) {
            throw new NotFoundException("объектов нет");
        }
        Color color = Color.valueOf(hairColor);
        return persons.stream()
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
        List<Person> persons = personRepository.findAll();
        if (persons.isEmpty()) {
            throw new NotFoundException("объектов нет");
        }
        Color color = Color.valueOf(hairColor);
        return persons.stream()
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