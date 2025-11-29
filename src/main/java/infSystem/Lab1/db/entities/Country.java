package infSystem.Lab1.db.entities;

public enum Country {
    GERMANY("Германия"),
    SPAIN("Испания"),
    VATICAN("Ватикан"),
    SOUTH_KOREA("Северная Корея"),
    JAPAN("Япония");

    private final String translation;

    Country(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }

    public static Country fromTranslation(String translation) {
        if (translation == null || translation.trim().isEmpty()) {
            return null;
        }
        for (Country country : values()) {
            if (country.getTranslation().equals(translation.trim())) {
                return country;
            }
        }
        return null;
    }
}