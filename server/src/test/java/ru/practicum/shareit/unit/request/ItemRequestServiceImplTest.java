package ru.practicum.shareit.unit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith({MockitoExtension.class})
public class ItemRequestServiceImplTest {

    private User user1;
    private User user2;
    private Item item1;

    private ItemRequest itemRequest1;
    private ItemRequest itemRequest2;
    private ItemRequest itemRequest3;
    private ItemRequestService service;

    @Mock
    private ItemRequestStorage storage;
    @Mock
    private UserStorage userStorage;
    @Mock
    private ItemStorage itemStorage;

    @BeforeEach
    public void createData() {
        service = new ItemRequestServiceImpl(storage, userStorage, itemStorage);
        user1 = new User(1, "name1", "email1@mail.com");
        user2 = new User(2,"name2", "email2@mail.com");
        item1 = new Item(1, user2.getId(), "itemName1", "itemDescription1",
                true, 1);
        LocalDateTime created = LocalDateTime.now();
        itemRequest1 = new ItemRequest(1, "description1", user1.getId(), created);
        itemRequest2 = new ItemRequest(2, "description2", user1.getId(), created);
        itemRequest3 = new ItemRequest(3, "description3", user2.getId(), created);
    }

    @Test
    public void shouldCreateAndReturnItemRequestDto() {
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        ItemRequestInfoDto itemRequestInfoDto = new ItemRequestInfoDto(itemRequest1.getId(),
                itemRequest1.getDescription(), itemRequest1.getRequestor(),
                itemRequest1.getCreated(), new ArrayList<>());
        Mockito.when(storage.save(any(ItemRequest.class))).thenReturn(itemRequest1);
        ItemRequestDto itemRequestDto = new ItemRequestDto(itemRequest1.getId(), itemRequest1.getDescription(),
                itemRequest1.getRequestor(), itemRequest1.getCreated());
        ItemRequestInfoDto result = service.createItemRequest(user1.getId(), itemRequestDto);

        assertThat(result.getId()).isEqualTo(itemRequestInfoDto.getId());
        assertThat(result.getDescription()).isEqualTo(itemRequestInfoDto.getDescription());
        assertThat(result.getRequestor()).isEqualTo(itemRequestInfoDto.getRequestor());

    }

    @Test
    public void shouldReturnUserItemRequests() {
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        Mockito.when(storage.findByRequestorOrderByCreatedDesc(anyInt()))
                .thenReturn(List.of(itemRequest1, itemRequest2));
        ItemRequestInfoDto itemRequestInfoDto = new ItemRequestInfoDto(itemRequest1.getId(),
                itemRequest1.getDescription(), itemRequest1.getRequestor(),
                itemRequest1.getCreated(), new ArrayList<>());
        List<ItemRequestInfoDto> result = service.getUserItemRequests(user1.getId());
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getRequestor()).isEqualTo(user1.getId());
        assertThat(result.get(0).getDescription()).isEqualTo(itemRequestInfoDto.getDescription());
    }

    @Test
    public void shouldReturnItemRequests() {
        Page<ItemRequest> page = new PageImpl<>(List.of(itemRequest3));
        Mockito.when(storage
                .findByRequestorIsNotOrderByCreatedDesc(anyInt(), any(PageRequest.class)))
                .thenReturn(page);
        List<ItemRequestInfoDto> result = service.getItemRequests(0, 10, 1);
        ItemRequestInfoDto itemRequestInfoDto = new ItemRequestInfoDto(itemRequest3.getId(),
                itemRequest3.getDescription(), itemRequest3.getRequestor(),
                itemRequest3.getCreated(), new ArrayList<>());
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getRequestor()).isEqualTo(user2.getId());
        assertThat(result.get(0).getDescription()).isEqualTo(itemRequestInfoDto.getDescription());
    }

    @Test
    public void shouldReturnItemRequestWithAnswer() {
        Mockito.when(storage.existsById(anyInt())).thenReturn(true);
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        Optional<ItemRequest> itemRequestResult = Optional.of(itemRequest1);
        Mockito.when(storage.findById(anyInt())).thenReturn(itemRequestResult);
        RequestItemDto requestItemDto = new RequestItemDto(item1.getId(), item1.getName(),
                item1.getDescription(), item1.getAvailable(), item1.getRequestId());
        ItemRequestInfoDto itemRequestInfoDto = new ItemRequestInfoDto(itemRequest1.getId(),
                itemRequest1.getDescription(), itemRequest1.getRequestor(),
                itemRequest1.getCreated(), List.of(requestItemDto));
        Mockito.when(itemStorage.findByRequestId(anyInt())).thenReturn(List.of(item1));
        ItemRequestInfoDto result = service.getItemRequest(1, 1);
        assertThat(result.getItems().get(0)).isEqualTo(requestItemDto);
        assertThat(result).isEqualTo(itemRequestInfoDto);

    }
}
