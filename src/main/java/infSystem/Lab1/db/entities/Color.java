package infSystem.Lab1.db.entities;

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

    public static Color fromTranslation(String translation) {
        if (translation == null || translation.trim().isEmpty()) {
            return null;
        }
        for (Color color : values()) {
            if (color.getTranslation().equals(translation.trim())) {
                return color;
            }
        }
        return null;
    }
}