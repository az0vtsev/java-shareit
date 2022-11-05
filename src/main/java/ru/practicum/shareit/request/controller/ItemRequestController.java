package ru.practicum.shareit.request.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestInfoDto createItemRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                            @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("POST /requests request received");
        ItemRequestInfoDto itemRequest = itemRequestService.createItemRequest(userId, itemRequestDto);
        log.info("POST /requests request done");
        return  itemRequest;
    }

    @GetMapping
    public List<ItemRequestInfoDto> getUserItemRequests(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("GET /requests request received");
        List<ItemRequestInfoDto> itemRequests = itemRequestService.getUserItemRequests(userId);
        log.info("GET /requests request done");
        return itemRequests;
    }

    @GetMapping("/all")
    public List<ItemRequestInfoDto> getItemRequests(@RequestHeader("X-Sharer-User-Id") int userId,
                                                    @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                    @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET /requests/all?from{}&size={} request received", from, size);
        List<ItemRequestInfoDto> itemRequests = itemRequestService.getItemRequests(from, size, userId);
        log.info("GET /requests/all?from{}&size={} request done", from, size);
        return itemRequests;
    }

    @GetMapping("/{requestId}")
    public ItemRequestInfoDto getItemRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @PathVariable int requestId) {
        log.info("GET /requests/{} request received", requestId);
        ItemRequestInfoDto itemRequestInfoDto = itemRequestService.getItemRequest(userId, requestId);
        log.info("GET /requests/{} request done", requestId);
        return itemRequestInfoDto;
    }

}
