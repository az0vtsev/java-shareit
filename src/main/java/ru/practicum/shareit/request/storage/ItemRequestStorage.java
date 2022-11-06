package ru.practicum.shareit.request.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestStorage extends JpaRepository<ItemRequest, Integer> {

    List<ItemRequest> findByRequestorOrderByCreatedDesc(int requestor);

    Page<ItemRequest> findByRequestorIsNotOrderByCreatedDesc(int requestor, Pageable pageable);
}
