package inf_system.Lab1.controller.dto;

import inf_system.Lab1.db.entities.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
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

    public void setBirthday(String birthday) { this.birthday = LocalDate.parse(birthday); }

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PersonDTO{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", coordinates=").append(coordinates);
        sb.append(", creationDate=").append(creationDate);
        sb.append(", eyeColor=").append(eyeColor);
        sb.append(", hairColor=").append(hairColor);
        sb.append(", location=").append(location);
        sb.append(", height=").append(height);
        sb.append(", birthday=").append(birthday);
        sb.append(", nationality=").append(nationality);
        sb.append('}');
        return sb.toString();
    }
}