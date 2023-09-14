package main.java.ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ResponseItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class FullResponseItemRequestDto {

    private long id;
    private String description;
    private LocalDateTime created;
    private List<ResponseItemDto> items;

}