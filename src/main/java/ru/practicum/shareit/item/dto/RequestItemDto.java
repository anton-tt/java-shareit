package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.utils.groups.Create;
import ru.practicum.shareit.utils.groups.Update;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Data
public class RequestItemDto {

    @NotBlank(groups = Create.class)
    @Size(max = 50, groups = {Create.class, Update.class})
    private String name;

    @NotBlank(groups = Create.class)
    @Size(max = 500, groups = {Create.class, Update.class})
    private String description;

    @NotNull(groups = Create.class)
    private Boolean available;

}