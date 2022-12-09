package ru.practicum.shareit.unit.item;


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
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    private User user1;
    private User user2;
    private UserDto userDto1;

    private Item item1;
    private ItemDto itemDto1;
    private ItemInfoDto itemInfoDto1;
    private ItemDto itemDto2;
    private ItemInfoDto itemInfoDto3;

    @Mock
    private ItemService service;

    @InjectMocks
    private ItemController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    @BeforeEach
    public void createData() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        user1 = new User(1, "name1", "email1@mail.com");
        user2 = new User(2,"name2", "email2@mail.com");
        userDto1 = UserMapper.mapToUserDto(user1);
        item1 = new Item(1, user1.getId(), "itemName1", "itemDescription1",
                true, null);
        itemDto1 = ItemMapper.mapToItemDto(item1);
        itemInfoDto1 = ItemMapper.mapToItemInfoDto(item1, userDto1, null, null);
        Item item2 = new Item(2, user2.getId(), "itemName2", "itemDescription2",
                true, null);
        itemDto2 = ItemMapper.mapToItemDto(item2);
        Item item3 = new Item(3, user1.getId(), "itemName3", "itemDescriotion3",
                true, null);
        itemInfoDto3 = ItemMapper.mapToItemInfoDto(item3, userDto1, null, null);
    }

    @Test
    public void shouldSaveItem() throws Exception {
        ItemDto createItemDto = new ItemDto(0, user1.getId(),
                itemDto1.getName(), itemDto1.getDescription(),
                itemDto1.getAvailable(), itemDto1.getRequestId(), new ArrayList<>());
        when(service.createItem(any(ItemDto.class))).thenReturn(itemDto1);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(createItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto1.getId())))
                .andExpect(jsonPath("$.owner", is(itemDto1.getOwner())))
                .andExpect(jsonPath("$.name", is(itemDto1.getName())))
                .andExpect(jsonPath("$.description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto1.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto1.getRequestId())));
        verify(service, times(1)).createItem(createItemDto);
    }

    @Test
    public void shouldUpdateItemAndReturn() throws Exception {
        Item updateItem = new Item(item1.getId(), item1.getOwner(), "updateName",
                item1.getDescription(), item1.getAvailable(), item1.getRequestId());
        ItemDto updateItemDto = ItemMapper.mapToItemDto(updateItem);
        when(service.updateItem(any(ItemDto.class))).thenReturn(updateItemDto);
        mvc.perform(patch("/items/{itemId}",item1.getId())
                        .content(mapper.writeValueAsString(updateItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", item1.getOwner()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updateItemDto.getId())))
                .andExpect(jsonPath("$.owner", is(updateItemDto.getOwner())))
                .andExpect(jsonPath("$.name", is(updateItemDto.getName())))
                .andExpect(jsonPath("$.description", is(updateItemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(updateItemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(updateItemDto.getRequestId())));
        verify(service, times(1)).updateItem(updateItemDto);
    }

    @Test
    public void shouldDeleteItem() throws Exception {
        mvc.perform(delete("/items/{itemId}",item1.getId())
                        .header("X-Sharer-User-Id", item1.getOwner()))
                .andExpect(status().isOk());
        verify(service, times(1)).deleteItem(item1.getId(), item1.getOwner());
    }

    @Test
    public void shouldReturnItemById() throws Exception {
        when(service.getItemById(anyInt(), anyInt())).thenReturn(itemInfoDto1);
        mvc.perform((get("/items/{itemId}", item1.getId()))
                        .content(mapper.writeValueAsString(itemInfoDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", item1.getOwner()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemInfoDto1.getId())))
                .andExpect(jsonPath("$.owner.id", is(itemInfoDto1.getOwner().getId())))
                .andExpect(jsonPath("$.name", is(itemInfoDto1.getName())))
                .andExpect(jsonPath("$.description", is(itemInfoDto1.getDescription())))
                .andExpect(jsonPath("$.available", is(itemInfoDto1.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemInfoDto1.getRequestId())));
        verify(service, times(1)).getItemById(item1.getId(), item1.getOwner());
    }

    @Test
    public void shouldReturnItems() throws Exception {
        List<ItemInfoDto> itemsDto = List.of(itemInfoDto1, itemInfoDto3);
        when(service.getItemsByOwner(anyInt(), anyInt(), anyInt())).thenReturn(itemsDto);
        mvc.perform((get("/items/?from={from}&size={size}",0, 10))
                        .content(mapper.writeValueAsString(itemsDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()", is(2)))
                .andExpect(jsonPath("[0].id", is(itemInfoDto1.getId())))
                .andExpect(jsonPath("[0].owner.id", is(itemInfoDto1.getOwner().getId())))
                .andExpect(jsonPath("[0].name", is(itemInfoDto1.getName())))
                .andExpect(jsonPath("[0].description", is(itemInfoDto1.getDescription())))
                .andExpect(jsonPath("[0].available", is(itemInfoDto1.getAvailable())))
                .andExpect(jsonPath("[0].requestId", is(itemInfoDto1.getRequestId())))
                .andExpect(jsonPath("[0].owner.id", is(userDto1.getId())))
                .andExpect(jsonPath("[1].owner.id", is(userDto1.getId())));
        verify(service, times(1)).getItemsByOwner(user1.getId(), 0, 10);
    }

    @Test
    public void shouldReturnItemsBySearch() throws Exception {
        List<ItemDto> itemsDto = List.of(itemDto2);
        when(service.getItemsBySearch(anyString(), anyInt(), anyInt())).thenReturn(itemsDto);
        mvc.perform((get("/items/search?text={text}&from={from}&size={size}", "itemName2", 0, 10))
                        .content(mapper.writeValueAsString(itemsDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()", is(1)))
                .andExpect(jsonPath("[0].id", is(itemDto2.getId())))
                .andExpect(jsonPath("[0].owner", is(itemDto2.getOwner())))
                .andExpect(jsonPath("[0].name", is(itemDto2.getName())))
                .andExpect(jsonPath("[0].description", is(itemDto2.getDescription())))
                .andExpect(jsonPath("[0].available", is(itemDto2.getAvailable())))
                .andExpect(jsonPath("[0].requestId", is(itemDto2.getRequestId())));
        verify(service, times(1)).getItemsBySearch(itemDto2.getName(), 0, 10);
    }

    @Test
    public void shouldAddCommentAndReturnComment() throws Exception {

        CommentDto commentDto = new CommentDto(1, "comment_text", item1.getId(), user2.getId(),
                user2.getName(), null);;
        when(service.addComment(anyInt(), anyInt(), any(CommentDto.class))).thenReturn(commentDto);
        mvc.perform((post("/items/{itemId}/comment", item1.getId()))
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId())))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.item", is(item1.getId())))
                .andExpect(jsonPath("$.authorId", is(user2.getId())))
                .andExpect(jsonPath("$.authorName", is(user2.getName())));
        verify(service, times(1)).addComment(item1.getId(), user2.getId(), commentDto);
    }
}
