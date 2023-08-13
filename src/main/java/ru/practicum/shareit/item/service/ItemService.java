package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;
import java.util.List;

public interface ItemService {
    Item create(Item item);
    Item getById(long id);
    List<Item> getAll();
    Item update(Item item);
    void delete(long id);

}
