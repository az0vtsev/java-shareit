package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.NonNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private final int id;
    @NonNull
    private String name;
    @NonNull
    private String email;
}
