package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestItemDto {
    private final int id;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
}
