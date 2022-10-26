package ru.practicum.shareit.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotUniqueEmailException;
import ru.practicum.shareit.exception.NotValidEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

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

}
