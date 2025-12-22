package infs.lab.controller.dto;

import infs.lab.db.entities.Person;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Человек")
public record PersonDTO(

    @Schema(description = "ID человека", example = "1") Long id,

    @Schema(description = "Имя", example = "Иван Иванов", required = true) String name,

    @Schema(description = "Координаты", required = true) CoordinatesDTO coordinates,

    @Schema(description = "Цвет глаз", example = "Синий",
            allowableValues = {"Черный", "Синий", "Желтый", "Оранжевый", "Белый"})
    String eyeColor,

    @Schema(description = "Цвет волос", example = "Черный", required = true,
            allowableValues = {"Черный", "Синий", "Желтый", "Оранжевый", "Белый"})
    String hairColor,

    @Schema(description = "Локация") LocationDTO location,

    @Schema(description = "Рост", example = "180", minimum = "1") int height,

    @Schema(description = "Дата рождения", example = "1990-01-15", required = true)
    LocalDate birthday,

    @Schema(description = "Национальность", example = "Германия", required = true,
            allowableValues = {"Германия", "Испания", "Ватикан", "Северная Корея", "Япония"})
    String nationality,

    @Schema(description = "Дата создания", example = "2024-01-01", required = true)
    LocalDate creationDate,

    @Schema(description = "Id фото") String photoId
) {

    public static PersonDTO map(Person person) {
        return new PersonDTO(
                person.getId(),
                person.getName(),
                CoordinatesDTO.map(person.getCoordinates()),
                person.getEyeColor().getTranslation(),
                person.getHairColor().getTranslation(),
                LocationDTO.map(person.getLocation()),
                person.getHeight(),
                person.getBirthday().toLocalDate(),
                person.getNationality().getTranslation(),
                person.getCreationDate(),
                person.getPhotoId()
        );
    }
}