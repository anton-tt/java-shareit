package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@Entity
@Table(name = "items", schema = "public")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;
    @Column(name = "description", nullable = false)
    @NotBlank
    private String description;
    @Column(name = "is_available", nullable = false)
    private boolean available;
    @Column(name = "owner_id", nullable = false)
    private long ownerId;
    @Column(name = "request_id")
    private long requestId;

}