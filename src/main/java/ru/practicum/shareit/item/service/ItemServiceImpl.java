package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto create(ItemDto itemDto, long userId) {
        //User user = userStorage.getById(userId);
        Item item = itemStorage.create(ItemMapper.toItem(itemDto));
        item.setOwnerId(userId);
        ItemDto createdItemDto = ItemMapper.toItemDto(item);
        log.info("Новая вещь создана: {}.", createdItemDto);
        return createdItemDto;
    }

    @Override
    public ItemDto getById(long id) {
        ItemDto itemDto = ItemMapper.toItemDto(itemStorage.getById(id));
        log.info("Данные вещи получены: {}.", itemDto);
        return itemDto;
    }

    @Override
    public List<ItemDto> getItemsOneOwner(long userId) {
        userStorage.getById(userId);
        List<ItemDto> itemDtoList = itemStorage.getAll()
                .stream()
                .filter(item -> item.getOwnerId() == userId)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        log.info("Сформирован список всех вещей пользователя с id = {} " +
                "в количестве {}.", userId, itemDtoList.size());
        return itemDtoList;
    }

    @Override
    public ItemDto update(long id, ItemDto itemDto, long userId) {
        userStorage.getById(userId);
        Item item = itemStorage.getById(id);

        if (item.getOwnerId() == userId) {
            Item updatedItem = itemStorage.update(ItemMapper.toUpdatedItem(item, itemDto));
            ItemDto updatedItemDto = ItemMapper.toItemDto(updatedItem);
            log.info("Данные вещи обновлены: {}.", updatedItemDto);
            return updatedItemDto;
        } else {
            throw new NotFoundException(String.format("Пользователь с id = %s не является владельцем вещи с id = %s!" +
                    "Обновить её данные невозможно.", userId, id));
        }
    }

    @Override
    public void delete(long id, long userId) {
        userStorage.getById(userId);
        Item item = itemStorage.getById(id);
        if (item.getOwnerId() == userId) {
            itemStorage.delete(id);
            log.info("Все данные вещи удалены.");
        } else {
            throw new NotFoundException(String.format("Пользователь с id = %s не является владельцем вещи с id = %s!" +
                    "Удалить её данные невозможно.", userId, id));
        }
    }

    @Override
    public List<ItemDto> search(String text) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        if (!text.isBlank()) {
            String lowerCaseText = text.toLowerCase();
            itemDtoList = itemStorage.getAll()
                    .stream()
                    .filter(Item::isAvailable)
                    .filter(item -> item.getName().toLowerCase().contains(lowerCaseText) ||
                            item.getDescription().toLowerCase().contains(lowerCaseText))
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        }
        log.info("Сформирован список всех доступных для аренды вещей в количестве {} штук" +
                " по запросу: {}.", itemDtoList.size(), text);
        return itemDtoList;
    }

}