package infs.lab.db.entities;

import infs.lab.controller.exception.ValidationException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

@Entity
@Table(name = "coordinates")
@Getter
@ToString
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
            throw new ValidationException("X не может быть больше 674");
        }
        this.x = x;
    }

    public void setY(int y) {
        if (y < -554) {
            throw new ValidationException("Y не может быть меньше -554");
        }
        this.y = y;
    }
}