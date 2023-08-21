package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto create(ItemDto itemDto, long userId) {
        Item dataItem = ItemMapper.toItem(itemDto, userId);
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("Пользователь " +
               "с id = %s отсутствует в БД. Выполнить операцию невозможно!", userId)));
        Item item = itemRepository.save(dataItem);
        log.info("Данные вещи добавлены в БД: {}.", item);
        ItemDto createdItemDto = ItemMapper.toItemDto(item);
        log.info("Новая вещь создана: {}.", createdItemDto);
        return createdItemDto;
    }

    @Override
    public ItemDto getById(long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Вещь " +
                "с id = %s отсутствует в БД. Выполнить операцию невозможно!", id)));
        log.info("Вещь найдена в БД: {}.", item);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        log.info("Данные вещи получены: {}.", itemDto);
        return itemDto;
    }

    @Override
    public List<ItemDto> getItemsOneOwner(long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("Пользователь " +
                "с id = %s отсутствует в БД. Выполнить операцию невозможно!", userId)));
        log.info("Получение данных всех вещей пользователя из БД.");
        List<ItemDto> itemDtoList = itemRepository.findAll()
                .stream()
                .filter(item -> item.getOwnerId() == userId)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        log.info("Сформирован список всех вещей пользователя с id = {} в количестве {}.", userId, itemDtoList.size());
        return itemDtoList;
    }

    @Override
    public ItemDto update(long id, ItemDto itemDto, long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("Пользователь " +
                "с id = %s отсутствует в БД. Выполнить операцию невозможно!", userId)));
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Вещь " +
                "с id = %s отсутствует в БД. Выполнить операцию невозможно!", id)));
        log.info("Вещь найдена в БД: {}.", item);
        if (item.getOwnerId() == userId) {
            Item newDataItem = itemRepository.save(ItemMapper.toUpdatedItem(item, itemDto));
            log.info("Данные вещи обновлены в БД: {}.", newDataItem);
            ItemDto updatedItemDto = ItemMapper.toItemDto(newDataItem);
            log.info("Данные вещи обновлены: {}.", updatedItemDto);
            return updatedItemDto;
        } else {
            throw new NotFoundException(String.format("Пользователь с id = %s не является владельцем вещи с id = %s!" +
                    "Обновить её данные невозможно.", userId, id));
        }
    }

    @Override
    public void delete(long id, long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("Пользователь " +
                "с id = %s отсутствует в БД. Выполнить операцию невозможно!", userId)));
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Вещь " +
                "с id = %s отсутствует в БД. Выполнить операцию невозможно!", id)));
        log.info("Вещь найдена в БД: {}.", item);
        if (item.getOwnerId() == userId) {
            itemRepository.delete(item);
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
            itemDtoList = itemRepository.search(text)
                    .stream()
                    .filter(Item::isAvailable)
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        }
        log.info("Сформирован список всех доступных для аренды вещей в количестве {} штук" +
                " по запросу: {}.", itemDtoList.size(), text);
        return itemDtoList;
    }

}