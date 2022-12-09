package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private int id;

    @NotBlank(message = "Comment text is required")
    private String text;

    private Integer item;
    private Integer authorId;
    private String authorName;
    private LocalDateTime created;
}