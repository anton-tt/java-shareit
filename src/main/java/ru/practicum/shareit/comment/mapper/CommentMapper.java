package ru.practicum.shareit.comment.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.comment.dto.ResponseCommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;

@Slf4j
public class CommentMapper {

    private CommentMapper() {

    }

    public static Comment toComment(long itemId, RequestCommentDto commentDto, long userId, LocalDateTime currentMoment) {
        String commentDtoText = commentDto.getText();

        if (commentDtoText == null || commentDtoText.isEmpty()) {
            throw new ValidationException("В комментарии отсутствует текст! Операцию выполнить невозможно.");
        } else {
            return Comment.builder()
                    .text(commentDtoText)
                    .created(currentMoment)
                    .itemId(itemId)
                    .authorId(userId)
                    .build();
        }
    }

    public static ResponseCommentDto toResponseCommentDto(Comment comment, User author) {
        return ResponseCommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .authorName(author.getName())
                .build();
    }


}