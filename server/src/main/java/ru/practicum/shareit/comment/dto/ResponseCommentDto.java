package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ResponseCommentDto {

    private long id;
    private String text;
    private LocalDateTime created;
    private String authorName;

}