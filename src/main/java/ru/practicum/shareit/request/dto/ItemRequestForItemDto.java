package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequestForItemDto {

    private long id;
    private String description;
    private LocalDateTime created;
    private long requestorId;

}