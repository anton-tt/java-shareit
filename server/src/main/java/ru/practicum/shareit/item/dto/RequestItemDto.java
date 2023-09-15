package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class RequestItemDto {

    private String name;
    private String description;
    private Boolean available;
    private long requestId;

}