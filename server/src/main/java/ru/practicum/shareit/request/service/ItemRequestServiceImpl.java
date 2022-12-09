package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.validator.Validator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class ItemRequestServiceImpl implements ItemRequestService {

    private ItemRequestStorage storage;
    private UserStorage userStorage;
    private ItemStorage itemStorage;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestStorage storage, UserStorage userStorage,
                                  ItemStorage itemStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
        this.itemStorage = itemStorage;
    }

    @Override
    public ItemRequestInfoDto createItemRequest(int userId, ItemRequestDto itemRequestDto) {
        Validator.checkUserExistence(userId, userStorage);
        itemRequestDto.setRequestor(userId);
        itemRequestDto.setCreated(LocalDateTime.now());
        ItemRequest createItemRequest = ItemRequestMapper.mapToItemRequest(itemRequestDto);
        return ItemRequestMapper.mapToItemRequestInfoDto(storage.save(createItemRequest));
    }

    @Override
    public List<ItemRequestInfoDto> getUserItemRequests(int userId) {
        Validator.checkUserExistence(userId, userStorage);
        List<ItemRequest> itemRequests = storage.findByRequestorOrderByCreatedDesc(userId);
        List<ItemRequestInfoDto> itemRequestsInfoDto = new ArrayList<>();
        for (ItemRequest itemRequest: itemRequests) {
            ItemRequestInfoDto itemRequestInfoDto = ItemRequestMapper.mapToItemRequestInfoDto(itemRequest);
            itemRequestInfoDto.setItems(getRequestItems(itemRequest.getId()));
            itemRequestsInfoDto.add(itemRequestInfoDto);
        }
        return itemRequestsInfoDto;
    }

    @Override
    public List<ItemRequestInfoDto> getItemRequests(int from, int size, int userId) {
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        List<ItemRequestInfoDto> itemRequestsInfoDto =
                storage.findByRequestorIsNotOrderByCreatedDesc(userId, pageRequest)
                .stream()
                .map(ItemRequestMapper::mapToItemRequestInfoDto)
                .collect(Collectors.toList());
        for (ItemRequestInfoDto itemRequestInfoDto : itemRequestsInfoDto) {
            itemRequestInfoDto.setItems(getRequestItems(itemRequestInfoDto.getId()));
        }
        return itemRequestsInfoDto;
    }

    @Override
    public ItemRequestInfoDto getItemRequest(int userId, int requestId) {
        Validator.checkItemRequestExistence(requestId, storage);
        Validator.checkUserExistence(userId, userStorage);
        ItemRequestInfoDto itemRequestInfoDto =
                ItemRequestMapper.mapToItemRequestInfoDto(storage.findById(requestId).get());
        itemRequestInfoDto.setItems(getRequestItems(requestId));
        return itemRequestInfoDto;
    }

    private List<RequestItemDto> getRequestItems(int requestId) {
        List<Item> items = itemStorage.findByRequestId(requestId);
        return items.stream()
                .map(ItemMapper::mapToRequestItemDto)
                .collect(Collectors.toList());
    }
}
