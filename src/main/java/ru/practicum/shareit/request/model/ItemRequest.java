package ru.practicum.shareit.request.model;

import ru.practicum.shareit.user.model.User;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class ItemRequest {
    @Positive
    private long id;
    @NotBlank
    private String description;
    private User requestor;
    private LocalDateTime created;

}
