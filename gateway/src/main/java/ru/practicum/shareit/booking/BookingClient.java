package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;
import java.util.Optional;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(int userId, BookingDto bookingDto) {
        return post("", userId, bookingDto);
    }

    public ResponseEntity<Object> updateBookingStatus(int userId, int bookingId, Boolean approved) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        String path = "/" + bookingId + "?approved={approved}";
        return patch(path, Integer.valueOf(userId).longValue(), parameters, null);
    }

    public ResponseEntity<Object> getBooking(int userId, int bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getUserBookings(int userId, BookingState state, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", Integer.valueOf(userId).longValue(), parameters);
    }

    public ResponseEntity<Object> getUserItemBookings(int userId, BookingState state, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        String path = "/owner?state={state}&from={from}&size={size}";
        return get(path, Integer.valueOf(userId).longValue(), parameters);
    }
}
