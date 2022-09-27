package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class ItemDto {

    private final int id;
    private final int owner;
    @NotBlank(message = "Item name is required")
    private String name;
    @NotBlank(message = "Item name is required")
    private String description;
    @NotNull(message = "Item name is required")
    private Boolean available;
    private final ItemRequest request;

}
