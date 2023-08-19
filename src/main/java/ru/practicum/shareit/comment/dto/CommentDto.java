package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Builder
@Data
public class CommentDto {

    @Positive
    private long id;
    @NonNull
    @NotBlank
    private String text;
    @NonNull
    private LocalDateTime created;
    @NonNull
    private Long itemId;
    @NonNull
    private Long authorId;

}