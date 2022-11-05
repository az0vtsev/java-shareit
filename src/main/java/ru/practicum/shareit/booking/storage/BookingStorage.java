package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingStorage extends JpaRepository<Booking, Integer> {
    Page<Booking> findByBookerOrderByStartDesc(int booker, Pageable pageable);

    List<Booking> findByBookerAndItemAndEndIsBefore(int booker, int item, LocalDateTime end);

    Page<Booking> findByBookerAndStatusOrderByStartDesc(int booker, BookingStatus status, Pageable pageable);

    Page<Booking> findByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(int booker,
                                                                            LocalDateTime start, LocalDateTime end,
                                                                            Pageable pageable);

    Page<Booking> findByBookerAndStartIsAfterOrderByStartDesc(int booker, LocalDateTime start,
                                                              Pageable pageable);

    Page<Booking> findByBookerAndStartIsGreaterThanEqualOrderByStartDesc(int booker, LocalDateTime start,
                                                                         Pageable pageable);

    Page<Booking> findByBookerAndEndIsBeforeOrderByStartDesc(int booker, LocalDateTime start, Pageable pageable);

    @Query(" select b from Booking b, Item i "
            + "where b.item = i.id and i.owner = ?1 "
            + "order by b.start desc ")
    Page<Booking> findUserBookings(int userId, Pageable pageable);

    @Query(" select b from Booking b, Item i "
            + "where b.item = i.id and i.owner = ?1 "
            + "and b.status = ?2 "
            + "order by b.start desc ")
    Page<Booking> findUserBookingsByStatus(int userId, BookingStatus status, Pageable pageable);

    @Query(" select b from Booking b, Item i "
            + "where b.item = i.id and i.owner = ?1 "
            + "and b.end <= ?2 "
            + "order by b.start desc ")
    Page<Booking> findUserBookingsPast(int userId, LocalDateTime now, Pageable pageable);

    @Query(" select b from Booking b, Item i "
            + "where ( b.item = i.id and i.owner = ?1 )"
            + "and ( b.start <= ?2 and b.end >= ?2)"
            + "order by b.start desc ")
    Page<Booking> findUserBookingsCurrent(int userId, LocalDateTime now, Pageable pageable);

    @Query(" select b from Booking b, Item i "
            + "where b.item = i.id and i.owner = ?1 "
            + "and b.start >= ?2 "
            + "order by b.start desc ")
    Page<Booking> findUserBookingsFuture(int userId, LocalDateTime now, Pageable pageable);

    List<Booking> findByItemAndEndIsBeforeOrderByEndDesc(int item, LocalDateTime end);

    List<Booking> findByItemAndStartIsAfterOrderByStartDesc(int item, LocalDateTime start);

}
