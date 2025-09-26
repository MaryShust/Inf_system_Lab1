package inf_system.Lab1.controller.dto;

import inf_system.Lab1.db.entities.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

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

    public Long getId() {
        return id;
    }
    public void setId(Long id) { this.id = id; }

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public CoordinatesDTO getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(CoordinatesDTO coordinates) { this.coordinates = coordinates; }

    public String getEyeColor() {
        return eyeColor;
    }
    public void setEyeColor(String eyeColor) { this.eyeColor = eyeColor; }

    public String getHairColor() {
        return hairColor;
    }
    public void setHairColor(String hairColor) { this.hairColor = hairColor; }

    public LocationDTO getLocation() {
        return location;
    }
    public void setLocation(LocationDTO location) { this.location = location; }

    public int getHeight() {
        return height;
    }
    public void setHeight(Integer height) { this.height = height; }

    public LocalDate getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) { this.birthday = LocalDate.parse(birthday); }

    public String getNationality() {
        return nationality;
    }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public LocalDate getCreationDate() {
        return creationDate;
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

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", eyeColor=" + eyeColor +
                ", hairColor=" + hairColor +
                ", location=" + location +
                ", height=" + height +
                ", birthday=" + birthday +
                ", nationality=" + nationality +
                '}';
    }

    public ResponseEntity<String> validation() {
        // Валидация обязательных полей
        if (getName() == null || getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Имя не может быть пустым");
        }

        if (getBirthday() == null || getBirthday().toString().isEmpty()) {
            return ResponseEntity.badRequest().body("Дата рождения обязательна");
        }

        if (getNationality() == null || getNationality().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Национальность обязательна");
        }

        if (getNationality() != null && !getNationality().trim().isEmpty() && Country.containsTranslation(getNationality())) {
            return ResponseEntity.badRequest().body("Национальность строго определенных значений");
        }

        if (getHeight() < 1) {
            return ResponseEntity.badRequest().body("Рост должен быть ≥ 1");
        }

        if (getHairColor() == null || getHairColor().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Цвет волос обязателен");
        }

        if (getHairColor() != null && !getHairColor().trim().isEmpty() && Color.containsTranslation(getHairColor())) {
            return ResponseEntity.badRequest().body("Цвет волос строго определенных значений");
        }

        if (getEyeColor() != null && !getEyeColor().trim().isEmpty() && Color.containsTranslation(getEyeColor())) {
            return ResponseEntity.badRequest().body("Цвет глаз строго определенных значений");
        }

        if (getCoordinates() == null || getCoordinates().getX() == null ||
                getCoordinates().getY() == null) {
            return ResponseEntity.badRequest().body("Все координаты (X, Y) обязательны");
        }

        if (getCoordinates() != null && getCoordinates().getX() != null &&
                getCoordinates().getX() > 674) {
            return ResponseEntity.badRequest().body("Координата X должна быть не больше 674");
        }

        if (getCoordinates() != null && getCoordinates().getY() != null &&
                getCoordinates().getY() < -554) {
            return ResponseEntity.badRequest().body("Координата Y не должна быть меньше -554");
        }
        return null;
    }
}