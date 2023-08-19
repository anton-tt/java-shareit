package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Builder
@Data
public class User {


    private long id;
    @NotBlank
    private String name;
    @NotNull @Email
    private String email;

}