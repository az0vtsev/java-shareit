package ru.practicum.shareit.user.service;

import java.util.List;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotUniqueEmailException;
import ru.practicum.shareit.exception.NotValidEmailException;
import ru.practicum.shareit.user.dto.UserDto;



public interface UserService {

    UserDto createUser(UserDto userDto) throws NotUniqueEmailException;

    UserDto updateUser(UserDto userDto) throws NotFoundException,
                                        NotValidEmailException, NotUniqueEmailException;

    void deleteUser(int id) throws NotFoundException;

    UserDto getUserById(int id) throws NotFoundException;

    List<UserDto> getUsers();
}
