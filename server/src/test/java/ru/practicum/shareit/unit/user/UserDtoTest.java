package ru.practicum.shareit.unit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
public class UserDtoTest {
    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void testUserDtoToJson() throws IOException {
        UserDto userDto = new UserDto(1, "name", "mail@mail.com");
        JsonContent<UserDto> result = json.write(userDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("mail@mail.com");

    }

    @Test
    void testJsonToUserDto() throws IOException {
        UserDto userDto = new UserDto(1, "name", "mail@mail.com");
        String stringJson = "{\n    \"id\": 1,\n        \"name\": \"name\",\n    \"email\": \"mail@mail.com\"\n}";
        UserDto userDtoJson = json.parseObject(stringJson);
        assertThat(userDtoJson).isEqualTo(userDto);

    }
}
