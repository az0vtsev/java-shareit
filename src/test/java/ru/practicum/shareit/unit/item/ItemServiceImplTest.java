package ru.practicum.shareit.unit.item;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.NotValidAuthorCommentException;
import ru.practicum.shareit.exception.NotValidUserException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith({MockitoExtension.class})
public class ItemServiceImplTest {

    private User user1;
    private User user2;
    private UserDto userDto1;
    private Item item1;
    private Item item2;
    private ItemDto itemDto1;
    private ItemDto itemDto2;

    private ItemService service;
    @Mock
    private ItemStorage storage;
    @Mock
    private UserStorage userStorage;
    @Mock
    private BookingStorage bookingStorage;
    @Mock
    private CommentStorage commentStorage;

    @BeforeEach
    public void createData() {
        service = new ItemServiceImpl(storage, userStorage, bookingStorage, commentStorage);
        user1 = new User(1, "name1", "email1@mail.com");
        user2 = new User(2,"name2", "email2@mail.com");
        userDto1 = new UserDto(user1.getId(), user1.getName(), user1.getEmail());
        item1 = new Item(1, user1.getId(), "itemName1", "itemDescription1",
                true, null);
        item2 = new Item(2, user2.getId(), "itemName2", "itemDescription2",
                true, null);
        itemDto1 = new ItemDto(item1.getId(), item1.getOwner(), item1.getName(), item1.getDescription(),
                item1.getAvailable(), item1.getRequestId(), new ArrayList<>());
        itemDto2 = new ItemDto(item2.getId(), item2.getOwner(), item2.getName(), item2.getDescription(),
                item2.getAvailable(), item2.getRequestId(), new ArrayList<>());
    }

    @Test
    public void shouldSaveAndReturnItemDto() {
        Mockito.when(storage.save(any(Item.class))).thenReturn(item1);
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        ItemDto itemDto = service.createItem(itemDto1);
        assertThat(itemDto).isEqualTo(itemDto1);
        Mockito.verify(userStorage, Mockito.times(1)).existsById(user1.getId());
        Mockito.verify(storage, Mockito.times(1)).save(item1);
    }

    @Test
    public void shouldUpdateNameAndReturnItemDto() {
        Item updateItem = new Item(item1.getId(), item1.getOwner(), "update_itemName1", item1.getDescription(),
                item1.getAvailable(), item1.getRequestId());
        ItemDto updateItemDto = new ItemDto(updateItem.getId(), updateItem.getOwner(), updateItem.getName(),
                updateItem.getDescription(), updateItem.getAvailable(), updateItem.getRequestId(), new ArrayList<>());
        Optional<Item> result = Optional.of(item1);

        Mockito.when(storage.existsById(anyInt())).thenReturn(true);
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        Mockito.when(storage.findById(anyInt())).thenReturn(result);
        Mockito.when(storage.save(any(Item.class))).thenReturn(updateItem);

        ItemDto updatedItemDto = service.updateItem(updateItemDto);
        assertThat(updatedItemDto).isEqualTo(updateItemDto);

        Mockito.verify(storage, Mockito.times(1)).existsById(updateItem.getId());
        Mockito.verify(userStorage, Mockito.times(1)).existsById(updateItem.getOwner());
        Mockito.verify(storage, Mockito.times(2)).findById(updateItem.getId());
        Mockito.verify(storage, Mockito.times(1)).save(updateItem);
    }

