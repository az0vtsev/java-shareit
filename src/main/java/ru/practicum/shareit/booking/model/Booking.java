package ru.practicum.shareit.booking.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;

    @Column(name = "start_date")
    @NonNull
    private LocalDateTime start;

    @Column(name = "end_date")
    @NonNull
    private LocalDateTime end;

    @Column(name = "item_id")
    @NonNull
    private Integer item;

    @Column(name = "booker_id")
    @NonNull
    private Integer booker;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
