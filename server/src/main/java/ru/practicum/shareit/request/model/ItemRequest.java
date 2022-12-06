package ru.practicum.shareit.request.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull
    private String description;
    @Column(name = "requestor_id")
    private int requestor;
    @NonNull
    private LocalDateTime created;
}
