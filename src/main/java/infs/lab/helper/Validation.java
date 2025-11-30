package infs.lab.helper;

import infs.lab.controller.dto.PersonDTO;
import infs.lab.db.entities.Color;
import infs.lab.db.entities.Country;

public class Validation {

    public static String validation(PersonDTO personDTO) {
        if (personDTO.getName() == null || personDTO.getName().trim().isEmpty()) {
            return "Имя не может быть пустым";
        }

        if (personDTO.getBirthday() == null || personDTO.getBirthday().toString().isEmpty()) {
            return "Дата рождения обязательна";
        }

        if (personDTO.getNationality() == null || personDTO.getNationality().trim().isEmpty()) {
            return "Национальность обязательна";
        }

        if (personDTO.getNationality() != null && !personDTO.getNationality().trim().isEmpty() && Country.fromTranslation(personDTO.getNationality()) == null) {
            return "Национальность строго определенных значений";
        }

        if (Country.fromTranslation(personDTO.getNationality()) == Country.GERMANY &&
                !(Color.fromTranslation(personDTO.getEyeColor()) == Color.BLUE && Color.fromTranslation(personDTO.getHairColor()) == Color.YELLOW)
        ) {
            return "У немцев могут быть только блондинами с голубыми глазами";
        }

        if (Country.fromTranslation(personDTO.getNationality()) == Country.JAPAN &&
                !(personDTO.getHeight() <= 170 && Color.fromTranslation(personDTO.getEyeColor()) == Color.BLACK)
        ) {
            return "У японцев могут быть только черные волосы и рост не выше 170";
        }

        if (Country.fromTranslation(personDTO.getNationality()) == Country.SOUTH_KOREA &&
                !(Color.fromTranslation(personDTO.getEyeColor()) == Color.BLACK && Color.fromTranslation(personDTO.getHairColor()) ==Color.BLACK)
        ) {
            return "У корейцев могут быть только черные волосы и глаза";
        }

        if (personDTO.getHeight() < 1) {
            return "Рост должен быть ≥ 1";
        }

        if (personDTO.getHairColor() == null || personDTO.getHairColor().trim().isEmpty()) {
            return "Цвет волос обязателен";
        }

        if (personDTO.getHairColor() != null && !personDTO.getHairColor().trim().isEmpty() && Color.fromTranslation(personDTO.getHairColor()) == null) {
            return "Цвет волос строго определенных значений";
        }

        if (personDTO.getEyeColor() != null && !personDTO.getEyeColor().trim().isEmpty() && Color.fromTranslation(personDTO.getEyeColor()) == null) {
            return "Цвет глаз строго определенных значений";
        }

        if (personDTO.getCoordinates() == null || personDTO.getCoordinates().getX() == null ||
        personDTO.getCoordinates().getY() == null) {
            return "Все координаты (X, Y) обязательны";
        }

        if (personDTO.getCoordinates() != null && personDTO.getCoordinates().getX() != null &&
                personDTO.getCoordinates().getX() > 674) {
            return "Координата X должна быть не больше 674";
        }

        if (personDTO.getCoordinates() != null && personDTO.getCoordinates().getY() != null &&
                personDTO.getCoordinates().getY() < -554) {
            return "Координата Y не должна быть меньше -554";
        }
        return null;
    }
}