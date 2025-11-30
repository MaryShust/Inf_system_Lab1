package infs.lab.db.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "history")
@Getter
@Setter
@ToString
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
}