package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private final int id;
    private int owner;
    @NotBlank(message = "Item name is required")
    private String name;
    @NotBlank(message = "Item name is required")
    private String description;
    @NotNull(message = "Available is required")
    private Boolean available;
    private Integer request;
    private List<CommentDto> comments;

}
