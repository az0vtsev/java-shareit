package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestInfoDto createItemRequest(int userId, ItemRequestDto itemRequestDto);

    List<ItemRequestInfoDto> getUserItemRequests(int userId);

    List<ItemRequestInfoDto> getItemRequests(int from, int size, int userId);

    ItemRequestInfoDto getItemRequest(int userId, int requestId);

}
