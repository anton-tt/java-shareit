package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class RequestBookingDto {

    private long itemId;
    private LocalDateTime start;
    private LocalDateTime end;

}