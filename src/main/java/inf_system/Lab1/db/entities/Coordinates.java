package inf_system.Lab1.db.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "coordinates")
public class Coordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "x")
    private int x;

    @Column(name = "y")
    private int y;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        if (x > 674) {
            throw new IllegalArgumentException("X не может быть больше 674");
        }
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(int y) {
        if (y < -554) {
            throw new IllegalArgumentException("Y не может быть меньше -554");
        }
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}