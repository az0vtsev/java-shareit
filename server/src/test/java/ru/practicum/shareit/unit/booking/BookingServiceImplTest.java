package ru.practicum.shareit.unit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.UnsupportedBookingDataException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith({MockitoExtension.class})
public class BookingServiceImplTest {

    private User user1;
    private User user2;
    private UserDto userDto2;
    private Item item1;
    private ItemDto itemDto1;
    private Booking booking1;
    private Booking booking2;
    private Booking booking3;
    BookingDto bookingDto1;
    private BookingService service;

    @Mock
    private BookingStorage storage;
    @Mock
    private UserStorage userStorage;
    @Mock
    private ItemStorage itemStorage;

    @BeforeEach
    public void createData() {
        service = new BookingServiceImpl(storage, userStorage, itemStorage);
        user1 = new User(1, "name1", "email1@mail.com");
        user2 = new User(2,"name2", "email2@mail.com");
        userDto2 = new UserDto(user2.getId(), user2.getName(), user2.getEmail());
        item1 = new Item(1, user1.getId(), "itemName1", "itemDescription1",
                true, null);
        itemDto1 = new ItemDto(item1.getId(), item1.getOwner(), item1.getName(), item1.getDescription(),
                item1.getAvailable(), item1.getRequestId(), new ArrayList<>());
        LocalDateTime start1 = LocalDateTime.now().plusDays(3);
        LocalDateTime end1 = start1.plusDays(1);
        booking1 = new Booking(1, start1, end1, item1.getId(), user2.getId(), BookingStatus.WAITING);
        bookingDto1 = new BookingDto(booking1.getId(), booking1.getStart(), booking1.getEnd(),
                booking1.getItem(), null, null);
        LocalDateTime start2 = LocalDateTime.now().minusDays(3);
        LocalDateTime end2 = start2.plusDays(1);
        booking2 = new Booking(1, start2, end2, item1.getId(), user2.getId(), BookingStatus.WAITING);
        LocalDateTime start3 = LocalDateTime.now().minusDays(1);
        LocalDateTime end3 = LocalDateTime.now().plusDays(1);
        booking3 = new Booking(1, start3, end3, item1.getId(), user2.getId(), BookingStatus.WAITING);
    }

    @Test
    public void shouldCreateAndReturnBooking() {
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        Optional<Item> itemResult = Optional.of(item1);
        Optional<User> userResult = Optional.of(user2);
        BookingInfoDto bookingInfoDto = new BookingInfoDto(booking1.getId(), booking1.getStart(),
                booking1.getEnd(), itemDto1, userDto2, booking1.getStatus());
        Mockito.when(itemStorage.findById(anyInt())).thenReturn(itemResult);
        Mockito.when(userStorage.findById(anyInt())).thenReturn(userResult);
        Mockito.when(storage.save(any(Booking.class))).thenReturn(booking1);
                BookingInfoDto result = service.createBooking(user2.getId(), bookingDto1);
        assertThat(result).isEqualTo(bookingInfoDto);
    }

    @Test
    public void shouldUpdateAndReturnBooking() {
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        Mockito.when(storage.existsById(anyInt())).thenReturn(true);
        Optional<Booking> bookingResult = Optional.of(booking1);
        Mockito.when(storage.findById(anyInt())).thenReturn(bookingResult);

        Optional<Item> itemResult = Optional.of(item1);
        Mockito.when(itemStorage.findById(anyInt())).thenReturn(itemResult);

        Optional<User> userResult = Optional.of(user2);
        Mockito.when(userStorage.findById(anyInt())).thenReturn(userResult);
        Booking updateBooking = new Booking(booking1.getId(), booking1.getStart(), booking1.getEnd(),
                booking1.getItem(), booking1.getBooker(), BookingStatus.APPROVED);
        Mockito.when(storage.save(any(Booking.class))).thenReturn(updateBooking);

                BookingInfoDto bookingInfoDto = new BookingInfoDto(updateBooking.getId(), updateBooking.getStart(),
                        updateBooking.getEnd(), itemDto1, userDto2, updateBooking.getStatus());
        BookingInfoDto result = service.updateBookingStatus(user1.getId(), booking1.getId(), true);

        assertThat(result).isEqualTo(bookingInfoDto);
    }

