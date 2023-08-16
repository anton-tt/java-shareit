package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
public class Item {

    @Positive
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private boolean available;
    private User owner;
    private ItemRequest request;

}