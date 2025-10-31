package inf_system.Lab1.db.entities;

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

    public static boolean isEnumValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        try {
            Color country = Color.valueOf(value.trim());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
