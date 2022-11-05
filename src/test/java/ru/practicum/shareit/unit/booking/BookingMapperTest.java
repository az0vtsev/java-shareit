package ru.practicum.shareit.unit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingMapperTest {

    private User booker;
    private UserDto bookerDto;
    private ItemDto itemDto;
    private Booking booking;
    private BookingDto bookingDto;

    @BeforeEach
    public void createTestData() {
        User user = new User(1, "name", "email@mail.com");
        booker = new User(2, "name2", "email2@mail.com");
        bookerDto = new UserDto(booker.getId(), booker.getName(), booker.getEmail());
        Item item = new Item(1, user.getId(), "itemName", "itemDescription", true, 2);
        itemDto = new ItemDto(item.getId(), item.getOwner(), item.getName(), item.getDescription(),
                item.getAvailable(), item.getRequestId(), new ArrayList<>());
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        booking = new Booking(1, start, end, item.getId(), booker.getId(), BookingStatus.WAITING);
        bookingDto = new BookingDto(booking.getId(), booking.getStart(), booking.getEnd(),
                booking.getItem(), booking.getBooker(), booking.getStatus());
    }

    @Test
    public void shouldMapToBookingTest() {
        Booking result = BookingMapper.mapToBooking(bookingDto);
        assertThat(result).isEqualTo(booking);
    }

    @Test
    public void shouldMapToBookingDtoTest() {
        BookingDto result = BookingMapper.mapToBookingDto(booking);
        assertThat(result).isEqualTo(bookingDto);
    }

    @Test
    public void shouldMapToBookingItemDtoTest() {
        BookingItemDto bookingItemDto = new BookingItemDto(booking.getId(), booking.getStart(),
                booking.getEnd(), itemDto, booker.getId(),booking.getStatus());
        BookingItemDto result = BookingMapper.mapToBookingItemDto(booking, itemDto, booker.getId());
        assertThat(result).isEqualTo(bookingItemDto);
    }

    @Test
    public void shouldMapToBookingInfoDtoTest() {
        BookingInfoDto bookingInfoDto = new BookingInfoDto(booking.getId(), booking.getStart(),
                booking.getEnd(), itemDto, bookerDto, booking.getStatus());
        BookingInfoDto result = BookingMapper.mapToBookingInfoDto(booking, itemDto, bookerDto);
        assertThat(result).isEqualTo(bookingInfoDto);
    }

}

