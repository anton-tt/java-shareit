package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Builder
@Data
public class UserDto {

    @Positive
    private long id;
    @NotBlank
    private String name;
    @NotNull @Email
    private String email;

}