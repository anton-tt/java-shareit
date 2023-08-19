package ru.practicum.shareit.comment.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments", schema = "public")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "text", nullable = false)
    @NotBlank
    private String text;
    @Column(name = "created_time", nullable = false)
    private LocalDateTime created;
    @Column(name = "item_id", nullable = false)
    private long itemId;
    @Column(name = "author_id", nullable = false)
    private long authorId;

}