package ru.practicum.shareit.unit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentDtoTest {
    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void testCommentDtoToJson() throws IOException {
        LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        CommentDto commentDto = new CommentDto(1, "text", 1, 1, "name", created);
        JsonContent<CommentDto> result = json.write(commentDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(commentDto.getId());
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(commentDto.getText());
        assertThat(result).extractingJsonPathNumberValue("$.authorId").isEqualTo(commentDto.getAuthorId());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(commentDto.getAuthorName());
    }

    @Test
    void testJsonToUserDto() throws IOException {
        LocalDateTime created = LocalDateTime.parse("2022-10-30T19:53:50");
        CommentDto commentDto = new CommentDto(1, "text", 1, 1, "name", created);
        String stringJson = "{\n    \"id\": 1,\n" +
                "\"text\": \"text\",\n" +
                "\"item\": 1,\n" +
                "\"authorId\": 1,\n" +
                "\"authorName\": \"name\",\n" +
                "\"created\": \"2022-10-30T19:53:50\"" +
                "\n}";
       CommentDto commentDtoJson = json.parseObject(stringJson);
        assertThat(commentDtoJson).isEqualTo(commentDto);
    }
}
