package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.utils.groups.Create;
import ru.practicum.shareit.utils.groups.Update;
import javax.validation.constraints.*;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class RequestUserDto {

    @NotBlank(groups = Create.class)
    @Size(max = 50, groups = {Create.class, Update.class})
    private String name;

    @NotEmpty(groups = Create.class)
    @Size(max = 100, groups = {Create.class, Update.class})
    @Email(groups = {Create.class, Update.class})
    private String email;

}