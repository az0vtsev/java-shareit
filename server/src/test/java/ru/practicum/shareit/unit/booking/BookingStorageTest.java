package ru.practicum.shareit.unit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookingStorageTest {

    Item item1;
    Item item2;
    Item item3;
    User user1;
    User user2;

    Booking booking1;
    Booking booking2;
    Booking booking3;

    @Autowired
    BookingStorage bookingStorage;

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
    public void shouldFindByBooker() {
        booking1 = bookingStorage.save(new Booking(1, LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(4),
                item1.getId(), user2.getId(), BookingStatus.WAITING));
        booking2 = bookingStorage.save(new Booking(2, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(2),
                item1.getId(), user2.getId(), BookingStatus.WAITING));
        Page<Booking> page = bookingStorage.findByBookerOrderByStartDesc(user2.getId(), Pageable.ofSize(10));
        List<Booking> bookings = page.getContent();
        assertThat(bookings.size()).isEqualTo(2);
        assertThat(bookings.get(0)).isEqualTo(booking2);
        assertThat(bookings.get(1)).isEqualTo(booking1);
    }

    @Test
    public void shouldFindByBookerAndItemAndEndIsBefore() {
        booking1 = bookingStorage.save(new Booking(1, LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(4), item1.getId(), user2.getId(), BookingStatus.WAITING));
        booking2 = bookingStorage.save(new Booking(2, LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1), item1.getId(), user2.getId(), BookingStatus.WAITING));
        List<Booking> bookings = bookingStorage.findByBookerAndItemAndEndIsBefore(user2.getId(), item1.getId(),
                LocalDateTime.now().minusDays(3));
        assertThat(bookings.size()).isEqualTo(1);
        assertThat(bookings.get(0)).isEqualTo(booking1);
    }

    @Test
    public void shouldFindByBookerAndStatus() {
        booking1 = bookingStorage.save(new Booking(1, LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(4), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        booking2 = bookingStorage.save(new Booking(2, LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(2), item1.getId(), user2.getId(), BookingStatus.WAITING));
        booking3 = bookingStorage.save(new Booking(3, LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        Page<Booking> page = bookingStorage.findByBookerAndStatusOrderByStartDesc(user2.getId(),
                BookingStatus.APPROVED, Pageable.ofSize(10));
        List<Booking> bookings = page.getContent();
        assertThat(bookings.size()).isEqualTo(2);
        assertThat(bookings.get(0)).isEqualTo(booking3);
        assertThat(bookings.get(1)).isEqualTo(booking1);
    }

    @Test
    public void shouldFindByBookerAndTimeCurrent() {
        booking1 = bookingStorage.save(new Booking(1, LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(4), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        booking2 = bookingStorage.save(new Booking(2, LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(2), item1.getId(), user2.getId(), BookingStatus.WAITING));
        booking3 = bookingStorage.save(new Booking(3, LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(2), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        Page<Booking> page = bookingStorage.findByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(user2.getId(),
                LocalDateTime.now(), LocalDateTime.now(), Pageable.ofSize(10));
        List<Booking> bookings = page.getContent();
        assertThat(bookings.size()).isEqualTo(1);
        assertThat(bookings.get(0)).isEqualTo(booking3);
    }

    @Test
    public void shouldFindByBookerAndTimeFuture() {
        booking1 = bookingStorage.save(new Booking(1, LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(4), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        booking2 = bookingStorage.save(new Booking(2, LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(2), item1.getId(), user2.getId(), BookingStatus.WAITING));
        booking3 = bookingStorage.save(new Booking(3, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        Page<Booking> page = bookingStorage.findByBookerAndStartIsGreaterThanEqualOrderByStartDesc(user2.getId(),
                LocalDateTime.now(), Pageable.ofSize(10));
        List<Booking> bookings = page.getContent();
        assertThat(bookings.size()).isEqualTo(1);
        assertThat(bookings.get(0)).isEqualTo(booking3);
    }

    @Test
    public void shouldFindByBookerAndTimePast() {
        booking1 = bookingStorage.save(new Booking(1, LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(4), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        booking2 = bookingStorage.save(new Booking(2, LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(2), item1.getId(), user2.getId(), BookingStatus.WAITING));
        booking3 = bookingStorage.save(new Booking(3, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        Page<Booking> page = bookingStorage.findByBookerAndEndIsBeforeOrderByStartDesc(user2.getId(),
                LocalDateTime.now(), Pageable.ofSize(10));
        List<Booking> bookings = page.getContent();
        assertThat(bookings.size()).isEqualTo(2);
        assertThat(bookings.get(0)).isEqualTo(booking2);
        assertThat(bookings.get(1)).isEqualTo(booking1);
    }

    @Test
    public void shouldFindUserItemsBookings() {
        booking1 = bookingStorage.save(new Booking(1, LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(4), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        booking2 = bookingStorage.save(new Booking(2, LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(2), item1.getId(), user2.getId(), BookingStatus.WAITING));
        booking3 = bookingStorage.save(new Booking(3, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        Page<Booking> page = bookingStorage.findUserBookings(user1.getId(), Pageable.ofSize(10));
        List<Booking> bookings = page.getContent();
        assertThat(bookings.size()).isEqualTo(3);
        assertThat(bookings.get(0)).isEqualTo(booking3);
        assertThat(bookings.get(1)).isEqualTo(booking2);
        assertThat(bookings.get(2)).isEqualTo(booking1);
    }

    @Test
    public void shouldFindUserItemsBookingsByStatus() {
        booking1 = bookingStorage.save(new Booking(1, LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(4), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        booking2 = bookingStorage.save(new Booking(2, LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(2), item1.getId(), user2.getId(), BookingStatus.WAITING));
        booking3 = bookingStorage.save(new Booking(3, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        Page<Booking> page = bookingStorage.findUserBookingsByStatus(user1.getId(), BookingStatus.APPROVED,
                Pageable.ofSize(10));
        List<Booking> bookings = page.getContent();
        assertThat(bookings.size()).isEqualTo(2);
        assertThat(bookings.get(0)).isEqualTo(booking3);
        assertThat(bookings.get(1)).isEqualTo(booking1);
    }

    @Test
    public void shouldFindUserItemsBookingsPast() {
        booking1 = bookingStorage.save(new Booking(1, LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(4), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        booking2 = bookingStorage.save(new Booking(2, LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(2), item1.getId(), user2.getId(), BookingStatus.WAITING));
        booking3 = bookingStorage.save(new Booking(3, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        Page<Booking> page = bookingStorage.findUserBookingsPast(user1.getId(), LocalDateTime.now(),
                Pageable.ofSize(10));
        List<Booking> bookings = page.getContent();
        assertThat(bookings.size()).isEqualTo(2);
        assertThat(bookings.get(0)).isEqualTo(booking2);
        assertThat(bookings.get(1)).isEqualTo(booking1);
    }

    @Test
    public void shouldFindUserItemsBookingsFuture() {
        booking1 = bookingStorage.save(new Booking(1, LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(4), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        booking2 = bookingStorage.save(new Booking(2, LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(2), item1.getId(), user2.getId(), BookingStatus.WAITING));
        booking3 = bookingStorage.save(new Booking(3, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        Page<Booking> page = bookingStorage.findUserBookingsFuture(user1.getId(), LocalDateTime.now(),
                Pageable.ofSize(10));
        List<Booking> bookings = page.getContent();
        assertThat(bookings.size()).isEqualTo(1);
        assertThat(bookings.get(0)).isEqualTo(booking3);
    }

    @Test
    public void shouldFindUserItemsBookingsCurrent() {
        booking1 = bookingStorage.save(new Booking(1, LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(4), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        booking2 = bookingStorage.save(new Booking(2, LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(2), item1.getId(), user2.getId(), BookingStatus.WAITING));
        booking3 = bookingStorage.save(new Booking(3, LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(2), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        Page<Booking> page = bookingStorage.findUserBookingsCurrent(user1.getId(), LocalDateTime.now(),
                Pageable.ofSize(10));
        List<Booking> bookings = page.getContent();
        assertThat(bookings.size()).isEqualTo(1);
        assertThat(bookings.get(0)).isEqualTo(booking3);
    }

    @Test
    public void shouldFindByItemAndEnd() {
        booking1 = bookingStorage.save(new Booking(1, LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(4), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        booking2 = bookingStorage.save(new Booking(2, LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(2), item1.getId(), user2.getId(), BookingStatus.WAITING));
        booking3 = bookingStorage.save(new Booking(3, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        List<Booking> bookings = bookingStorage.findByItemAndEndIsBeforeOrderByEndDesc(item1.getId(),
                LocalDateTime.now());
        assertThat(bookings.size()).isEqualTo(2);
        assertThat(bookings.get(0)).isEqualTo(booking2);
        assertThat(bookings.get(1)).isEqualTo(booking1);
    }

    @Test
    public void shouldFindByItemAndStart() {
        booking1 = bookingStorage.save(new Booking(1, LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(4), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        booking2 = bookingStorage.save(new Booking(2, LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(2), item1.getId(), user2.getId(), BookingStatus.WAITING));
        booking3 = bookingStorage.save(new Booking(3, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item1.getId(), user2.getId(), BookingStatus.APPROVED));
        List<Booking> bookings = bookingStorage.findByItemAndStartIsAfterOrderByStartDesc(item1.getId(),
                LocalDateTime.now());
        assertThat(bookings.size()).isEqualTo(1);
        assertThat(bookings.get(0)).isEqualTo(booking3);
    }

}
