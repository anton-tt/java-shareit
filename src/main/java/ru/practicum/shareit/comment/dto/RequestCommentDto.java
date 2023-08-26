package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class RequestCommentDto {

    long id;
    @NotBlank
    private String text;

}