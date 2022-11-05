package ru.practicum.shareit.unit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testItemDtoToJson() throws IOException {
        LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        ItemRequestDto itemRequestDto = new ItemRequestDto(1, "description", 1, created);
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemRequestDto.getId());
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemRequestDto.getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.requestor")
                .isEqualTo(itemRequestDto.getRequestor());
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo(itemRequestDto.getCreated().toString());

    }

    @Test
    void testJsonToUserDto() throws IOException {
        LocalDateTime created = LocalDateTime.parse("2022-10-30T19:53:50");
        ItemRequestDto itemRequestDto = new ItemRequestDto(1, "description", 1, created);
        String stringJson = "{\n    \"id\": 1,\n" +
                "\"description\": \"description\",\n" +
                "\"requestor\": 1,\n" +
                "\"created\": \"2022-10-30T19:53:50\"" +
                "\n}";
        ItemRequestDto itemRequestDtoJson = json.parseObject(stringJson);
        assertThat(itemRequestDtoJson).isEqualTo(itemRequestDto);
    }
}
