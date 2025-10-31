package inf_system.Lab1.db.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;

@Entity
@Table(name = "persons")
@Getter
@Setter
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

    public void setName(@NotNull String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name не может быть пустым");
        }
        this.name = name;
    }

    public void setHeight(int height) {
        if (height < 1) {
            throw new IllegalArgumentException("Y не может быть меньше 1");
        }
        this.height = height;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("Person{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", coordinates=").append(coordinates);
        sb.append(", creationDate=").append(creationDate);
        sb.append(", eyeColor=").append(eyeColor);
        sb.append(", hairColor=").append(hairColor);
        sb.append(", location=").append(location);
        sb.append(", height=").append(height);
        sb.append(", birthday=").append(birthday);
        sb.append(", nationality=").append(nationality);
        sb.append('}');
        return sb.toString();
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
            case "name" -> person.name;
            case "coordinates.x" -> person.coordinates.getX();
            case "coordinates.y" -> person.coordinates.getY();
            case "location.x" -> person.location.getX();
            case "location.y" -> person.location.getY();
            case "location.z" -> person.location.getZ();
            case "height" -> person.height;
            case "eyecolor" -> person.eyeColor.getTranslation();
            case "haircolor" -> person.hairColor.getTranslation();
            case "nationality" -> person.nationality.getTranslation();
            case "creationdate" -> person.creationDate;
            case "birthday" -> person.birthday.toLocalDate();
            default -> person.id; // По умолчанию сортировка по ID
        };
    }
}