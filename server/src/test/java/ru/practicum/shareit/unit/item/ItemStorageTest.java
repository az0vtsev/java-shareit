package ru.practicum.shareit.unit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ItemStorageTest {
    Item item1;
    Item item2;
    Item item3;
    User user1;
    User user2;

    @Autowired
    ItemStorage itemStorage;

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
        item3 = itemStorage.save(new Item(user2.getId(), "itemName3",
                "item3Description", true));
    }

    @AfterEach
    public void clear() {
        itemStorage.deleteAll();
        userStorage.deleteAll();
    }

    @Test
    public void shouldFindByOwner() {
        Page<Item> page = itemStorage.findByOwnerOrderById(user2.getId(), Pageable.ofSize(10));
        List<Item> items = page.getContent();
        assertThat(items.size()).isEqualTo(2);
        assertThat(items.get(0)).isEqualTo(item2);
        assertThat(items.get(1)).isEqualTo(item3);
    }

    @Test
    public void shouldFindByText() {
        Page<Item> page = itemStorage.search("itemName2", Pageable.ofSize(10));
        List<Item> items = page.getContent();
        assertThat(items.size()).isEqualTo(1);
        assertThat(items.get(0)).isEqualTo(item2);
    }
}
