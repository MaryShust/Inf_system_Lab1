package infs.lab.controller.dto;

import infs.lab.db.entities.Person;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;
import java.util.Optional;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@ToString
@Schema(description = "Человек")
public class PersonDTO {

    @Schema(description = "ID человека", example = "1")
    private Long id;

    @Schema(description = "Имя", example = "Иван Иванов", required = true)
    private String name;

    @Schema(description = "Координаты", required = true)
    private CoordinatesDTO coordinates;

    @Schema(description = "Цвет глаз", example = "Синий",
            allowableValues = {"Черный", "Синий", "Желтый", "Оранжевый", "Белый"})
    private String eyeColor;

    @Schema(description = "Цвет волос", example = "Черный", required = true,
            allowableValues = {"Черный", "Синий", "Желтый", "Оранжевый", "Белый"})
    private String hairColor;

    @Schema(description = "Локация")
    private LocationDTO location;

    @Schema(description = "Рост", example = "180", minimum = "1")
    private int height;

    @Schema(description = "Дата рождения", example = "1990-01-15", required = true)
    private LocalDate birthday;

    @Schema(description = "Национальность", example = "Германия", required = true,
            allowableValues = {"Германия", "Испания", "Ватикан", "Северная Корея", "Япония"})
    private String nationality;

    @Schema(description = "Дата создания", example = "2024-01-01", required = true)
    private LocalDate creationDate = LocalDate.now();

    public void setBirthday(Optional<String> birthday) {
        if (birthday.isPresent()) {
            this.birthday = LocalDate.parse(birthday.get());
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
        personDTO.birthday = person.getBirthday().toLocalDate();
        personDTO.nationality = person.getNationality().getTranslation();
        personDTO.creationDate = person.getCreationDate();
        return personDTO;
    }
}