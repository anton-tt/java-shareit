package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Status;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemBookingDto {

    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private long itemId;
    private long bookerId;

}