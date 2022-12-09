package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/items")
public class ItemController {
    private ItemService service;

    @Autowired
    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") int userId,
                              @RequestBody ItemDto itemDto) {
        log.info("POST /items/ request received");
        ItemDto createItemDto = service.createItem(new ItemDto(0, userId, itemDto.getName(),
                itemDto.getDescription(), itemDto.getAvailable(), itemDto.getRequestId(), new ArrayList<>()));
        log.info("POST /items/ request done");
        return createItemDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int itemId,
                              @RequestBody ItemDto itemDto) {
        log.info("PATCH /items/{} request received", itemId);
        ItemDto updateItemDto = service.updateItem(new ItemDto(itemId, userId, itemDto.getName(),
                itemDto.getDescription(), itemDto.getAvailable(), itemDto.getRequestId(), new ArrayList<>()));
        log.info("PATCH /items/{} request done", itemId);
        return updateItemDto;
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int itemId) {
        log.info("DELETE /items/{} request received", itemId);
        service.deleteItem(itemId, userId);
        log.info("DELETE /items/{} request done", itemId);
    }

    @GetMapping("/{itemId}")
    public ItemInfoDto getItem(@RequestHeader("X-Sharer-User-Id") int userId,
                           @PathVariable int itemId) {
        log.info("GET /items/{} request received", itemId);
        ItemInfoDto itemDto = service.getItemById(itemId, userId);
        log.info("GET /items/{} request done", itemId);
        return itemDto;
    }

    @GetMapping()
    public List<ItemInfoDto> getItems(@RequestHeader("X-Sharer-User-Id") int userId,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size) {
        log.info("GET /items/ request received");
        List<ItemInfoDto> itemsDto = service.getItemsByOwner(userId, from, size);
        log.info("GET /items/ request done");
        return itemsDto;
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearch(@RequestParam(required = false) String text,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        log.info("GET /items/search request received");
        List<ItemDto> itemsDto = service.getItemsBySearch(text, from, size);
        log.info("GET /items/search request done");
        return itemsDto;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                 @RequestBody CommentDto commentDto,
                                 @PathVariable int itemId) {
        log.info("POST /items/{}/comment request received", itemId);
        CommentDto comment = service.addComment(itemId, userId, commentDto);
        log.info("POST /items/{}/comment request done", itemId);
        return comment;
    }

}
