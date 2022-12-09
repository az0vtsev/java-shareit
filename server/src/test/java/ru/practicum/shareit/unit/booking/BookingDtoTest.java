package ru.practicum.shareit.unit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testBookingDtoToJson() throws IOException {
        LocalDateTime start = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        LocalDateTime end = start.plusDays(1);
        BookingDto bookingDto = new BookingDto(1, start, end, 1,
                1, BookingStatus.WAITING);
        JsonContent<BookingDto> result = json.write(bookingDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(bookingDto.getId());
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(bookingDto.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(bookingDto.getEnd().toString());
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(bookingDto.getItemId());
        assertThat(result).extractingJsonPathNumberValue("$.booker").isEqualTo(bookingDto.getBooker());
        assertThat(result).extractingJsonPathStringValue("$.status")
                .isEqualTo(bookingDto.getStatus().toString());
    }

    @Test
    void testJsonToUserDto() throws IOException {
        LocalDateTime start = LocalDateTime.parse("2022-10-30T19:53:50");
        LocalDateTime end = LocalDateTime.parse("2022-11-30T19:53:50");
        BookingDto bookingDto = new BookingDto(1, start, end, 1, 1, BookingStatus.WAITING);
        String stringJson = "{\n    \"id\": 1,\n" +
                "\"start\": \"2022-10-30T19:53:50\",\n" +
                "\"end\": \"2022-11-30T19:53:50\",\n" +
                "\"itemId\": 1,\n" +
                "\"booker\": 1,\n" +
                "\"status\": \"WAITING\"" +
                "\n}";
        BookingDto bookingDtoJson = json.parseObject(stringJson);
        assertThat(bookingDtoJson).isEqualTo(bookingDto);
    }

}
