package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.RequestItemDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestInfoDto {
    private int id;

    @NotNull
    private String description;
    private int requestor;

    @NotNull
    private LocalDateTime created;
    List<RequestItemDto> items;

}
