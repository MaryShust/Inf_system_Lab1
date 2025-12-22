package infs.lab.helper;

import infs.lab.controller.dto.PersonDTO;
import infs.lab.db.entities.Color;
import infs.lab.db.entities.Country;

public class Validation {

    public static String validation(PersonDTO personDTO) {
        if (personDTO.name() == null || personDTO.name().trim().isEmpty()) {
            return "Имя не может быть пустым";
        }

        if (personDTO.birthday() == null || personDTO.birthday().toString().isEmpty()) {
            return "Дата рождения обязательна";
        }

        if (personDTO.nationality() == null || personDTO.nationality().trim().isEmpty()) {
            return "Национальность обязательна";
        }

        if (Country.fromTranslation(personDTO.nationality()) == null) {
            return "Национальность строго определенных значений";
        }

        if (Country.fromTranslation(personDTO.nationality()) == Country.GERMANY &&
                !(Color.fromTranslation(personDTO.eyeColor()) == Color.BLUE && Color.fromTranslation(personDTO.hairColor()) == Color.YELLOW)
        ) {
            return "У немцев могут быть только блондинами с голубыми глазами";
        }

        if (Country.fromTranslation(personDTO.nationality()) == Country.JAPAN &&
                !(personDTO.height() <= 170 && Color.fromTranslation(personDTO.eyeColor()) == Color.BLACK)
        ) {
            return "У японцев могут быть только черные волосы и рост не выше 170";
        }

        if (Country.fromTranslation(personDTO.nationality()) == Country.SOUTH_KOREA &&
                !(Color.fromTranslation(personDTO.eyeColor()) == Color.BLACK && Color.fromTranslation(personDTO.hairColor()) ==Color.BLACK)
        ) {
            return "У корейцев могут быть только черные волосы и глаза";
        }

        if (personDTO.height() < 1) {
            return "Рост должен быть ≥ 1";
        }

        if (personDTO.hairColor() == null || personDTO.hairColor().trim().isEmpty()) {
            return "Цвет волос обязателен";
        }

        if (Color.fromTranslation(personDTO.hairColor()) == null) {
            return "Цвет волос строго определенных значений";
        }

        if (personDTO.eyeColor() != null && !personDTO.eyeColor().trim().isEmpty() && Color.fromTranslation(personDTO.eyeColor()) == null) {
            return "Цвет глаз строго определенных значений";
        }

        if (personDTO.coordinates() == null || personDTO.coordinates().x() == null ||
        personDTO.coordinates().y() == null) {
            return "Все координаты (X, Y) обязательны";
        }

        if (personDTO.coordinates().x() > 674) {
            return "Координата X должна быть не больше 674";
        }

        if (personDTO.coordinates().y() < -554) {
            return "Координата Y не должна быть меньше -554";
        }
        return null;
    }
}