package ru.practicum.shareit.unit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemMapperTest {

    private Item item;
    private ItemDto itemDto;

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void createTestData() {
        user = new User(1, "name", "email@mail.com");
        userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
        item = new Item(1, user.getId(), "itemName", "itemDescription", true, 2);
        itemDto = new ItemDto(item.getId(), item.getOwner(), item.getName(),
                item.getDescription(), item.getAvailable(), item.getRequestId(), new ArrayList<>());
    }

    @Test
    public void shouldMapToItemTest() {
        Item result = ItemMapper.mapToItem(itemDto);
        assertThat(result).isEqualTo(item);
    }

    @Test
    public void shouldMapToItemDtoTest() {
        ItemDto result = ItemMapper.mapToItemDto(item);
        assertThat(result).isEqualTo(itemDto);
    }

    @Test
    public void shouldMapToItemInfoDtoTest() {
        LocalDateTime end1 = LocalDateTime.now().minusDays(1);
        LocalDateTime start1 = end1.minusDays(1);
        LocalDateTime start2 = LocalDateTime.now().plusDays(1);
        LocalDateTime end2 = start2.plusDays(1);
        BookingItemDto bookingPrev = new BookingItemDto(1, start1, end1, itemDto,
                userDto.getId(), BookingStatus.APPROVED);
        BookingItemDto bookingNext = new BookingItemDto(2, start2, end2, itemDto,
                userDto.getId(), BookingStatus.APPROVED);
        ItemInfoDto itemInfoDto = new ItemInfoDto(item.getId(), userDto, item.getName(),
                item.getDescription(), item.getAvailable(), item.getRequestId(),
                new ArrayList<>(), bookingPrev, bookingNext);

        ItemInfoDto result = ItemMapper.mapToItemInfoDto(item, userDto, bookingPrev, bookingNext);
        assertThat(result).isEqualTo(itemInfoDto);
    }

    @Test
    public void shouldMapToRequestItemDtoTest() {
        RequestItemDto requestItemDto = new RequestItemDto(item.getId(), item.getName(),
                item.getDescription(), item.getAvailable(), item.getRequestId());

        RequestItemDto result = ItemMapper.mapToRequestItemDto(item);
        assertThat(result).isEqualTo(requestItemDto);
    }

}
