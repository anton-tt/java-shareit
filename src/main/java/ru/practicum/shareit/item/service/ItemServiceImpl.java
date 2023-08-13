package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemServiceImpl implements ItemService {
    private long id = 0;
    private final Map<Long, Item> itemsMap = new HashMap<>();

    private long getNextId() {
        return ++id;
    }

    @Override
    public Item create(Item item) {
        item.setId(getNextId());
        itemsMap.put(item.getId(), item);
        return item;

    }

    @Override
    public Item getById(long id) {
        /*if (id < 1) {
            throw new NotFoundException(String.format("Получен некорректный id: %s - неположительное число", id));
        }*/
        // !(itemsMap.containsKey(id)) ? return itemsMap.get(id) : throw new NotFoundException(String.format("Вещь с id = %s отсутствует.", id));

        if (!itemsMap.containsKey(id)) {
            throw new NotFoundException(String.format("Вещь с id = %s отсутствует.", id));
        }
        return itemsMap.get(id);

    }

    @Override
    public List<Item> getAll() {
        List<Item> itemsList = new ArrayList<>();
        if (!itemsMap.isEmpty()) {
            for (Map.Entry<Long, Item> entry : itemsMap.entrySet()) {
                itemsList.add(entry.getValue());
            }
        }
        return itemsList;
    }

    @Override
    public Item update(Item item) {
        long itemId = item.getId();
        if (itemsMap.containsKey(itemId)) {
            itemsMap.put(itemId, item);
            return item;
        } else {
            throw new NotFoundException("Вещь, данные которой необходимо обновить, отсутствует.");
        }
    }

    @Override
    public void delete(long id) {
        if (itemsMap.containsKey(id)) {
            itemsMap.remove(id);
        } else {
            throw new NotFoundException(String.format("Фильм с id = %s отсутствует.", id));
        }
    }
}