    @Test
    public void shouldReturnBookingById() {
        Mockito.when(storage.existsById(anyInt())).thenReturn(true);
        Optional<Booking> bookingResult = Optional.of(booking1);
        Mockito.when(storage.findById(anyInt())).thenReturn(bookingResult);
        Optional<User> userResult = Optional.of(user2);
        Mockito.when(userStorage.findById(anyInt())).thenReturn(userResult);
        Optional<Item> itemResult = Optional.of(item1);
        Mockito.when(itemStorage.findById(anyInt())).thenReturn(itemResult);
        BookingInfoDto bookingInfoDto = new BookingInfoDto(booking1.getId(), booking1.getStart(),
                booking1.getEnd(), itemDto1, userDto2, booking1.getStatus());
        BookingInfoDto result = service.getBookingById(booking1.getId(), user2.getId());
        assertThat(result).isEqualTo(bookingInfoDto);

    }

    @Test
    public void shouldReturnByBookerAndStateAll() {
        Page<Booking> page = new PageImpl<>(List.of(booking1, booking3, booking2));
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        Mockito.when(storage.findByBookerOrderByStartDesc(anyInt(), any(PageRequest.class)))
                .thenReturn(page);
        Optional<User> userResult = Optional.of(user2);
        Mockito.when(userStorage.findById(anyInt())).thenReturn(userResult);
        Optional<Item> itemResult = Optional.of(item1);
        Mockito.when(itemStorage.findById(anyInt())).thenReturn(itemResult);
        List<BookingInfoDto> result = service.getUserBookings(user2.getId(), "ALL", 0, 10);
        BookingInfoDto bookingInfoDto = new BookingInfoDto(booking1.getId(), booking1.getStart(),
                booking1.getEnd(), itemDto1, userDto2, booking1.getStatus());
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0)).isEqualTo(bookingInfoDto);

    }

    @Test
    public void shouldReturnByBookerAndStateWaitingOrRejected() {
        booking3.setStatus(BookingStatus.REJECTED);
        Page<Booking> pageWaiting = new PageImpl<>(List.of(booking1, booking2));
        Page<Booking> pageRejected = new PageImpl<>(List.of(booking3));
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        Mockito.when(storage.findByBookerAndStatusOrderByStartDesc(anyInt(), eq(BookingStatus.WAITING),
                        any(PageRequest.class))).thenReturn(pageWaiting);
        Mockito.when(storage.findByBookerAndStatusOrderByStartDesc(anyInt(), eq(BookingStatus.REJECTED),
                any(PageRequest.class))).thenReturn(pageRejected);
        Optional<User> userResult = Optional.of(user2);
        Mockito.when(userStorage.findById(anyInt())).thenReturn(userResult);
        Optional<Item> itemResult = Optional.of(item1);
        Mockito.when(itemStorage.findById(anyInt())).thenReturn(itemResult);
        List<BookingInfoDto> resultWaiting = service.getUserBookings(user2.getId(), "WAITING", 0, 10);
        List<BookingInfoDto> resultRejected = service.getUserBookings(user2.getId(), "REJECTED", 0, 10);
        BookingInfoDto bookingInfoDto1 = new BookingInfoDto(booking1.getId(), booking1.getStart(),
                booking1.getEnd(), itemDto1, userDto2, booking1.getStatus());
        BookingInfoDto bookingInfoDto3 = new BookingInfoDto(booking3.getId(), booking3.getStart(),
                booking3.getEnd(), itemDto1, userDto2, booking3.getStatus());
        assertThat(resultWaiting.size()).isEqualTo(2);
        assertThat(resultRejected.size()).isEqualTo(1);

        assertThat(resultWaiting.get(0)).isEqualTo(bookingInfoDto1);
        assertThat(resultRejected.get(0)).isEqualTo(bookingInfoDto3);

    }

    @Test
    public void shouldReturnByBookerAndStatePastOrCurrentOrFuture() {
        Page<Booking> pageFuture = new PageImpl<>(List.of(booking1));
        Page<Booking> pagePast = new PageImpl<>(List.of(booking2));
        Page<Booking> pageCurrent = new PageImpl<>(List.of(booking3));
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        Mockito.when(storage.findByBookerAndEndIsBeforeOrderByStartDesc(anyInt(),
                any(LocalDateTime.class), any(PageRequest.class))).thenReturn(pagePast);
        Mockito.when(storage.findByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(anyInt(),
                any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(pageCurrent);
        Mockito.when(storage.findByBookerAndStartIsGreaterThanEqualOrderByStartDesc(anyInt(),
                any(LocalDateTime.class), any(PageRequest.class))).thenReturn(pageFuture);
        Optional<User> userResult = Optional.of(user2);
        Mockito.when(userStorage.findById(anyInt())).thenReturn(userResult);
        Optional<Item> itemResult = Optional.of(item1);
        Mockito.when(itemStorage.findById(anyInt())).thenReturn(itemResult);
        List<BookingInfoDto> resultPast = service.getUserBookings(user2.getId(), "PAST", 0, 10);
        List<BookingInfoDto> resultCurrent = service.getUserBookings(user2.getId(), "CURRENT", 0, 10);
        List<BookingInfoDto> resultFuture = service.getUserBookings(user2.getId(), "FUTURE", 0, 10);
        BookingInfoDto bookingInfoDto1 = new BookingInfoDto(booking1.getId(), booking1.getStart(),
                booking1.getEnd(), itemDto1, userDto2, booking1.getStatus());
        BookingInfoDto bookingInfoDto2 = new BookingInfoDto(booking2.getId(), booking2.getStart(),
                booking2.getEnd(), itemDto1, userDto2, booking2.getStatus());
        BookingInfoDto bookingInfoDto3 = new BookingInfoDto(booking3.getId(), booking3.getStart(),
                booking3.getEnd(), itemDto1, userDto2, booking3.getStatus());
        assertThat(resultPast.size()).isEqualTo(1);
        assertThat(resultFuture.size()).isEqualTo(1);
        assertThat(resultCurrent.size()).isEqualTo(1);

        assertThat(resultPast.get(0)).isEqualTo(bookingInfoDto2);
        assertThat(resultCurrent.get(0)).isEqualTo(bookingInfoDto3);
        assertThat(resultFuture.get(0)).isEqualTo(bookingInfoDto1);

    }

    @Test
    public void shouldReturnOwnerItemsBookingAndStatePast() {
        Page<Booking> pagePast = new PageImpl<>(List.of(booking2));
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        Mockito.when(storage.findUserBookingsPast(anyInt(),
                any(LocalDateTime.class), any(PageRequest.class))).thenReturn(pagePast);

        Optional<User> userResult = Optional.of(user2);
        Mockito.when(userStorage.findById(anyInt())).thenReturn(userResult);
        Optional<Item> itemResult = Optional.of(item1);
        Mockito.when(itemStorage.findById(anyInt())).thenReturn(itemResult);

        List<BookingInfoDto> resultPast = service.getUserItemsBookings(user1.getId(), "PAST", 0, 10);

        BookingInfoDto bookingInfoDto2 = new BookingInfoDto(booking2.getId(), booking2.getStart(),
                booking2.getEnd(), itemDto1, userDto2, booking2.getStatus());

        assertThat(resultPast.size()).isEqualTo(1);

        assertThat(resultPast.get(0)).isEqualTo(bookingInfoDto2);

    }

    @Test
    public void shouldReturnOwnerItemsBookingAndStateCurrent() {
        Page<Booking> pageCurrent = new PageImpl<>(List.of(booking3));
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);

        Mockito.when(storage.findUserBookingsCurrent(anyInt(),
                        any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(pageCurrent);

        Optional<User> userResult = Optional.of(user2);
        Mockito.when(userStorage.findById(anyInt())).thenReturn(userResult);
        Optional<Item> itemResult = Optional.of(item1);
        Mockito.when(itemStorage.findById(anyInt())).thenReturn(itemResult);

        List<BookingInfoDto> resultCurrent = service.getUserItemsBookings(user1.getId(), "CURRENT", 0, 10);


        BookingInfoDto bookingInfoDto3 = new BookingInfoDto(booking3.getId(), booking3.getStart(),
                booking3.getEnd(), itemDto1, userDto2, booking3.getStatus());

        assertThat(resultCurrent.size()).isEqualTo(1);

        assertThat(resultCurrent.get(0)).isEqualTo(bookingInfoDto3);


    }

    @Test
    public void shouldReturnOwnerItemsBookingAndStateFuture() {
        Page<Booking> pageFuture = new PageImpl<>(List.of(booking1));

        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);

        Mockito.when(storage.findUserBookingsFuture(anyInt(),
                any(LocalDateTime.class), any(PageRequest.class))).thenReturn(pageFuture);
        Optional<User> userResult = Optional.of(user2);
        Mockito.when(userStorage.findById(anyInt())).thenReturn(userResult);
        Optional<Item> itemResult = Optional.of(item1);
        Mockito.when(itemStorage.findById(anyInt())).thenReturn(itemResult);

        List<BookingInfoDto> resultFuture = service.getUserItemsBookings(user1.getId(), "FUTURE", 0, 10);
        BookingInfoDto bookingInfoDto1 = new BookingInfoDto(booking1.getId(), booking1.getStart(),
                booking1.getEnd(), itemDto1, userDto2, booking1.getStatus());

        assertThat(resultFuture.size()).isEqualTo(1);

        assertThat(resultFuture.get(0)).isEqualTo(bookingInfoDto1);

    }

    @Test
    public void shouldReturnOwnerItemsBookingAndStateWaitingOrRejected() {
        booking3.setStatus(BookingStatus.REJECTED);
        Page<Booking> pageWaiting = new PageImpl<>(List.of(booking1, booking2));
        Page<Booking> pageRejected = new PageImpl<>(List.of(booking3));
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        Mockito.when(storage.findUserBookingsByStatus(anyInt(), eq(BookingStatus.WAITING),
                any(PageRequest.class))).thenReturn(pageWaiting);
        Mockito.when(storage.findUserBookingsByStatus(anyInt(), eq(BookingStatus.REJECTED),
                any(PageRequest.class))).thenReturn(pageRejected);
        Optional<User> userResult = Optional.of(user2);
        Mockito.when(userStorage.findById(anyInt())).thenReturn(userResult);
        Optional<Item> itemResult = Optional.of(item1);
        Mockito.when(itemStorage.findById(anyInt())).thenReturn(itemResult);
        List<BookingInfoDto> resultWaiting = service.getUserItemsBookings(user1.getId(),
                "WAITING", 0, 10);
        List<BookingInfoDto> resultRejected = service.getUserItemsBookings(user1.getId(),
                "REJECTED", 0, 10);
        BookingInfoDto bookingInfoDto1 = new BookingInfoDto(booking1.getId(), booking1.getStart(),
                booking1.getEnd(), itemDto1, userDto2, booking1.getStatus());
        BookingInfoDto bookingInfoDto3 = new BookingInfoDto(booking3.getId(), booking3.getStart(),
                booking3.getEnd(), itemDto1, userDto2, booking3.getStatus());
        assertThat(resultWaiting.size()).isEqualTo(2);
        assertThat(resultRejected.size()).isEqualTo(1);

        assertThat(resultWaiting.get(0)).isEqualTo(bookingInfoDto1);
        assertThat(resultRejected.get(0)).isEqualTo(bookingInfoDto3);

    }

    @Test
    public void shouldReturnOwnerItemsBookingAndStateAll() {
        Page<Booking> page = new PageImpl<>(List.of(booking1, booking3, booking2));
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        Mockito.when(storage.findUserBookings(anyInt(), any(PageRequest.class)))
                .thenReturn(page);
        Optional<User> userResult = Optional.of(user2);
        Mockito.when(userStorage.findById(anyInt())).thenReturn(userResult);
        Optional<Item> itemResult = Optional.of(item1);
        Mockito.when(itemStorage.findById(anyInt())).thenReturn(itemResult);
        List<BookingInfoDto> result = service.getUserItemsBookings(user1.getId(), "ALL", 0, 10);
        BookingInfoDto bookingInfoDto = new BookingInfoDto(booking1.getId(), booking1.getStart(),
                booking1.getEnd(), itemDto1, userDto2, booking1.getStatus());
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0)).isEqualTo(bookingInfoDto);
    }

    @Test
    public void shouldThrowNotSupportedBookingDataException() {
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        Mockito.when(itemStorage.findById(anyInt())).thenReturn(Optional.of(new Item(1, 1, "name",
                "description", false, null)));
        assertThrows(UnsupportedBookingDataException.class, () -> service.createBooking(2, bookingDto1));
    }

}
