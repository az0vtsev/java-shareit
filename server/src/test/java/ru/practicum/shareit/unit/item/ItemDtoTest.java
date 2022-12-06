package ru.practicum.shareit.unit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
public class ItemDtoTest {
    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testItemDtoToJson() throws IOException {
        ItemDto itemDto = new ItemDto(1, 1, "name", "description",
                true, 1, new ArrayList<>());
        JsonContent<ItemDto> result = json.write(itemDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemDto.getId());
        assertThat(result).extractingJsonPathNumberValue("$.owner").isEqualTo(itemDto.getOwner());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemDto.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(itemDto.getRequestId());
        assertThat(result).extractingJsonPathValue("$.comments").isEqualTo(itemDto.getComments());

    }

    @Test
    void testJsonToUserDto() throws IOException {
        ItemDto itemDto = new ItemDto(1, 1, "name", "description",
                true, 1, new ArrayList<>());
        String stringJson = "{\n    \"id\": 1,\n" +
                "\"owner\": 1,\n" +
                "\"name\": \"name\",\n" +
                "\"description\": \"description\",\n" +
                "\"available\": true,\n" +
                "\"requestId\": 1,\n" +
                "\"comments\": []" +
                "\n}";
        ItemDto itemDtoJson = json.parseObject(stringJson);
        assertThat(itemDtoJson).isEqualTo(itemDto);
    }
}
