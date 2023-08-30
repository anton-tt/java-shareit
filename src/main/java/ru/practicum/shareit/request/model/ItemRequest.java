package ru.practicum.shareit.request.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "requests", schema = "public")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "description", nullable = false)
    @NotBlank
    private String description;
    @Column(name = "created_time", nullable = false)
    private LocalDateTime created;
    @Column(name = "requestor_id", nullable = false)
    private long requestorId;

}