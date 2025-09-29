package inf_system.Lab1.db.entities;

import java.util.Arrays;

public enum Country {
    GERMANY("Германия"),
    SPAIN("Испания"),
    VATICAN("Ватикан"),
    SOUTH_KOREA("Северная корея"),
    JAPAN("Япония");

    private final String translation;

    Country(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }

    // Метод для проверки наличия строки среди translation
    public static boolean containsTranslation(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }

        return Arrays.stream(Country.values())
                .anyMatch(country -> country.getTranslation().equals(value));
    }

    // Метод с игнорированием регистра (опционально)
    public static boolean containsTranslationIgnoreCase(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }

        String normalized = value.trim();
        return Arrays.stream(Country.values())
                .anyMatch(country -> country.getTranslation().equalsIgnoreCase(normalized));
    }
}
