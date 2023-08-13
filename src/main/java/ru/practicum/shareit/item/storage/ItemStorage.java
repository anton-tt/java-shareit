package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;
import java.util.List;

public interface ItemStorage {

    Item create(Item item);
    Item getById(long id);
    List<Item> getAll();
    Item update(Item item);
    void delete(Item item);


}
