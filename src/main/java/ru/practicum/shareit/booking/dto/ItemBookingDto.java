package ru.practicum.shareit.booking.dto;

import com.sun.istack.NotNull;
import lombok.*;
import ru.practicum.shareit.booking.model.Status;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemBookingDto {

    @NotNull
    private long id;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    @NotNull
    private Status status;
    @NotNull
    private long itemId;
    @NotNull
    private long bookerId;

}