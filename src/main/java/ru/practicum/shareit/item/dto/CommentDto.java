package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {

    private final int id;

    @NotBlank(message = "Comment text is required")
    private String text;

    private Integer item;
    private Integer authorId;
    private String authorName;
    private LocalDateTime created;
}
