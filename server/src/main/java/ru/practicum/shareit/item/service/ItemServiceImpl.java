package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.validator.Validator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemStorage storage;
    private UserStorage userStorage;
    private BookingStorage bookingStorage;

    private CommentStorage commentStorage;

    @Autowired
    public ItemServiceImpl(ItemStorage storage, UserStorage userStorage,
                           BookingStorage bookingStorage, CommentStorage commentStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
        this.bookingStorage = bookingStorage;
        this.commentStorage = commentStorage;
    }

    @Override
    public ItemDto createItem(ItemDto itemDto) {
        Validator.checkUserExistence(itemDto.getOwner(), userStorage);
        Item createItem = ItemMapper.mapToItem(itemDto);
        return ItemMapper.mapToItemDto(storage.save(createItem));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto) {
        Validator.ownerAuthorization(itemDto.getId(), itemDto.getOwner(), userStorage, storage);
        ItemDto oldItem = ItemMapper.mapToItemDto(storage.findById(itemDto.getId()).get());
        prepareToUpdate(itemDto, oldItem);
        Item updateItem = ItemMapper.mapToItem(itemDto);
        return ItemMapper.mapToItemDto(storage.save(updateItem));
    }

    @Override
    public void deleteItem(int itemId, int ownerId) {
        Validator.ownerAuthorization(itemId, ownerId, userStorage, storage);
        storage.deleteById(itemId);
    }

    @Override
    public ItemInfoDto getItemById(int id, int userId) {
        Validator.checkItemExistence(id, storage);
        Validator.checkUserExistence(userId, userStorage);
        LocalDateTime now = LocalDateTime.now();
        Item item = storage.findById(id).get();
        UserDto userDto;
        ItemDto itemDto = ItemMapper.mapToItemDto(item);
        ItemInfoDto itemInfoDto;
        if (item.getOwner() == userId) {
            userDto = UserMapper.mapToUserDto(userStorage.findById(userId).get());
            List<Booking> bookingsPrev = bookingStorage
                    .findByItemAndEndIsBeforeOrderByEndDesc(item.getId(), now);
            List<Booking> bookingsNext = bookingStorage
                    .findByItemAndStartIsAfterOrderByStartDesc(item.getId(), now);
            BookingItemDto bookingPrevDto = !bookingsPrev.isEmpty()
                    ? BookingMapper.mapToBookingItemDto(bookingsPrev.get(0), itemDto,
                            bookingsPrev.get(0).getBooker()) : null;
            BookingItemDto bookingNextDto = !bookingsNext.isEmpty()
                    ? BookingMapper.mapToBookingItemDto(bookingsNext.get(0), itemDto,
                            bookingsNext.get(0).getBooker()) : null;
            itemInfoDto = ItemMapper.mapToItemInfoDto(item, userDto, bookingPrevDto, bookingNextDto);
        } else {
            userDto = UserMapper.mapToUserDto(userStorage.findById(item.getOwner()).get());
            itemInfoDto = ItemMapper.mapToItemInfoDto(item, userDto, null, null);
        }
        itemInfoDto.setComments(getCommentsByItemId(id));
        return itemInfoDto;
    }

    @Override
    public List<ItemInfoDto> getItemsByOwner(int ownerId, int from, int size) {
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        LocalDateTime now = LocalDateTime.now();
        Page<Item> items = storage.findByOwnerOrderById(ownerId, pageRequest);
        List<ItemInfoDto> itemsDto = new ArrayList<>();
        UserDto userDto = UserMapper.mapToUserDto(userStorage.findById(ownerId).get());
        for (Item item : items.getContent()) {
            ItemDto itemDto = ItemMapper.mapToItemDto(item);
            List<Booking> bookingsPrev = bookingStorage
                    .findByItemAndEndIsBeforeOrderByEndDesc(item.getId(), now);
            List<Booking> bookingsNext = bookingStorage
                    .findByItemAndStartIsAfterOrderByStartDesc(item.getId(), now);
            BookingItemDto bookingPrevDto = !bookingsPrev.isEmpty()
                    ? BookingMapper.mapToBookingItemDto(bookingsPrev.get(0), itemDto,
                            bookingsPrev.get(0).getBooker()) : null;
            BookingItemDto bookingNextDto = !bookingsNext.isEmpty()
                    ? BookingMapper.mapToBookingItemDto(bookingsNext.get(0), itemDto,
                            bookingsNext.get(0).getBooker()) : null;
            ItemInfoDto itemInfoDto = ItemMapper.mapToItemInfoDto(item, userDto, bookingPrevDto,
                    bookingNextDto);
            itemInfoDto.setComments(getCommentsByItemId(item.getId()));
            itemsDto.add(itemInfoDto);
        }
        return itemsDto;
    }

    @Override
    public CommentDto addComment(int itemId, int userId, CommentDto commentDto) {
        Validator.checkUserExistence(userId, userStorage);
        Validator.checkItemExistence(itemId, storage);
        Validator.checkUserBookingItem(userId, itemId, bookingStorage);
        UserDto userDto = UserMapper.mapToUserDto(userStorage.findById(userId).get());
        ItemDto itemDto = ItemMapper.mapToItemDto(storage.findById(itemId).get());
        commentDto.setItem(itemId);
        commentDto.setAuthorId(userId);
        commentDto.setCreated(LocalDateTime.now());
        Comment createComment = CommentMapper.mapToComment(commentDto);
        return CommentMapper.mapToCommentDto(commentStorage.save(createComment), itemDto, userDto);
    }

    @Override
    public List<ItemDto> getItemsBySearch(String text, int from, int size) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return getItemsByText(text, from, size);
    }

    private List<ItemDto> getItemsByText(String text, int from, int size) {
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        return storage.search(text, pageRequest)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    private void prepareToUpdate(ItemDto updateItem, ItemDto oldItem) {
        if (updateItem.getName() == null) {
            updateItem.setName(oldItem.getName());
        }
        if (updateItem.getDescription() == null) {
            updateItem.setDescription(oldItem.getDescription());
        }
        if (updateItem.getAvailable() == null) {
            updateItem.setAvailable(oldItem.getAvailable());
        }
    }

    private List<CommentDto> getCommentsByItemId(int itemId) {
        List<Comment> comments = commentStorage.findByItemOrderByCreatedDesc(itemId);
        List<CommentDto> commentsDto = new ArrayList<>();
        ItemDto itemDto = ItemMapper.mapToItemDto(storage.findById(itemId).get());
        for (Comment comment : comments) {
            UserDto userDto = UserMapper.mapToUserDto(userStorage.findById(comment.getAuthor()).get());
            CommentDto commentDto = CommentMapper.mapToCommentDto(comment, itemDto, userDto);
            commentsDto.add(commentDto);
        }
        return commentsDto;
    }
}
