package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String text;

    @Column(name = "item_id")
    @NonNull
    private Integer item;

    @Column(name = "author_id")
    @NonNull
    private Integer author;

    @NonNull
    private LocalDateTime created;

}
