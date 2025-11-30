package infs.lab.controller.dto;

import infs.lab.db.entities.Person;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class PersonDTO {

    private Long id;
    private String name;
    private CoordinatesDTO coordinates;
    private String eyeColor;
    private String hairColor;
    private LocationDTO location;
    private int height;
    private LocalDate birthday;
    private String nationality;
    private LocalDate creationDate = LocalDate.now();

    public void setBirthday(String birthday) {
        if (birthday != null && !birthday.trim().isEmpty()) {
            this.birthday = LocalDate.parse(birthday);
        }
    }

    public static PersonDTO map(Person person) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.id = person.getId();
        personDTO.name = person.getName();
        personDTO.coordinates = CoordinatesDTO.map(person.getCoordinates());
        personDTO.eyeColor = person.getEyeColor().getTranslation();
        personDTO.hairColor = person.getHairColor().getTranslation();
        personDTO.location = LocationDTO.map(person.getLocation());
        personDTO.height = person.getHeight();
        personDTO.birthday = person.getBirthday().toLocalDate();;
        personDTO.nationality = person.getNationality().getTranslation();
        personDTO.creationDate = person.getCreationDate();
        return personDTO;
    }
}