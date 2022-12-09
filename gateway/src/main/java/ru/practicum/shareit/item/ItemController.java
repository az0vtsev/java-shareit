package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@Positive @RequestHeader("X-Sharer-User-Id") int userId,
                                              @Valid @RequestBody ItemDto itemDto) {
        log.info("POST /items/ request");
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@Positive @RequestHeader("X-Sharer-User-Id") int userId,
                                             @Positive @PathVariable int itemId,
                                             @RequestBody ItemDto itemDto) {
        log.info("PATCH /items/{} request received", itemId);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@Positive @RequestHeader("X-Sharer-User-Id") int userId,
                                             @Positive @PathVariable int itemId) {
        log.info("DELETE /items/{} request received", itemId);
        return itemClient.deleteItem(userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@Positive @RequestHeader("X-Sharer-User-Id") int userId,
                                          @Positive @PathVariable int itemId) {
        log.info("GET /items/{} request received", itemId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping()
    public ResponseEntity<Object> getItems(@Positive @RequestHeader("X-Sharer-User-Id") int userId,
                                      @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                      @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET /items/ request received");
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsBySearch(@Positive @RequestHeader("X-Sharer-User-Id") int userId,
                                          @RequestParam(required = false) String text,
                                          @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                          @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET /items/search?text={}&from={}&size{} request received", text, from, size);
        return itemClient.getItemsBySearch(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Positive @RequestHeader("X-Sharer-User-Id") int userId,
                                             @Valid @RequestBody CommentDto commentDto,
                                             @Positive @PathVariable int itemId) {
        log.info("POST /items/{}/comment request received", itemId);
        return itemClient.addComment(userId, itemId, commentDto);
    }

}
