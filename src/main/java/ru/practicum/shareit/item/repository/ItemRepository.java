package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))")
    Page<Item> search(String text, Pageable pageable);

    Page<Item> findAllByOwnerId(Long userId, Pageable pageable);

    Boolean existsAllByOwnerId(Long ownerId);

    List<Item> findByItemRequestId(Long requestId);

    List<Item>  findAllByItemRequestIdIn(List<Long> requestIdList);

    List<Item> findByItemRequestIdIsNotNull();

}