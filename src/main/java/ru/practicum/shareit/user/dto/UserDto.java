package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {

    private final int id;

    @NotBlank(message = "User name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^(.+)@(\\S+)$", message = "Must be formatted: mailName@domain")
    private String email;

}
