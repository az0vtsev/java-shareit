package ru.practicum.shareit.unit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemRequestTest {

    private User user1;
    private User user2;
    private ItemRequest request1;
    private ItemRequest request2;
    private ItemRequestDto requestDto1;
    private ItemRequestInfoDto requestInfoDto1;
    private ItemRequestInfoDto requestInfoDto2;

    @Mock
    private ItemRequestService service;
    @InjectMocks
    private ItemRequestController controller;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private MockMvc mvc;

    public ItemRequestTest() {
    }

    @BeforeEach
    public void createData() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        user1 = new User(1, "name1", "email1@mail.com");
        user2 = new User(2,"name2", "email2@mail.com");
        request1 = new ItemRequest(1, "request_item1", user2.getId(), LocalDateTime.now());
        request2 = new ItemRequest(2, "request_item2", user1.getId(), LocalDateTime.now());
        requestDto1 = ItemRequestMapper.mapToItemRequestDto(request1);
        requestInfoDto1 = ItemRequestMapper.mapToItemRequestInfoDto(request1);
        requestInfoDto2 = ItemRequestMapper.mapToItemRequestInfoDto(request2);
    }

    @Test
    public void shouldSaveItemRequest() throws Exception {

        when(service.createItemRequest(anyInt(), any(ItemRequestDto.class)))
                .thenReturn(requestInfoDto1);
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request1.getId())))
                .andExpect(jsonPath("$.description", is(request1.getDescription())))
                .andExpect(jsonPath("$.requestor", is(request1.getRequestor())));
        verify(service, times(1)).createItemRequest(user2.getId(), requestDto1);
    }

    @Test
    public void shouldReturnUserItemRequest() throws Exception {
        List<ItemRequestInfoDto> requests = List.of(requestInfoDto1);
        when(service.getUserItemRequests(anyInt())).thenReturn(requests);
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", user2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()", is(1)))
                .andExpect(jsonPath("[0].id", is(request1.getId())))
                .andExpect(jsonPath("[0].description", is(request1.getDescription())))
                .andExpect(jsonPath("[0].requestor", is(request1.getRequestor())));
        verify(service, times(1)).getUserItemRequests(user2.getId());
    }

    @Test
    public void shouldReturnAllItemRequest() throws Exception {
        List<ItemRequestInfoDto> requests = List.of(requestInfoDto2);
        when(service.getItemRequests(anyInt(), anyInt(), anyInt())).thenReturn(requests);
        mvc.perform(get("/requests/all?from={from}&size={size}", 0, 10)
                        .header("X-Sharer-User-Id", user2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()", is(1)))
                .andExpect(jsonPath("[0].id", is(request2.getId())))
                .andExpect(jsonPath("[0].description", is(request2.getDescription())))
                .andExpect(jsonPath("[0].requestor", is(user1.getId())));
        verify(service, times(1)).getItemRequests(0, 10, user2.getId());
    }

    @Test
    public void shouldReturnItemRequestById() throws Exception {
        when(service.getItemRequest(anyInt(), anyInt())).thenReturn(requestInfoDto1);
        mvc.perform(get("/requests/{requestId}", 1)
                        .header("X-Sharer-User-Id", user1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request1.getId())))
                .andExpect(jsonPath("$.description", is(request1.getDescription())))
                .andExpect(jsonPath("$.requestor", is(user2.getId())));
        verify(service, times(1)).getItemRequest(user1.getId(), request1.getId());
    }
}
