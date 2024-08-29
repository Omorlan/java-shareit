package ru.practicum.shareit.item.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.comment.dto.CommentAddDto;
import ru.practicum.shareit.item.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.comment.model.Comment;

@UtilityClass
public class CommentMapper {

    public static CommentResponseDto toDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static Comment toEntity(CommentAddDto commentAddDto) {
        return Comment.builder()
                .text(commentAddDto.getText())
                .build();
    }


}