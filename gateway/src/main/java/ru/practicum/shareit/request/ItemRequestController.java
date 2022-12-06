package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;


    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                            @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("POST /requests request received");
        return  itemRequestClient.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItemRequests(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("GET /requests request received");
        return itemRequestClient.getUserItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequests(@RequestHeader("X-Sharer-User-Id") int userId,
                                                    @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                    @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET /requests/all?from={}&size={} request received", from, size);
        return itemRequestClient.getItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @PathVariable int requestId) {
        log.info("GET /requests/{} request received", requestId);
        return itemRequestClient.getItemRequest(userId, requestId);
    }

}
