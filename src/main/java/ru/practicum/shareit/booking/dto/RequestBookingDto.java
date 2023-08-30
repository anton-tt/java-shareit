package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.valid.StartBeforeEndDateValid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@StartBeforeEndDateValid
public class RequestBookingDto {

    @Positive
    private long itemId;

    @Future
    private LocalDateTime start;

    private LocalDateTime end;

}