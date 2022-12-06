package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.validator.Validator;

import java.util.List;
import java.util.stream.Collectors;



@Service
public class UserServiceImpl implements UserService {
    private final UserStorage storage;

    @Autowired
    public UserServiceImpl(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public UserDto getUserById(int id) {
        Validator.checkUserExistence(id, storage);
        return UserMapper.mapToUserDto(storage.findById(id).get());
    }

    @Override
    public List<UserDto> getUsers() {
        return  storage.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User createUser = UserMapper.mapToUser(userDto);
        return UserMapper.mapToUserDto(storage.save(createUser));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        UserDto oldUser = getUserById(userDto.getId());
        prepareToUpdate(userDto, oldUser);
        User updateUser = UserMapper.mapToUser(userDto);
        return UserMapper.mapToUserDto(storage.save(updateUser));
    }

    @Override
    public void deleteUser(int id) {
        Validator.checkUserExistence(id, storage);
        storage.deleteById(id);
    }

    private void prepareToUpdate(UserDto updateUser, UserDto oldUser) {
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
