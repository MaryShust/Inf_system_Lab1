package inf_system.Lab1.db.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "locations")
@Getter
@Setter
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "x")
    private double x;

    @Column(name = "y")
    private float y;

    @Column(name = "z")
    private double z;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append("Location{");
        sb.append("id=").append(id);
        sb.append(", x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", z=").append(z);
        sb.append('}');
        return sb.toString();
    }
}