    @Test
    public void shouldUpdateDescriptionAndReturnItemDto() {
        Item updateItem = new Item(item1.getId(), item1.getOwner(), item1.getName(),
                "update_itemDescription1", item1.getAvailable(), item1.getRequestId());
        ItemDto updateItemDto = new ItemDto(updateItem.getId(), updateItem.getOwner(), updateItem.getName(),
                updateItem.getDescription(), updateItem.getAvailable(), updateItem.getRequestId(), new ArrayList<>());
        Optional<Item> result = Optional.of(item1);

        Mockito.when(storage.existsById(anyInt())).thenReturn(true);
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        Mockito.when(storage.findById(anyInt())).thenReturn(result);
        Mockito.when(storage.save(any(Item.class))).thenReturn(updateItem);

        ItemDto updatedItemDto = service.updateItem(updateItemDto);
        assertThat(updatedItemDto).isEqualTo(updateItemDto);

        Mockito.verify(storage, Mockito.times(1)).existsById(updateItem.getId());
        Mockito.verify(userStorage, Mockito.times(1)).existsById(updateItem.getOwner());
        Mockito.verify(storage, Mockito.times(2)).findById(updateItem.getId());
        Mockito.verify(storage, Mockito.times(1)).save(updateItem);
    }

    @Test
    public void shouldUpdateAvailableAndReturnItemDto() {
        Item updateItem = new Item(item1.getId(), item1.getOwner(), item1.getName(),
                item1.getDescription(), false, item1.getRequestId());
        ItemDto updateItemDto = new ItemDto(updateItem.getId(), updateItem.getOwner(), updateItem.getName(),
                updateItem.getDescription(), updateItem.getAvailable(), updateItem.getRequestId(), new ArrayList<>());
        Optional<Item> result = Optional.of(item1);

        Mockito.when(storage.existsById(anyInt())).thenReturn(true);
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        Mockito.when(storage.findById(anyInt())).thenReturn(result);
        Mockito.when(storage.save(any(Item.class))).thenReturn(updateItem);

        ItemDto updatedItemDto = service.updateItem(updateItemDto);
        assertThat(updatedItemDto).isEqualTo(updateItemDto);

        Mockito.verify(storage, Mockito.times(1)).existsById(updateItem.getId());
        Mockito.verify(userStorage, Mockito.times(1)).existsById(updateItem.getOwner());
        Mockito.verify(storage, Mockito.times(2)).findById(updateItem.getId());
        Mockito.verify(storage, Mockito.times(1)).save(updateItem);
    }

    @Test
    public void shouldDeleteAndAutorizeOwner() {
        Optional<Item> result = Optional.of(item1);

        Mockito.when(storage.existsById(anyInt())).thenReturn(true);
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        Mockito.when(storage.findById(anyInt())).thenReturn(result);

        service.deleteItem(item1.getId(), user1.getId());

        Mockito.verify(storage, Mockito.times(1)).existsById(item1.getId());
        Mockito.verify(userStorage, Mockito.times(1)).existsById(item1.getOwner());
        Mockito.verify(storage, Mockito.times(1)).findById(item1.getId());
    }

    @Test
    public void shouldReturnItemIfUserNotOwner() {
        Optional<Item> result = Optional.of(item1);
        Optional<User> userResult = Optional.of(user1);
        Mockito.when(storage.existsById(anyInt())).thenReturn(true);
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        Mockito.when(storage.findById(anyInt())).thenReturn(result);
        Mockito.when(userStorage.findById(anyInt())).thenReturn(userResult);
        Mockito.when(commentStorage.findByItemOrderByCreatedDesc(anyInt()))
                .thenReturn(List.of());

        ItemInfoDto itemInfoDto = new ItemInfoDto(item1.getId(), userDto1, item1.getName(),
                item1.getDescription(), item1.getAvailable(), item1.getRequestId(), new ArrayList<>(),
                null, null);
        ItemInfoDto itemResult = service.getItemById(item1.getId(), user2.getId());
        assertThat(itemResult).isEqualTo(itemInfoDto);

        Mockito.verify(storage, Mockito.times(1)).existsById(item1.getId());
        Mockito.verify(storage, Mockito.times(2)).findById(item1.getId());
        Mockito.verify(userStorage, Mockito.times(1)).existsById(user2.getId());
        Mockito.verify(userStorage, Mockito.times(1)).findById(item1.getOwner());
        Mockito.verify(commentStorage, Mockito.times(1))
                .findByItemOrderByCreatedDesc(item1.getId());
    }

