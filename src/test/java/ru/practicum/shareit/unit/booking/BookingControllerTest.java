package ru.practicum.shareit.unit.booking;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ErrorHandler;
import ru.practicum.shareit.exception.UnsupportedBookingStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    private User user1;
    private User user2;

    private Item item1;
    private Booking booking1;
    private BookingDto bookingDto1;
    private BookingInfoDto bookingInfoDto1;
    private BookingInfoDto bookingInfoDto2;


    @Mock
    private BookingService service;

    @InjectMocks
    private BookingController controller;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private MockMvc mvc;

    @BeforeEach
    public void createData() {
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(ErrorHandler.class)
                .build();
        user1 = new User(1, "name1", "email1@mail.com");
        user2 = new User(2,"name2", "email2@mail.com");
        UserDto userDto2 = UserMapper.mapToUserDto(user2);
        item1 = new Item(1, user1.getId(), "itemName1", "itemDescription1",
                true, null);
        ItemDto itemDto1 = ItemMapper.mapToItemDto(item1);
        booking1 = new Booking(1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                item1.getId(), user2.getId(), BookingStatus.WAITING);
        Booking booking2 = new Booking(2, LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4),
                item1.getId(), user2.getId(), BookingStatus.WAITING);
        bookingDto1 = BookingMapper.mapToBookingDto(booking1);
        bookingInfoDto1 = BookingMapper.mapToBookingInfoDto(booking1, itemDto1, userDto2);
        bookingInfoDto2 = BookingMapper.mapToBookingInfoDto(booking2, itemDto1, userDto2);
    }

    @Test
    public void shouldCreateBookingAndReturn() throws Exception {
        when(service.createBooking(anyInt(), any(BookingDto.class))).thenReturn(bookingInfoDto1);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingInfoDto1.getId())))
                .andExpect(jsonPath("$.booker.id", is(bookingInfoDto1.getBooker().getId())))
                .andExpect(jsonPath("$.item.id", is(bookingInfoDto1.getItem().getId())))
                .andExpect(jsonPath("$.status", is(bookingInfoDto1.getStatus().toString())));
        verify(service, times(1)).createBooking(user2.getId(), bookingDto1);
    }

    @Test
    public void shouldUpdateBookingAndReturn() throws Exception {
        bookingInfoDto1.setStatus(BookingStatus.APPROVED);
        when(service.updateBookingStatus(anyInt(), anyInt(), anyBoolean())).thenReturn(bookingInfoDto1);
        mvc.perform(patch("/bookings/{bookingId}?approved={approved}", 1, true)
                        .header("X-Sharer-User-Id", user1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingInfoDto1.getId())))
                .andExpect(jsonPath("$.booker.id", is(bookingInfoDto1.getBooker().getId())))
                .andExpect(jsonPath("$.item.id", is(bookingInfoDto1.getItem().getId())))
                .andExpect(jsonPath("$.status", is(BookingStatus.APPROVED.toString())));
        verify(service, times(1)).updateBookingStatus(user1.getId(),
                booking1.getId(), true);
    }

    @Test
    public void shouldReturnBookingById() throws Exception {
        when(service.getBookingById(anyInt(), anyInt())).thenReturn(bookingInfoDto1);
        mvc.perform((get("/bookings/{bookingId}", booking1.getId()))
                        .header("X-Sharer-User-Id", item1.getOwner()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking1.getId())))
                .andExpect(jsonPath("$.item.id", is(booking1.getItem())))
                .andExpect(jsonPath("$.booker.id", is(booking1.getBooker())))
                .andExpect(jsonPath("$.status", is(booking1.getStatus().toString())));
        verify(service, times(1)).getBookingById(booking1.getId(), item1.getOwner());
    }

    @Test
    public void shouldReturnUserBookings() throws Exception {
        Optional<String> state = Optional.of("ALL");
        List<BookingInfoDto> bookingsDto = List.of(bookingInfoDto1, bookingInfoDto2);
        when(service.getUserBookings(anyInt(), anyString(), anyInt(), anyInt())).thenReturn(bookingsDto);
        mvc.perform((get("/bookings/?state={state}&from={from}&size={size}",state, 0, 10))
                        .header("X-Sharer-User-Id", user2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()", is(2)))
                .andExpect(jsonPath("[0].id", is(bookingInfoDto1.getId())))
                .andExpect(jsonPath("[0].item.id", is(bookingInfoDto1.getItem().getId())))
                .andExpect(jsonPath("[0].booker.id", is(bookingInfoDto1.getBooker().getId())))
                .andExpect(jsonPath("[0].status", is(bookingInfoDto1.getStatus().toString())))
                .andExpect(jsonPath("[1].id", is(bookingInfoDto2.getId())));
    }

    @Test
    public void shouldReturnUserItemsBookings() throws Exception {
        Optional<String> state = Optional.of("ALL");
        List<BookingInfoDto> bookingsDto = List.of(bookingInfoDto1, bookingInfoDto2);
        when(service.getUserItemsBookings(anyInt(), anyString(), anyInt(), anyInt())).thenReturn(bookingsDto);
        mvc.perform((get("/bookings/owner?state={state}&from={from}&size={size}",state, 0, 10))
                        .header("X-Sharer-User-Id", user1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()", is(2)))
                .andExpect(jsonPath("[0].id", is(bookingInfoDto1.getId())))
                .andExpect(jsonPath("[0].item.id", is(bookingInfoDto1.getItem().getId())))
                .andExpect(jsonPath("[0].item.owner", is(user1.getId())))
                .andExpect(jsonPath("[0].booker.id", is(bookingInfoDto1.getBooker().getId())))
                .andExpect(jsonPath("[0].status", is(bookingInfoDto1.getStatus().toString())))
                .andExpect(jsonPath("[1].id", is(bookingInfoDto2.getId())))
                .andExpect(jsonPath("[1].item.owner", is(user1.getId())));
    }

    @Test
    public void shouldThrowsException() {
        when(service.getUserItemsBookings(anyInt(), anyString(), anyInt(), anyInt()))
                .thenThrow(UnsupportedBookingStatusException.class);
        Optional<String> state = Optional.of("unsupported");
        assertThrows(UnsupportedBookingStatusException.class,
                () -> controller.getUserItemsBookings(1, state, 0, 10));
    }

    @Test
    public void shouldThrowsUnsupportedBookingStatusException() throws Exception {
        Optional<String> state = Optional.of("unsupported");
        when(service.getUserItemsBookings(anyInt(), anyString(), anyInt(), anyInt()))
                .thenThrow(new UnsupportedBookingStatusException("UNSUPPORTED_STATUS"));
        mvc.perform(get("/bookings/owner?state={state}&from={from}&size={size}",state, 0, 10)
                        .header("X-Sharer-User-Id", user1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(400));
    }

}
