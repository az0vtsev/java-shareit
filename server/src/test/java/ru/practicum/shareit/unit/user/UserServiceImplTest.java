package ru.practicum.shareit.unit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotUniqueEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith({MockitoExtension.class})
public class UserServiceImplTest {

    private User user1;
    private User user2;
    private UserDto userDto1;
    private UserDto userDto2;

    private UserService service;

    @Mock
    private UserStorage storage;

    @BeforeEach
    public void createData() {
        service = new UserServiceImpl(storage);
        user1 = new User(1, "name1", "email1@mail.com");
        user2 = new User(2,"name2", "email2@mail.com");
        userDto1 = new UserDto(user1.getId(), user1.getName(), user1.getEmail());
        userDto2 = new UserDto(user2.getId(), user2.getName(), user2.getEmail());
    }

    @Test
    public void shouldSaveAndReturnUserDto() {
        Mockito.when(storage.save(any(User.class))).thenReturn(user1);
        UserDto userDto = service.createUser(userDto1);
        assertThat(userDto).isEqualTo(userDto1);
        Mockito.verify(storage, Mockito.times(1)).save(user1);
    }

    @Test
    public void shouldUpdateNameAndReturnUserDto() {
        User updateUser = new User(1, "update_name1", "email1@mail.com");
        UserDto updateUserDto = new UserDto(1, "update_name1", null);
        Optional<User> result = Optional.of(user1);
        Mockito.when(storage.save(any(User.class))).thenReturn(updateUser);
        Mockito.when(storage.findById(anyInt())).thenReturn(result);
        Mockito.when(storage.existsById(anyInt())).thenReturn(true);
        UserDto updatedUserDto = service.updateUser(updateUserDto);
        assertThat(updatedUserDto).isEqualTo(UserMapper.mapToUserDto(updateUser));
        Mockito.verify(storage, Mockito.times(1)).findById(updateUser.getId());
        Mockito.verify(storage, Mockito.times(1)).existsById(updateUser.getId());
        Mockito.verify(storage, Mockito.times(1)).save(updateUser);
    }

    @Test
    public void shouldUpdateEmailAndReturnUserDto() {
        User updateUser = new User(1, "name1", "updatemail1@mail.com");
        UserDto updateUserDto = new UserDto(1, null, "updatemail1@mail.com");
        Optional<User> result = Optional.of(user1);
        Mockito.when(storage.findAll()).thenReturn(List.of(user1, user2));
        Mockito.when(storage.save(any(User.class))).thenReturn(updateUser);
        Mockito.when(storage.findById(anyInt())).thenReturn(result);
        Mockito.when(storage.existsById(anyInt())).thenReturn(true);
        UserDto updatedUserDto = service.updateUser(updateUserDto);
        assertThat(updatedUserDto).isEqualTo(UserMapper.mapToUserDto(updateUser));
        Mockito.verify(storage, Mockito.times(1)).findById(updateUser.getId());
        Mockito.verify(storage, Mockito.times(1)).existsById(updateUser.getId());
        Mockito.verify(storage, Mockito.times(1)).findAll();
        Mockito.verify(storage, Mockito.times(1)).save(updateUser);
    }

    @Test
    public void shouldReturnUserDto() {
        Optional<User> result = Optional.of(user1);
        Mockito.when(storage.existsById(anyInt())).thenReturn(true);
        Mockito.when(storage.findById(anyInt())).thenReturn(result);
        UserDto userDto = service.getUserById(user1.getId());
        assertThat(userDto).isEqualTo(UserMapper.mapToUserDto(user1));
        Mockito.verify(storage, Mockito.times(1)).existsById(user1.getId());
        Mockito.verify(storage, Mockito.times(1)).findById(user1.getId());
    }

    @Test
    public void shouldReturnUsersDto() {
        Mockito.when(storage.findAll()).thenReturn(List.of(user1, user2));
        List<UserDto> usersDto = service.getUsers();
        assertThat(usersDto.size()).isEqualTo(2);
        assertThat(usersDto.get(1)).isEqualTo(userDto2);
        Mockito.verify(storage, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldThrowNotFoundExceptionWhileUpdateWrongId() {
        Mockito.when(storage.existsById(anyInt())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> service.updateUser(userDto1));
        Mockito.verify(storage, Mockito.times(1)).existsById(userDto1.getId());
    }

    @Test
    public void shouldThrowNotUniqueEmailExceptionWhileUpdateWrongEmail() {
        Mockito.when(storage.existsById(anyInt())).thenReturn(true);
        Mockito.when(storage.findById(anyInt())).thenReturn(Optional.of(user1));
        Mockito.when(storage.findAll()).thenReturn(List.of(user1, user2));
        userDto1.setEmail(user2.getEmail());
        assertThrows(NotUniqueEmailException.class, () -> service.updateUser(userDto1));
        Mockito.verify(storage, Mockito.times(1)).existsById(userDto1.getId());
        Mockito.verify(storage, Mockito.times(1)).findById(userDto1.getId());
    }
}