    @Test
    public void shouldReturnItemIfUserNotOwnerWithComment() {
        Optional<Item> result = Optional.of(item1);
        Optional<User> user1Result = Optional.of(user1);
        Optional<User> user2Result = Optional.of(user2);
        Mockito.when(storage.existsById(anyInt())).thenReturn(true);
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        Mockito.when(storage.findById(anyInt())).thenReturn(result);
        Mockito.when(userStorage.findById(1)).thenReturn(user1Result);
        Mockito.when(userStorage.findById(2)).thenReturn(user2Result);
        LocalDateTime created = LocalDateTime.now();
        Comment comment = new Comment(1,"text", item1.getId(), user2.getId(), created);
        CommentDto commentDto = new CommentDto(comment.getId(), comment.getText(),
                comment.getItem(), comment.getAuthor(), user2.getName(), comment.getCreated());
        Mockito.when(commentStorage.findByItemOrderByCreatedDesc(anyInt()))
                .thenReturn(List.of(comment));

        ItemInfoDto itemInfoDto = new ItemInfoDto(item1.getId(), userDto1, item1.getName(),
                item1.getDescription(), item1.getAvailable(), item1.getRequestId(), new ArrayList<>(),
                null, null);
        itemInfoDto.setComments(List.of(commentDto));
        ItemInfoDto itemResult = service.getItemById(item1.getId(), user2.getId());
        assertThat(itemResult).isEqualTo(itemInfoDto);

        Mockito.verify(storage, Mockito.times(1)).existsById(item1.getId());
        Mockito.verify(storage, Mockito.times(2)).findById(item1.getId());
        Mockito.verify(userStorage, Mockito.times(1)).existsById(user2.getId());
        Mockito.verify(userStorage, Mockito.times(1)).findById(item1.getOwner());
        Mockito.verify(commentStorage, Mockito.times(1))
                .findByItemOrderByCreatedDesc(item1.getId());
    }

    @Test
    public void shouldReturnItemIfUserIsOwner() {
        Optional<Item> result = Optional.of(item1);
        Optional<User> userResult = Optional.of(user1);
        Mockito.when(storage.existsById(anyInt())).thenReturn(true);
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        Mockito.when(storage.findById(anyInt())).thenReturn(result);
        Mockito.when(userStorage.findById(anyInt())).thenReturn(userResult);

        LocalDateTime startPrev = LocalDateTime.now().minusDays(3);
        LocalDateTime endPrev = startPrev.plusDays(1);
        LocalDateTime startNext = LocalDateTime.now().plusDays(3);
        LocalDateTime endNext = startNext.plusDays(1);
        Booking bookingPrev = new Booking(1, startPrev, endPrev, item1.getId(),
                user2.getId(), BookingStatus.APPROVED);
        Booking bookingNext = new Booking(2, startNext, endNext, item1.getId(),
                user2.getId(), BookingStatus.APPROVED);
        Mockito.when(commentStorage.findByItemOrderByCreatedDesc(anyInt()))
                .thenReturn(List.of());

        Mockito.when(bookingStorage
                .findByItemAndStartIsAfterOrderByStartDesc(anyInt(), any(LocalDateTime.class)))
                .thenReturn(List.of(bookingNext));
        Mockito.when(bookingStorage
                .findByItemAndEndIsBeforeOrderByEndDesc(anyInt(), any(LocalDateTime.class)))
                .thenReturn(List.of(bookingPrev));
        BookingItemDto bookingPrevDto = new BookingItemDto(bookingPrev.getId(), bookingPrev.getStart(),
                bookingPrev.getEnd(), itemDto1, bookingPrev.getBooker(), bookingPrev.getStatus());
        BookingItemDto bookingNextDto = new BookingItemDto(bookingNext.getId(), bookingNext.getStart(),
                bookingNext.getEnd(), itemDto1, bookingNext.getBooker(), bookingNext.getStatus());
        ItemInfoDto itemInfoDto = new ItemInfoDto(item1.getId(), userDto1, item1.getName(),
                item1.getDescription(), item1.getAvailable(), item1.getRequestId(), new ArrayList<>(),
                bookingPrevDto, bookingNextDto);
        ItemInfoDto itemResult = service.getItemById(item1.getId(), user1.getId());
        assertThat(itemResult).isEqualTo(itemInfoDto);

        Mockito.verify(storage, Mockito.times(1)).existsById(item1.getId());
        Mockito.verify(storage, Mockito.times(2)).findById(item1.getId());
        Mockito.verify(userStorage, Mockito.times(1)).existsById(user1.getId());
        Mockito.verify(userStorage, Mockito.times(1)).findById(item1.getOwner());
        Mockito.verify(commentStorage, Mockito.times(1))
                .findByItemOrderByCreatedDesc(item1.getId());
    }

