package ru.practicum.shareit.booking.storage;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingStorage extends JpaRepository<Booking, Integer> {
    List<Booking> findByBookerOrderByStartDesc(int booker);

    List<Booking> findByBookerAndItemAndEndIsBefore(int booker, int item, LocalDateTime end);

    List<Booking> findByBookerAndStatusOrderByStartDesc(int booker, @NonNull BookingStatus status);

    List<Booking> findByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(int booker, @NonNull LocalDateTime start,
                                                                    @NonNull LocalDateTime end);

    List<Booking> findByBookerAndStartIsAfterOrderByStartDesc(int booker, @NonNull LocalDateTime start);

    List<Booking> findByBookerAndStartIsGreaterThanEqualOrderByStartDesc(int booker, @NonNull LocalDateTime start);

    List<Booking> findByBookerAndEndIsBeforeOrderByStartDesc(int booker, @NonNull LocalDateTime start);

    @Query(" select b from Booking b, Item i "
            + "where b.item = i.id and i.owner = ?1 "
            + "order by b.start desc ")
    List<Booking> findUserBookings(int userId);

    @Query(" select b from Booking b, Item i "
            + "where b.item = i.id and i.owner = ?1 "
            + "and b.status = ?2 "
            + "order by b.start desc ")
    List<Booking> findUserBookingsByStatus(int userId, BookingStatus status);

    @Query(" select b from Booking b, Item i "
            + "where b.item = i.id and i.owner = ?1 "
            + "and b.end <= ?2 "
            + "order by b.start desc ")
    List<Booking> findUserBookingsPast(int userId, LocalDateTime now);

    @Query(" select b from Booking b, Item i "
            + "where ( b.item = i.id and i.owner = ?1 )"
            + "and ( b.start <= ?2 and b.end >= ?2)"
            + "order by b.start desc ")
    List<Booking> findUserBookingsCurrent(int userId, LocalDateTime now);

    @Query(" select b from Booking b, Item i "
            + "where b.item = i.id and i.owner = ?1 "
            + "and b.start >= ?2 "
            + "order by b.start desc ")
    List<Booking> findUserBookingsFuture(int userId, LocalDateTime now);

    List<Booking> findByItemAndEndIsBeforeOrderByEndDesc(int item, LocalDateTime end);

    List<Booking> findByItemAndStartIsAfterOrderByStartDesc(int item, LocalDateTime start);

}
