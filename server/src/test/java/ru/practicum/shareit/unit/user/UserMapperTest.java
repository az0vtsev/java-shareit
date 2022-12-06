package ru.practicum.shareit.unit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMapperTest {

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void createTestData() {
        user = new User(1, "name", "email@mail.com");
        userDto = new UserDto(1,"name", "email@mail.com");
    }

    @Test
    public void shouldMapToUserDtoTest() {
        UserDto result = UserMapper.mapToUserDto(user);
        assertThat(result).isEqualTo(userDto);
    }

    @Test
    public void shouldMapToUserTest() {
        User result = UserMapper.mapToUser(userDto);
        assertThat(result).isEqualTo(user);
    }
}
