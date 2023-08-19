package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.booking.model.Status;
import javax.validation.constraints.Future;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
public class BookingDto {

    @Positive
    private long id;
    @NonNull
    @Future
    private LocalDateTime start;
    @NonNull
    @Future
    private LocalDateTime end;
    @NonNull
    private Status status;
    @NonNull
    private Long itemId;
    @NonNull
    private Long bookerId;

}