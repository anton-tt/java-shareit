package main.java.ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.user.dto.ResponseUserDto;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseBookingDto {

    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private ResponseItemDto item;
    private ResponseUserDto booker;

}