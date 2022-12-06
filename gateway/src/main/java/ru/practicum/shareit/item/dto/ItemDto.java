package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private int id;
    private int owner;
    @NotBlank(message = "Item name is required")
    private String name;
    @NotBlank(message = "Item description is required")
    private String description;
    @NotNull(message = "Available is required")
    private Boolean available;
    private Integer requestId;
    private List<CommentDto> comments;

}