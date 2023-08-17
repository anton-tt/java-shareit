package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
public class Booking {

    @Positive
    private long id;
    @NotNull
    private LocalDateTime start;
    @NotNull @Future
    private LocalDateTime end;
    private Item item;
    private User booker;
    private Status status;

}
