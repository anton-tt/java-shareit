package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Builder
@Data
public class CommentDto {

    @Positive
    private long id;
    @NotBlank
    private String text;
    @NotNull
    private LocalDateTime created;
    @NotNull
    private Long itemId;
    private String authorName;

}