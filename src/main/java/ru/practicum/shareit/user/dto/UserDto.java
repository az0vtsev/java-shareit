package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private int id;

    @NotBlank(message = "User name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^(.+)@(\\S+)$", message = "Must be formatted: mailName@domain")
    private String email;

}