    @Test
    public void shouldReturnEmptyList() {
        List<ItemDto> list = service.getItemsBySearch("", 0, 10);
        assertThat(list.isEmpty()).isEqualTo(true);
    }

    @Test
    public void shouldReturnItemsByText() {
        List<ItemDto> list = List.of(itemDto1, itemDto2);
        Page<Item> page = new PageImpl<>(List.of(item1, item2));
        Mockito.when(storage.search(anyString(),any(PageRequest.class)))
                .thenReturn(page);
        List<ItemDto> result = service.getItemsBySearch("text", 0, 10);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualTo(list.get(0));
        assertThat(result.get(1)).isEqualTo(list.get(1));
        Mockito.verify(storage, Mockito.times(1))
                .search("text", PageRequest.of(0,10));
    }

    @Test
    public void shouldAddComment() {
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        Mockito.when(storage.existsById(anyInt())).thenReturn(true);
        Mockito.when(bookingStorage
                .findByBookerAndItemAndEndIsBefore(anyInt(), anyInt(), any(LocalDateTime.class)))
                .thenReturn(List.of(new Booking()));
        Optional<User> userResult = Optional.of(user1);
        Optional<Item> itemResult = Optional.of(item2);
        Mockito.when(userStorage.findById(anyInt())).thenReturn(userResult);
        Mockito.when(storage.findById(anyInt())).thenReturn(itemResult);
        LocalDateTime created = LocalDateTime.now();
        Comment comment = new Comment(1, "text", item2.getId(), user1.getId(), created);
        CommentDto commentDto = new CommentDto(comment.getId(), comment.getText(), comment.getItem(),
                user1.getId(), user1.getName(), comment.getCreated());
        Mockito.when(commentStorage.save(any(Comment.class))).thenReturn(comment);
        CommentDto result = service.addComment(item2.getId(), user1.getId(), commentDto);
        assertThat(result.getId()).isEqualTo(commentDto.getId());
        assertThat(result.getText()).isEqualTo(commentDto.getText());
        assertThat(result.getItem()).isEqualTo(commentDto.getItem());
        assertThat(result.getAuthorId()).isEqualTo(commentDto.getAuthorId());
    }

