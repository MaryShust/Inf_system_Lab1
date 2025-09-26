package inf_system.Lab1.db.entities;

import java.util.Arrays;

public enum Color {
    BLACK("Черный"),
    BLUE("Синий"),
    YELLOW("Желтый"),
    ORANGE("Оранжевый"),
    WHITE("Белый");

    private final String translation;

    Color(String translation) {
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

        return Arrays.stream(Color.values())
                .anyMatch(color -> color.getTranslation().equals(value));
    }

    // Метод с игнорированием регистра (опционально)
    public static boolean containsTranslationIgnoreCase(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }

        String normalized = value.trim();
        return Arrays.stream(Color.values())
                .anyMatch(color -> color.getTranslation().equalsIgnoreCase(normalized));
    }
}