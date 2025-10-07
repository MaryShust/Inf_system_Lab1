package inf_system.Lab1.db.entities;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "coordinates")
@Getter
public class Coordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "x")
    private int x;

    @Column(name = "y")
    private int y;

    public void setX(int x) {
        if (x > 674) {
            throw new IllegalArgumentException("X не может быть больше 674");
        }
        this.x = x;
    }

    public void setY(int y) {
        if (y < -554) {
            throw new IllegalArgumentException("Y не может быть меньше -554");
        }
        this.y = y;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append("Coordinates{");
        sb.append("id=").append(id);
        sb.append(", x=").append(x);
        sb.append(", y=").append(y);
        sb.append('}');
        return sb.toString();
    }
}