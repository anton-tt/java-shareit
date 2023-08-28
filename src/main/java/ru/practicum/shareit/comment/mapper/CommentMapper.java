package ru.practicum.shareit.comment.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.comment.dto.ResponseCommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;

@UtilityClass
@Slf4j
public class CommentMapper {

    public Comment toComment(Item item, RequestCommentDto commentDto, User user, LocalDateTime currentMoment) {
        return Comment.builder()
                .text(commentDto.getText())
                .created(currentMoment)
                .item(item)
                .author(user)
                .build();
    }

    public ResponseCommentDto toResponseCommentDto(Comment comment, User author) {
        return ResponseCommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .authorName(author.getName())
                .build();
    }

}