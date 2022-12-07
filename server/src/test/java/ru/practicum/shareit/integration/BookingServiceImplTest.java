package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
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
public class BookingServiceImplTest {
    private final ItemService itemService;
    private final BookingService service;
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

    private Booking booking1;
    private Booking booking2;
    private Booking booking3;

    private BookingDto bookingDto1;
    private BookingDto bookingDto2;
    private BookingDto bookingDto3;

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
        LocalDateTime start = LocalDateTime.now();
        booking1 = new Booking(1, start.minusDays(5), start.minusDays(4), itemDto1.getId(),
                user2Dto.getId(), BookingStatus.WAITING);
        booking2 = new Booking(2, start.minusDays(3), start.minusDays(2), itemDto1.getId(),
                user2Dto.getId(), BookingStatus.WAITING);
        booking3 = new Booking(3, start.minusDays(1), start.plusDays(1), itemDto1.getId(),
                user2Dto.getId(), BookingStatus.WAITING);
        bookingDto1 = BookingMapper.mapToBookingDto(booking1);
        bookingDto2 = BookingMapper.mapToBookingDto(booking2);
        bookingDto3 = BookingMapper.mapToBookingDto(booking3);
        service.createBooking(user2Dto.getId(), bookingDto1);
        service.createBooking(user2Dto.getId(), bookingDto2);
        service.createBooking(user2Dto.getId(), bookingDto3);

    }

    @Test
    public void shouldReturnBookingById() {

        BookingInfoDto result = service.getBookingById(bookingDto1.getId(), user1.getId());

        assertThat(result.getId()).isEqualTo(bookingDto1.getId());
        assertThat(result.getStart()).isEqualTo(bookingDto1.getStart());
        assertThat(result.getEnd()).isEqualTo(bookingDto1.getEnd());
        assertThat(result.getBooker().getId()).isEqualTo(user2Dto.getId());
        assertThat(result.getItem().getId()).isEqualTo(itemDto1.getId());
    }

    @Test
    public void shouldReturnAll() {
        List<BookingInfoDto> result = service.getUserBookings(user2Dto.getId(), "ALL", 0, 10);

        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getId()).isEqualTo(bookingDto3.getId());
        assertThat(result.get(0).getStart()).isEqualTo(bookingDto3.getStart());
        assertThat(result.get(0).getEnd()).isEqualTo(bookingDto3.getEnd());
        assertThat(result.get(0).getBooker().getId()).isEqualTo(user2Dto.getId());
        assertThat(result.get(0).getItem().getId()).isEqualTo(itemDto1.getId());
        assertThat(result.get(2).getId()).isEqualTo(bookingDto1.getId());
        assertThat(result.get(2).getStart()).isEqualTo(bookingDto1.getStart());
        assertThat(result.get(2).getEnd()).isEqualTo(bookingDto1.getEnd());
        assertThat(result.get(2).getBooker().getId()).isEqualTo(user2Dto.getId());
        assertThat(result.get(2).getItem().getId()).isEqualTo(itemDto1.getId());

    }

    @Test
    public void shouldReturnAllItemsBookings() {
        List<BookingInfoDto> result = service.getUserItemsBookings(user1Dto.getId(), "ALL", 0, 10);

        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getId()).isEqualTo(bookingDto3.getId());
        assertThat(result.get(0).getStart()).isEqualTo(bookingDto3.getStart());
        assertThat(result.get(0).getEnd()).isEqualTo(bookingDto3.getEnd());
        assertThat(result.get(0).getBooker().getId()).isEqualTo(user2Dto.getId());
        assertThat(result.get(0).getItem().getId()).isEqualTo(itemDto1.getId());
        assertThat(result.get(0).getItem().getOwner()).isEqualTo(user1Dto.getId());
        assertThat(result.get(2).getId()).isEqualTo(bookingDto1.getId());
        assertThat(result.get(2).getStart()).isEqualTo(bookingDto1.getStart());
        assertThat(result.get(2).getEnd()).isEqualTo(bookingDto1.getEnd());
        assertThat(result.get(2).getBooker().getId()).isEqualTo(user2Dto.getId());
        assertThat(result.get(2).getItem().getId()).isEqualTo(itemDto1.getId());
        assertThat(result.get(2).getItem().getOwner()).isEqualTo(user1Dto.getId());

    }

}
