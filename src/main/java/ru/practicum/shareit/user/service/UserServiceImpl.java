package ru.practicum.shareit.user.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotUniqueEmailException;
import ru.practicum.shareit.exception.NotValidEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.validator.Validator;



@Service
public class UserServiceImpl implements UserService {
    private final UserStorage storage;

    @Autowired
    public UserServiceImpl(@Qualifier("userInMemoryStorage") UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public UserDto getUserById(int id) throws NotFoundException {
        Validator.checkUserExistence(id, storage);
        return UserMapper.mapToUserDto(storage.getById(id));
    }

    @Override
    public List<UserDto> getUsers() {
        return  storage.getAll().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(UserDto userDto) throws NotUniqueEmailException {
        Validator.checkEmailIsUnique(userDto.getEmail(), storage);
        User createUser = UserMapper.mapToUser(userDto);
        return UserMapper.mapToUserDto(storage.create(createUser));
    }

    @Override
    public UserDto updateUser(UserDto userDto) throws NotFoundException, NotValidEmailException,
            NotUniqueEmailException {
        Validator.checkUserExistence(userDto.getId(), storage);
        UserDto oldUser = getUserById(userDto.getId());
        prepareToUpdate(userDto, oldUser);
        User updateUser = UserMapper.mapToUser(userDto);
        return UserMapper.mapToUserDto(storage.update(updateUser));
    }

    @Override
    public void deleteUser(int id) throws NotFoundException {
        Validator.checkUserExistence(id, storage);
        storage.delete(id);
    }

    private void prepareToUpdate(UserDto updateUser, UserDto oldUser) throws NotValidEmailException,
            NotUniqueEmailException {
        if (updateUser.getEmail() != null) {
            Validator.checkEmailIsValid(updateUser.getEmail());
            Validator.checkEmailIsUnique(updateUser.getEmail(), storage);
        } else {
            updateUser.setEmail(oldUser.getEmail());
        }
        if (updateUser.getName() == null) {
            updateUser.setName(oldUser.getName());
        }
    }

}
