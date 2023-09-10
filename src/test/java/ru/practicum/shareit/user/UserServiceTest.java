package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private final long userId = 10L;
    private final String userName = "userUser";
    private final String userMail = "user@mail.com";
    private final RequestUserDto requestUserDto = RequestUserDto.builder().name(userName).email(userMail).build();
    private final User user = User.builder().id(userId).name(userName).email(userMail).build();
    private final ResponseUserDto responseUserDto = ResponseUserDto.builder().id(userId).name(userName)
            .email(userMail).build();

    private final String newUserName = "newUser";
    private final String newUserMail = "newUser@mail.com";
    private final RequestUserDto newDataUserDto = RequestUserDto.builder().name(newUserName).email(newUserMail).build();
    private final User newDataUser = User.builder().id(userId).name(newUserName).email(newUserMail).build();
    private final ResponseUserDto newResponseUserDto = ResponseUserDto.builder().id(userId).name(newUserName)
            .email(newUserMail).build();

    @Test
    void createUserTest() {
        Mockito.when(userRepository.save(Mockito.any()))
               .thenReturn(user);

        assertEquals(userService.create(requestUserDto), responseUserDto);
    }

    @Test
    void getUserByIdTest() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
               .thenReturn(Optional.ofNullable(user));

        assertEquals(userService.getById(userId), responseUserDto);
    }

    @Test
    void getUserByNotExistingIdTest() {
        Mockito.when(userRepository.findById(Mockito.any()))
               .thenThrow(new NotFoundException("Пользователь с id = 9999 отсутствует в БД. " +
                        "Выполнить операцию невозможно!"));
        Exception exception = assertThrows(NotFoundException.class, () -> userService.getById(9999));

        assertEquals("Пользователь с id = 9999 отсутствует в БД. Выполнить операцию невозможно!",
                exception.getMessage());
    }

    @Test
    void getAllUsersTest() {
        Mockito.when(userRepository.findAll())
               .thenReturn(List.of(user));

        assertEquals(userService.getAll(), List.of(responseUserDto));
    }

    @Test
    void updateUserTest() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
               .thenReturn(Optional.ofNullable(user));
        Mockito.when(userRepository.save(Mockito.any()))
               .thenReturn(newDataUser);

        assertEquals(userService.update(userId, newDataUserDto), newResponseUserDto);
    }

    @Test
    void deleteUserTest() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
               .thenReturn(Optional.ofNullable(user));
        userService.delete(100);

        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
    }

}