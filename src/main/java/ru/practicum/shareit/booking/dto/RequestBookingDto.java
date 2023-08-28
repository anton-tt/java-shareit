package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class RequestBookingDto {

    @Positive
    private long itemId;

    @NotNull
    @Future
    private LocalDateTime start;

    @NotNull
    @Future
    private LocalDateTime end;

}