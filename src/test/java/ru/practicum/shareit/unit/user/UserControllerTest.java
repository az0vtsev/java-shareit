package ru.practicum.shareit.unit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private User user1;
    private UserDto userDto1;
    private UserDto userDto2;
    @Mock
    private UserService service;
    @InjectMocks
    private UserController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    @BeforeEach
    public void createData() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        user1 = new User(1, "name1", "email1@mail.com");
        User user2 = new User(2, "name2", "email2@mail.com");
        userDto1 = UserMapper.mapToUserDto(user1);
        userDto2 = UserMapper.mapToUserDto(user2);
    }

    @Test
    public void shouldSaveUser() throws Exception {
        when(service.createUser(any(UserDto.class))).thenReturn(userDto1);
        mvc.perform(post("/users")
                .content(mapper.writeValueAsString(userDto1))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto1.getId())))
                .andExpect(jsonPath("$.name", is(userDto1.getName())))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail())));
    }

    @Test
    public void shouldUpdateUserAndReturn() throws Exception {
        User updateUser = new User(user1.getId(), "udateName", user1.getEmail());
        UserDto updateUserDto = UserMapper.mapToUserDto(updateUser);
        when(service.updateUser(any(UserDto.class))).thenReturn(updateUserDto);
        mvc.perform(patch("/users/{userId}",1)
                        .content(mapper.writeValueAsString(updateUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updateUserDto.getId())))
                .andExpect(jsonPath("$.name", is(updateUserDto.getName())))
                .andExpect(jsonPath("$.email", is(updateUserDto.getEmail())));
        verify(service, times(1)).updateUser(updateUserDto);
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        mvc.perform(delete("/users/{userId}",1))
                .andExpect(status().isOk());
       verify(service, times(1)).deleteUser(1);
    }

    @Test
    public void shouldReturnUsers() throws Exception {
        List<UserDto> userDtos = List.of(userDto1, userDto2);
        when(service.getUsers()).thenReturn(userDtos);
        mvc.perform((get("/users"))
                        .content(mapper.writeValueAsString(userDtos))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()", is(2)))
                .andExpect(jsonPath("[0].id", is(userDto1.getId())))
                .andExpect(jsonPath("[0].name", is(userDto1.getName())))
                .andExpect(jsonPath("[0].email", is(userDto1.getEmail())))
                .andExpect(jsonPath("[1].id", is(userDto2.getId())))
                .andExpect(jsonPath("[1].name", is(userDto2.getName())))
                .andExpect(jsonPath("[1].email", is(userDto2.getEmail())));
    }

    @Test
    public void shouldReturnUserById() throws Exception {
        when(service.getUserById(anyInt())).thenReturn(userDto1);
        mvc.perform((get("/users/{id}", 1))
                        .content(mapper.writeValueAsString(userDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto1.getId())))
                .andExpect(jsonPath("$.name", is(userDto1.getName())))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail())));
        verify(service, times(1)).getUserById(1);
    }
}
