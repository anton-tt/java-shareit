package ru.practicum.shareit.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.comment.dto.ResponseCommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public Comment toComment(Item item, RequestCommentDto commentDto, User user, LocalDateTime currentMoment) {
        return Comment.builder()
                .text(commentDto.getText())
                .created(currentMoment)
                .item(item)
                .author(user)
                .build();
    }

    public ResponseCommentDto toResponseCommentDto(Comment comment) {
        return ResponseCommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .authorName(comment.getAuthor().getName())
                .build();
    }

}