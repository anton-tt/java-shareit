package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Data
@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="name", nullable = false)
    @NotBlank
    private String name;
    @Column(name="email", nullable=false, unique=true)
    @Email
    private String email;

}