package ru.practicum.shareit.user.controller;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotUniqueEmailException;
import ru.practicum.shareit.exception.NotValidEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    public UserDto getUser(@PathVariable int id) throws NotFoundException {
        log.info("GET /users/{} request received", id);
        UserDto userDto = service.getUserById(id);
        log.info("GET /users/{} request done", id);
        return userDto;
    }

    @GetMapping
    public List<UserDto> getUsers() {
        log.info("GET /users request received");
        List<UserDto> users = service.getUsers();
        log.info("GET /users request done");
        return users;
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) throws NotUniqueEmailException {
        log.info("POST /users request received");
        UserDto createUser = service.createUser(userDto);
        log.info("POST /users request done");
        return createUser;
    }

    @PatchMapping(value = "/{userId}")
    public UserDto update(@PathVariable int userId,
                       @RequestBody UserDto userDto) throws NotFoundException, NotValidEmailException,
            NotUniqueEmailException {
        log.info("PATCH /users request received");
        UserDto updateUser = service.updateUser(new UserDto(userId, userDto.getName(), userDto.getEmail()));
        log.info("PATCH /users request done");
        return updateUser;
    }

    @DeleteMapping(value = "/{userId}")
    public void delete(@PathVariable int userId) throws NotFoundException {
        log.info("DELETE /users request received");
        service.deleteUser(userId);
        log.info("DELETE /users request done");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Data isn't valid",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Entity not found",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler(NotUniqueEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleNotUniqueEmailException(final NotUniqueEmailException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Email already exist",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler(NotValidEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNotValidEmailException(final NotValidEmailException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Email isn't valid",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(final RuntimeException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Runtime Exception",
                "errorMessage", e.getMessage()
        );
    }

}
