package ru.practicum.shareit.unit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRequestMapperTest {
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    public void createTestData() {
        itemRequest = new ItemRequest(1, "itemDescription", 2, LocalDateTime.now());
        itemRequestDto = new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestor(),
                itemRequest.getCreated()
        );
    }

    @Test
    public void shouldMapToItemRequestTest() {
        ItemRequest result = ItemRequestMapper.mapToItemRequest(itemRequestDto);
        assertThat(result).isEqualTo(itemRequest);
    }

    @Test
    public void shouldMapToItemRequestDtoTest() {
        ItemRequestDto result = ItemRequestMapper.mapToItemRequestDto(itemRequest);
        assertThat(result).isEqualTo(itemRequestDto);
    }

    @Test
    public void shouldMapToItemRequestInfoDtoTest() {
        ItemRequestInfoDto itemRequestInfoDto = new ItemRequestInfoDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestor(),
                itemRequest.getCreated(),
                new ArrayList<>()
        );
        ItemRequestInfoDto result = ItemRequestMapper.mapToItemRequestInfoDto(itemRequest);
        assertThat(result).isEqualTo(itemRequestInfoDto);
    }
}
