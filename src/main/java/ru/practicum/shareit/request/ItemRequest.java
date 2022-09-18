package ru.practicum.shareit.request;

import lombok.Data;

import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    private int id;
    private String description;
    private int requestor;
    private LocalDate created;
}
