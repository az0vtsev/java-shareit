package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotItemOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private ItemService service;
    @Autowired
    public ItemController(@Qualifier("itemServiceImpl") ItemService service) {
        this.service = service;
    }
    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") int userId,
                              @Valid @RequestBody ItemDto itemDto) throws NotFoundException {
        log.info("POST /items/ request received");
        ItemDto createItemDto = service.createItem(new ItemDto(0, userId, itemDto.getName()
                , itemDto.getDescription(), itemDto.getAvailable(), itemDto.getRequest()));
        log.info("POST /items/ request done");
        return createItemDto;
    }
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int itemId,
                              @RequestBody ItemDto itemDto)
            throws NotFoundException, NotItemOwnerException {
        log.info("PATCH /items/{} request received", itemId);
        ItemDto updateItemDto = service.updateItem(new ItemDto(itemId, userId, itemDto.getName()
                , itemDto.getDescription(), itemDto.getAvailable(), itemDto.getRequest()));
        log.info("PATCH /items/{} request done", itemId);
        return updateItemDto;
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int itemId)
            throws NotFoundException, NotItemOwnerException {
        log.info("DELETE /items/{} request received", itemId);
        service.deleteItem(itemId, userId);
        log.info("DELETE /items/{} request done", itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable int itemId) throws NotFoundException {
        log.info("GET /items/{} request received", itemId);
        ItemDto itemDto = service.getItemById(itemId);
        log.info("GET /items/{} request done", itemId);
        return itemDto;
    }

    @GetMapping()
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("GET /items/ request received");
        List<ItemDto> itemsDto = service.getItemsByOwner(userId);
        log.info("GET /items/ request done");
        return itemsDto;
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearch(@RequestParam(required = false) String text) {
        log.info("GET /items/search request received");
        List<ItemDto> itemsDto = service.getItemsBySearch(text);
        log.info("GET /items/search request done");
        return itemsDto;
    }
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Entity not found",
                "errorMessage", e.getMessage()
        );
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Data isn't valid",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler(NotItemOwnerException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> handleNotItemOwnerException(final NotItemOwnerException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Not item owner",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(final RuntimeException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Runtime Exception",
                "errorMessage", e.getMessage()
        );
    }
}
