package main.java.ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.dto.ResponseUserDto;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseItemRequestDto {

    private long id;
    private String description;
    private LocalDateTime created;
    private ResponseUserDto requestor;

}