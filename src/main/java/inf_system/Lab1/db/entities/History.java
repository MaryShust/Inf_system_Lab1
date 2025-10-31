package inf_system.Lab1.db.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "history")
@Getter
@Setter
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    private boolean status;

    @Column(name = "author")
    private String author;

    @Column(name = "count_items")
    private int countItems;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append("Location{");
        sb.append("id=").append(id);
        sb.append(", status=").append(status);
        sb.append(", author=").append(author);
        sb.append(", countItems=").append(countItems);
        sb.append('}');
        return sb.toString();
    }
}