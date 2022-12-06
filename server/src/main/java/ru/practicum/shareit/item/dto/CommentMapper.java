package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.dto.UserDto;

public class CommentMapper {

    public static CommentDto mapToCommentDto(Comment comment, ItemDto item, UserDto user) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                item.getId(),
                user.getId(),
                user.getName(),
                comment.getCreated()
        );
    }

    public static Comment mapToComment(CommentDto commentDto) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                commentDto.getItem(),
                commentDto.getAuthorId(),
                commentDto.getCreated()
        );
    }
}
