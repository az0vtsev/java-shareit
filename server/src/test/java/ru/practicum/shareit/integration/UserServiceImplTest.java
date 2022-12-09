package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserServiceImplTest {
    private final UserService service;

    private User user1;
    private User user2;
    private UserDto user1Dto;
    private UserDto user2Dto;

    @BeforeEach
    public void createData() {
        user1 = new User(1, "name1", "email1@mail.com");
        user2 = new User(2, "name2", "email2@mail.com");
        user1Dto = new UserDto(0, user1.getName(), user1.getEmail());
        user2Dto = new UserDto(0, user2.getName(),  user2.getEmail());
        service.createUser(user1Dto);
        service.createUser(user2Dto);
    }

    @Test
    public void shouldReturnUserById() {

        UserDto result1 = service.getUserById(user1.getId());

        assertThat(result1.getId()).isEqualTo(user1.getId());
        assertThat(result1.getName()).isEqualTo(user1.getName());
        assertThat(result1.getEmail()).isEqualTo(user1.getEmail());
    }

    @Test
    public void shouldReturnAll() {
        List<UserDto> result = service.getUsers();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(1).getId()).isEqualTo(user2.getId());
        assertThat(result.get(1).getName()).isEqualTo(user2.getName());
        assertThat(result.get(1).getEmail()).isEqualTo(user2.getEmail());
    }

}
