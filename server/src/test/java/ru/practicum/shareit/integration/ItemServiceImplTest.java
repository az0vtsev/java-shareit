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
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
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
public class ItemServiceImplTest {
    private final ItemService service;
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
        service.createItem(itemDto1);
        service.createItem(itemDto2);
        service.createItem(itemDto3);
    }

    @Test
    public void shouldReturnItemById() {

        ItemInfoDto result = service.getItemById(item1.getId(), user1.getId());

        assertThat(result.getId()).isEqualTo(itemDto1.getId());
        assertThat(result.getOwner()).isEqualTo(user1Dto);
        assertThat(result.getName()).isEqualTo(itemDto1.getName());
        assertThat(result.getDescription()).isEqualTo(itemDto1.getDescription());
    }

    @Test
    public void shouldReturnAll() {

        List<ItemInfoDto> result = service.getItemsByOwner(user1.getId(), 0, 10);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(itemDto1.getId());
        assertThat(result.get(0).getOwner()).isEqualTo(user1Dto);
        assertThat(result.get(0).getName()).isEqualTo(itemDto1.getName());
        assertThat(result.get(0).getDescription()).isEqualTo(itemDto1.getDescription());
        assertThat(result.get(1).getId()).isEqualTo(itemDto2.getId());
        assertThat(result.get(1).getOwner()).isEqualTo(user1Dto);
        assertThat(result.get(1).getName()).isEqualTo(itemDto2.getName());
        assertThat(result.get(1).getDescription()).isEqualTo(itemDto2.getDescription());


    }

}