    @Test
    public void shouldReturnItemsByOwner() {
        Item item3 = new Item(3, user1.getId(), "itemName3", "itemDescription3",
                true, null);
        Optional<User> userResult = Optional.of(user1);
        Page<Item> page = new PageImpl<>(List.of(item1, item3));
        Mockito.when(storage.findByOwnerOrderById(anyInt(), any(PageRequest.class))).thenReturn(page);
        Mockito.when(userStorage.findById(anyInt())).thenReturn(userResult);

        LocalDateTime startPrev = LocalDateTime.now().minusDays(3);
        LocalDateTime endPrev = startPrev.plusDays(1);
        LocalDateTime startNext = LocalDateTime.now().plusDays(3);
        LocalDateTime endNext = startNext.plusDays(1);
        Booking bookingPrev = new Booking(1, startPrev, endPrev, item1.getId(),
                user2.getId(), BookingStatus.APPROVED);
        Booking bookingNext = new Booking(2, startNext, endNext, item1.getId(),
                user2.getId(), BookingStatus.APPROVED);
        Mockito.when(commentStorage.findByItemOrderByCreatedDesc(anyInt()))
                .thenReturn(List.of());

        Mockito.when(bookingStorage
                        .findByItemAndStartIsAfterOrderByStartDesc(eq(1), any(LocalDateTime.class)))
                .thenReturn(List.of(bookingNext));
        Mockito.when(bookingStorage
                        .findByItemAndEndIsBeforeOrderByEndDesc(eq(1), any(LocalDateTime.class)))
                .thenReturn(List.of(bookingPrev));
        BookingItemDto bookingPrevDto = new BookingItemDto(bookingPrev.getId(), bookingPrev.getStart(),
                bookingPrev.getEnd(), itemDto1, bookingPrev.getBooker(), bookingPrev.getStatus());
        BookingItemDto bookingNextDto = new BookingItemDto(bookingNext.getId(), bookingNext.getStart(),
                bookingNext.getEnd(), itemDto1, bookingNext.getBooker(), bookingNext.getStatus());
        ItemInfoDto itemInfoDto1 = new ItemInfoDto(item1.getId(), userDto1, item1.getName(),
                item1.getDescription(), item1.getAvailable(), item1.getRequestId(), new ArrayList<>(),
                bookingPrevDto, bookingNextDto);

        Mockito.when(bookingStorage
                        .findByItemAndStartIsAfterOrderByStartDesc(eq(3), any(LocalDateTime.class)))
                .thenReturn(List.of());
        Mockito.when(bookingStorage
                        .findByItemAndEndIsBeforeOrderByEndDesc(eq(3), any(LocalDateTime.class)))
                .thenReturn(List.of());
        ItemInfoDto itemInfoDto3 = new ItemInfoDto(item3.getId(), userDto1, item3.getName(),
                item3.getDescription(), item3.getAvailable(), item3.getRequestId(), new ArrayList<>(),
                null, null);

        Mockito.when(commentStorage.findByItemOrderByCreatedDesc(anyInt()))
                .thenReturn(new ArrayList<>());
        Optional<Item> item1Result = Optional.of(item1);
        Mockito.when(storage.findById(eq(1))).thenReturn(item1Result);
        Optional<Item> item3Result = Optional.of(item3);
        Mockito.when(storage.findById(eq(3))).thenReturn(item3Result);

        List<ItemInfoDto> itemResults = service.getItemsByOwner(user1.getId(), 0, 10);

        assertThat(itemResults.size()).isEqualTo(2);
        assertThat(itemResults.get(0)).isEqualTo(itemInfoDto1);
        assertThat(itemResults.get(1)).isEqualTo(itemInfoDto3);
    }

    @Test
    public void shouldThrowNotValidAuthorCommentException() {
        Mockito.when(bookingStorage.findByBookerAndItemAndEndIsBefore(anyInt(), anyInt(), any(LocalDateTime.class)))
                .thenReturn(List.of());
        Mockito.when(storage.existsById(anyInt())).thenReturn(true);
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        assertThrows(NotValidAuthorCommentException.class,
                () -> service.addComment(1, 2,new CommentDto()));
    }

    @Test
    public void shouldThrowNotValidUserException() {
        Mockito.when(userStorage.existsById(anyInt())).thenReturn(true);
        Mockito.when(storage.existsById(anyInt())).thenReturn(true);
        Mockito.when(storage.findById(anyInt())).thenReturn(Optional.of(item1));
        itemDto1.setOwner(2);
        assertThrows(NotValidUserException.class,
                () -> service.updateItem(itemDto1));
    }
}
