package infs.lab.db.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

@Entity
@Table(name = "history")
@Getter
@Setter
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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

    @Column(name = "original_filename")
    private String originalFilename;

    @Column(name = "file_object_name")
    private String fileObjectName;

    @Column(name = "file_size")
    private Long fileSize;
}