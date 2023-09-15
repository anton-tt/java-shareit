package ru.practicum.shareit.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class RequestCommentDto {

    private long id;
    private String text;

}