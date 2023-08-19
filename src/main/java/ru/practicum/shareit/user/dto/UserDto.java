package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Builder
@Data
public class UserDto {

    @Positive
    private long id;
    @NotBlank
    private String name;
    @NonNull
    @Email
    private String email;

}