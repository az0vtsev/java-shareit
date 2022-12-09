package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private int id;
    private int owner;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
    private List<CommentDto> comments;

}
