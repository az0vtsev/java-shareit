package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ItemRequestServiceImplTest {
    private final ItemRequestService service;
    private final ItemService itemService;
    private final UserService userService;

    private User user1;
    private User user2;
    private UserDto user1Dto;
    private UserDto user2Dto;

    private Item item1;
    private Item item2;
    private Item item3;

    private ItemDto itemDto1;
    private ItemDto itemDto2;
    private ItemDto itemDto3;

    private ItemRequest request1;
    private ItemRequest request2;
    private ItemRequest request3;

    private ItemRequestDto requestDto1;
    private ItemRequestDto requestDto2;
    private ItemRequestDto requestDto3;

    @BeforeEach
    public void createData() {
        user1 = new User(1, "name1", "email1@mail.com");
        user2 = new User(2, "name2", "email2@mail.com");
        user1Dto = new UserDto(1, user1.getName(), user1.getEmail());
        user2Dto = new UserDto(2, user2.getName(),  user2.getEmail());
        userService.createUser(user1Dto);
        userService.createUser(user2Dto);
        item1 = new Item(1, user1.getId(), "itemName1", "description1", true, null);
        item2 = new Item(2, user1.getId(), "itemName2", "description2", true, null);
        item3 = new Item(3, user2.getId(), "itemName3", "description3", true, null);
        itemDto1 = ItemMapper.mapToItemDto(item1);
        itemDto2 = ItemMapper.mapToItemDto(item2);
        itemDto3 = ItemMapper.mapToItemDto(item3);
        itemService.createItem(itemDto1);
        itemService.createItem(itemDto2);
        itemService.createItem(itemDto3);

        request1 = new ItemRequest(1, "description1", user1Dto.getId(), LocalDateTime.now());
        request2 = new ItemRequest(2, "description2", user2Dto.getId(), LocalDateTime.now().minusDays(1));
        request3 = new ItemRequest(3, "description3", user1Dto.getId(), LocalDateTime.now().minusDays(2));
        requestDto1 = ItemRequestMapper.mapToItemRequestDto(request1);
        requestDto2 = ItemRequestMapper.mapToItemRequestDto(request2);
        requestDto3 = ItemRequestMapper.mapToItemRequestDto(request3);
        service.createItemRequest(user1Dto.getId(), requestDto1);
        service.createItemRequest(user2Dto.getId(), requestDto2);
        service.createItemRequest(user1Dto.getId(), requestDto3);
    }

    @Test
    public void shouldReturnItemById() {

        ItemRequestInfoDto result = service.getItemRequest(user1Dto.getId(), requestDto1.getId());

        assertThat(result.getId()).isEqualTo(requestDto1.getId());
        assertThat(result.getDescription()).isEqualTo(requestDto1.getDescription());
        assertThat(result.getRequestor()).isEqualTo(requestDto1.getRequestor());

    }

    @Test
    public void shouldReturnAll() {

        List<ItemRequestInfoDto> result = service.getItemRequests(0, 10, user2Dto.getId());

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(requestDto3.getId());
        assertThat(result.get(1).getId()).isEqualTo(requestDto1.getId());

    }

    @Test
    public void shouldReturnUserItemRequests() {
        List<ItemRequestInfoDto> result = service.getUserItemRequests(user2Dto.getId());

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(requestDto2.getId());
        assertThat(result.get(0).getRequestor()).isEqualTo(user2Dto.getId());

    }

}
