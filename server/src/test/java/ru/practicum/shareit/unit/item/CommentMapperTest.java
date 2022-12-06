package ru.practicum.shareit.unit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentMapperTest {

    private UserDto authorDto;
    private ItemDto itemDto;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    public void createTestData() {
        User author = new User(2, "name2", "email2@mail.com");
        authorDto = new UserDto(author.getId(), author.getName(), author.getEmail());
        Item item = new Item(1, 1, "itemName", "itemDescription", true, 2);
        itemDto = new ItemDto(item.getId(), item.getOwner(),
                item.getName(), item.getDescription(), item.getAvailable(), item.getRequestId(), new ArrayList<>());
        LocalDateTime created = LocalDateTime.now();
        comment = new Comment(1, "text", item.getId(), author.getId(), created);
        commentDto = new CommentDto(comment.getId(), comment.getText(), item.getId(),
                author.getId(), author.getName(), comment.getCreated());
    }

    @Test
    public void shouldMapToCommentTest() {
        Comment result = CommentMapper.mapToComment(commentDto);
        assertThat(result).isEqualTo(comment);
    }

    @Test
    public void shouldMapToCommentDtoTest() {
        CommentDto result = CommentMapper.mapToCommentDto(comment, itemDto, authorDto);
        assertThat(result).isEqualTo(commentDto);
    }
}
