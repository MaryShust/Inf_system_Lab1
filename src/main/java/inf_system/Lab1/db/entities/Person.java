package inf_system.Lab1.db.entities;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;

@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "coordinates_id")
    @NotNull
    private Coordinates coordinates;

    @Column(name = "creation_date")
    @NotNull
    private LocalDate creationDate;

    @Column(name = "eye_color")
    @Enumerated(EnumType.STRING)
    private Color eyeColor;

    @Column(name = "hair_color")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Color hairColor;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "height")
    private int height;

    @Column(name = "birthday")
    @NotNull
    private LocalDateTime birthday;

    @Column(name = "nationality")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Country nationality;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name не может быть пустым");
        }
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(@NotNull Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(@NotNull Color hairColor) {
        this.hairColor = hairColor;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (height < 1) {
            throw new IllegalArgumentException("Y не может быть меньше 1");
        }
        this.height = height;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(@NotNull Country nationality) {
        this.nationality = nationality;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
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


    public static Comparator<Person> getComparator(String sortField, String sortOrder) {
        Comparator<Person> comparator = (p1, p2) -> {
            Object val1 = getFieldValue(p1, sortField);
            Object val2 = getFieldValue(p2, sortField);

            if (val1 instanceof Comparable && val2 instanceof Comparable) {
                return ((Comparable) val1).compareTo(val2);
            }
            return 0;
        };

        if ("desc".equalsIgnoreCase(sortOrder)) {
            comparator = comparator.reversed();
        }

        return Comparator.nullsFirst(comparator);
    }

    public static Object getFieldValue(Person person, String field) {
        return switch (field.toLowerCase()) {
            case "name" -> person.getName();
            case "coordinates.x" -> person.getCoordinates().getX();
            case "coordinates.y" -> person.getCoordinates().getY();
            case "location.x" -> person.getLocation().getX();
            case "location.y" -> person.getLocation().getY();
            case "location.z" -> person.getLocation().getZ();
            case "height" -> person.getHeight();
            case "eyecolor" -> person.getEyeColor().getTranslation();
            case "haircolor" -> person.getHairColor().getTranslation();
            case "nationality" -> person.getNationality().getTranslation();
            case "creationdate" -> person.getCreationDate();
            case "birthday" -> person.getBirthday().toLocalDate();
            default -> person.getId(); // По умолчанию сортировка по ID
        };
    }
}