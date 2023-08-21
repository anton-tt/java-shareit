package ru.practicum.shareit.item.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
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
    private Long ownerId;
    @Column(name = "request_id")
    private Long requestId;

}