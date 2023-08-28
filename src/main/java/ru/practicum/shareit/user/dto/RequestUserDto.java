package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.utils.groups.Create;
import ru.practicum.shareit.utils.groups.Update;
import javax.validation.constraints.*;

@NoArgsConstructor
@Data
public class RequestUserDto {

    @NotBlank(groups = Create.class)
    @Size(max = 50, groups = {Create.class, Update.class})
    private String name;

    @NotNull(groups = Create.class)
    @NotEmpty(groups = Create.class)
    @Size(max = 100, groups = {Create.class, Update.class})
    @Email(groups = {Create.class, Update.class})
    private String email;

}