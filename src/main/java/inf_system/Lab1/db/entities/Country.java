package inf_system.Lab1.db.entities;

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

    public static boolean isEnumValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        try {
            Country country = Country.valueOf(value.trim());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}