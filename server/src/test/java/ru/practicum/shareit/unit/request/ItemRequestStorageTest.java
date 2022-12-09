package ru.practicum.shareit.unit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ItemRequestStorageTest {
    Item item1;
    Item item2;
    Item item3;
    User user1;
    User user2;

    ItemRequest request1;
    ItemRequest request2;
    ItemRequest request3;

    @Autowired
    ItemStorage itemStorage;

    @Autowired
    ItemRequestStorage requestStorage;

    @Autowired
    UserStorage userStorage;

    @BeforeEach
    public void createData() {
        user1 = userStorage.save(new User("name1", "mail1@mail.com"));
        user2 = userStorage.save(new User("name2", "mail2@mail.com"));
        item1 = itemStorage.save(new Item(user1.getId(), "itemName1",
                "item1Description", true));
        item2 = itemStorage.save(new Item(user2.getId(), "itemName2",
                "item2Description", true));
        item3 = itemStorage.save(new Item(user2.getId(), "itemName3","item3Description", true));
        request1 = requestStorage.save(new ItemRequest(1, "description1",
                user1.getId(), LocalDateTime.now()));
        request2 = requestStorage.save(new ItemRequest(2, "description2",
                user1.getId(), LocalDateTime.now().minusDays(1)));
        request3 = requestStorage.save(new ItemRequest(3, "description3",
                user2.getId(), LocalDateTime.now().minusDays(2)));
    }

    @AfterEach
    public void clear() {
        itemStorage.deleteAll();
        userStorage.deleteAll();
    }

    @Test
    public void shouldFindByRequestor() {
        List<ItemRequest> requests = requestStorage.findByRequestorOrderByCreatedDesc(user1.getId());
        assertThat(requests.size()).isEqualTo(2);
        assertThat(requests.get(0)).isEqualTo(request1);
        assertThat(requests.get(1)).isEqualTo(request2);
    }

    @Test
    public void shouldFindByRequestorIsNot() {
        Page<ItemRequest> page = requestStorage.findByRequestorIsNotOrderByCreatedDesc(user2.getId(),
                PageRequest.ofSize(10));
        List<ItemRequest> requests = page.getContent();
        assertThat(requests.size()).isEqualTo(2);
        assertThat(requests.get(0)).isEqualTo(request1);
        assertThat(requests.get(1)).isEqualTo(request2);

    }
}
