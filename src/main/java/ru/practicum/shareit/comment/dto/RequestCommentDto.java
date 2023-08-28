package ru.practicum.shareit.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Data
public class RequestCommentDto {

    private long id;

    @NotBlank
    @Size(max = 500)
    private String text;

}