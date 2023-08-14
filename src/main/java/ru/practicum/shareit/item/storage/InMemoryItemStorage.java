package ru.practicum.shareit.item.storage;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Repository
@NoArgsConstructor
@Slf4j
public class InMemoryItemStorage implements ItemStorage {
    private int id = 0;
    private final Map<Long, Item> itemsMap = new HashMap<>();

    private int getNextId() {
        return ++id;
    }

    @Override
    public Item create(Item item) {
        item.setId(getNextId());
        itemsMap.put(item.getId(), item);
        log.info("Новая вещь добавлена в itemsMap: {}", item);
        return item;
    }

    @Override
    public Item getById(long id) {
        if (itemsMap.containsKey(id)) {
            log.info("Вещь с id = {} найдена в itemsMap.", id);
            return itemsMap.get(id);
        } else {
            throw new NotFoundException(String.format("Вещь с id = %s отсутствует в itemsMap.", id));
        }
    }

    @Override
    public List<Item> getAll() {
        if (!itemsMap.isEmpty()) {
            log.info("Сформирован список всех вещей в количестве {}.", itemsMap.size());
            return new ArrayList<>(itemsMap.values());
        } else {
            throw new NotFoundException("itemsMap не содержит элементов, вывести список всех вещей невозможно!");
        }
    }

    @Override
    public Item update(Item item) {
        long itemId = item.getId();
        if (itemsMap.containsKey(itemId)) {
            itemsMap.put(itemId, item);
            log.info("Данные вещи с id = {} обновлены в itemsMap.", itemId);
            return item;
        } else {
            throw new NotFoundException(String.format("Вещь с id = %s отсутствует в itemsMap! " +
                    "Обновить её данные невозможно.", id));
        }
    }

    @Override
    public void delete(Item item) {
        long itemId = item.getId();
        if (itemsMap.containsKey(itemId)) {
            itemsMap.remove(itemId);
            log.info("Все данные вещи с id = {} удалены из itemsMap.", itemId);
        } else {
            throw new NotFoundException(String.format("Вещь с id = %s отсутствует в itemsMap! " +
                    "Удалить её данные невозможно.", id));
        }
    }

}