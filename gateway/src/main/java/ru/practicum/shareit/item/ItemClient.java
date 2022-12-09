package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(int userId, ItemDto itemDto) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> updateItem(int userId, int itemId, ItemDto itemDto) {
        String path = "/" + itemId;
        return patch(path, userId, itemDto);
    }

    public ResponseEntity<Object> deleteItem(int userId, int itemId) {
        String path = "/" + itemId;
        return delete(path, userId);
    }

    public ResponseEntity<Object> getItem(int userId, int itemId) {
        String path = "/" + itemId;
        return get(path, userId);
    }

    public ResponseEntity<Object> getItems(int userId, int from, int size) {
        String path = "?from={from}&size={size}";
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get(path, Integer.valueOf(userId).longValue(), parameters);
    }

    public ResponseEntity<Object> getItemsBySearch(int userId, String text, int from, int size) {
        String path = "/search?text={text}&from={from}&size={size}";
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get(path, Integer.valueOf(userId).longValue(), parameters);
    }

    public ResponseEntity<Object> addComment(int userId, int itemId, CommentDto commentDto) {
        String path = "/" + itemId + "/comment";
        return post(path, userId, commentDto);
    }
}
