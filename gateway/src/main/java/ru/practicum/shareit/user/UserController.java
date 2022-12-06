package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getUser(@Positive @PathVariable int id) {
        log.info("GET /users/{} request", id);
        return userClient.getUser(id);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("GET /users request");
        return userClient.getUsers();
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto) {
        log.info("POST /users request");
        return userClient.createUser(userDto);
    }

    @PatchMapping(value = "/{userId}")
    public ResponseEntity<Object> update(@Positive @PathVariable int userId,
                       @RequestBody UserDto userDto) {
        log.info("PATCH /users request");
        return userClient.updateUser(userId, userDto);
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Object> delete(@Positive @PathVariable int userId) {
        log.info("DELETE /users request");
        return userClient.deleteUser(userId);
    }
}
