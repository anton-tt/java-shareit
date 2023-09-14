package main.java.ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.ItemBookingDto;
import ru.practicum.shareit.comment.dto.ResponseCommentDto;
import ru.practicum.shareit.request.dto.ItemRequestForItemDto;

import java.util.List;

@Builder
@Data
public class FullResponseItemDto {

    private long id;
    private String name;
    private String description;
    private Boolean available;
    private ItemBookingDto lastBooking;
    private ItemBookingDto nextBooking;
    private ItemRequestForItemDto request;
    private List<ResponseCommentDto> comments;

}