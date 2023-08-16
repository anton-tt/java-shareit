package ru.practicum.shareit.user.storage;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DataConflictsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import java.util.*;

@Repository
@NoArgsConstructor
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private int id = 0;
    private final Map<Long, User> usersMap = new HashMap<>();
    Set<String> mailsSet = new HashSet<>();

    private int getNextId() {
        return ++id;
    }

    @Override
    public User create(User user) {
        String userMail = user.getEmail();

        if (mailsSet.contains(userMail)) {
            throw new DataConflictsException(String.format("Пользователь с такой же электронной почтой %s " +
                    "уже существует! Добавить нового пользователя в usersMap невозможно.", userMail));
        }

        if (userMail != null && userMail.contains("@")) {
            user.setId(getNextId());
            usersMap.put(user.getId(), user);
            mailsSet.add(userMail);
        } else {
            throw new ValidationException(String.format("У пользователя электронная почта отсутствует или " +
                    "не соответствует правильному формату: %s! Добавить нового пользователя в usersMap " +
                    "невозможно.", userMail));
        }

        log.info("Новый пользователь добавлен в usersMap: {}.", user);
        return user;
    }

    @Override
    public User getById(long id) {
        if (usersMap.containsKey(id)) {
            log.info("Пользователь с id = {} найден в usersMap.", id);
            return usersMap.get(id);
        } else {
            throw new NotFoundException(String.format("Пользователь с id = %s отсутствует в usersMap. " +
                    "Выполнить операцию невозможно!", id));
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
    public User update(User updatedUser, UserDto userDto) {
        String updatedUserEmail = updatedUser.getEmail();
        String userDtoName = userDto.getName();
        String userDtoEmail = userDto.getEmail();

        if (userDtoName != null) {
            updatedUser.setName(userDtoName);
            log.info("Имя пользователя изменено на {}, новые данные сохранены в usersMap.", userDtoName);
        }

        if (userDtoEmail != null)
            if (!userDtoEmail.contains("@")) {
                throw new ValidationException(String.format("У пользователя некорректный новой адрес " +
                        "электронной почты: %s. Обновить данные в usersMap невозможно!", userDtoEmail));
            } else if (!updatedUserEmail.equals(userDtoEmail) && mailsSet.contains(userDtoEmail)) {
                throw new DataConflictsException(String.format("Пользователь с такой же электронной почтой %s " +
                        "уже существует! Обновить данные текущего пользователя в usersMap невозможно.", userDtoEmail));
            } else if (updatedUserEmail.equals(userDtoEmail)) {
                log.info("Адрес новой электронной почты и адрес старой одинаковые: {}. " +
                        "Обновление данных в usersMap не требуется.", userDtoEmail);
            } else {
                mailsSet.remove(updatedUserEmail);
                updatedUser.setEmail(userDtoEmail);
                mailsSet.add(userDtoEmail);
                log.info("Эл.почта пользователя изменена на {}, новые данные сохранены в usersMap.", userDtoEmail);
            }

        usersMap.put(updatedUser.getId(), updatedUser);
        return updatedUser;
    }

    @Override
    public void delete(User user) {
        mailsSet.remove(user.getEmail());
        Long userId = user.getId();
        usersMap.remove(userId);
        log.info("Данные пользователя с id = {} удалены из usersMap.", userId);
    }

}