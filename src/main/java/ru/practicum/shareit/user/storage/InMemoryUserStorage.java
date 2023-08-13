package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exception.DataConflictsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import java.util.*;

@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private int id = 0;
    private final Map<Long, User> usersMap = new HashMap<>();
    Set<String> mailsSet = new HashSet<>();

    private int getNextId() {
        return ++id;
    }

    private boolean checkRecurrenceMail(String mail) {
        boolean isRepeatedMail = false;
        if (mailsSet.contains(mail)) {
            isRepeatedMail = true;
        }
        return isRepeatedMail;
    }
    @Override
    public User create(User user) {
        String userMail = user.getEmail();
        if (!checkRecurrenceMail(userMail)) {
            user.setId(getNextId());
            usersMap.put(user.getId(), user);
            log.info("Новый пользователь добавлен в usersMap: {}.", user);
            return user;
        } else {
            throw new DataConflictsException(String.format("Пользователь с электронной почтой %s " +
                    "уже существует! Добавить нового пользователя в usersMap невозможно.", userMail));
        }
    }

    @Override
    public User getById(long id) {
        if (usersMap.containsKey(id)) {
            log.info("Пользователь с id = {} найден в usersMap.", id);
            return usersMap.get(id);
        } else {
            throw new NotFoundException(String.format("Пользователь с id = %s отсутствует в usersMap.", id));
        }
    }

    @Override
    public List<User> getAll() {
        if (!usersMap.isEmpty()) {
            log.info("Сформирован список всех пользователей в количестве {}.", usersMap.size());
            return new ArrayList<>(usersMap.values());
        } else {
            throw new NotFoundException("usersMap не содержит элементов, вывести список всех пользователей невозможно!");
        }
    }

    @Override
    public User update(User user) { // обновление эл. почты в Set?
        long userId = user.getId();
        if (usersMap.containsKey(userId)) {
            usersMap.put(userId, user);
            log.info("Данные пользователя с id = {} обновлены в usersMap.", userId);
            return user;
        } else {
            throw new NotFoundException(String.format("Пользователь с id = %s отсутствует в usersMap! " +
                    "Обновить его данные невозможно.", id));
        }
    }

    @Override
    public void delete(User user) {
        long userId = user.getId();
        if (usersMap.containsKey(userId)) {
            usersMap.remove(userId);
            log.info("Данные пользователя с id = {} удалены из usersMap.", userId);
        } else {
            throw new NotFoundException(String.format("Пользователь с id = %s отсутствует в usersMap! " +
                    "Удалить его данные невозможно.", id));
        }
    }

}
