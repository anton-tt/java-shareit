package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.FinalBookingDto;
import ru.practicum.shareit.booking.dto.InitialBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public FinalBookingDto create(InitialBookingDto initialBookingDto, long userId) {
        User booker = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("Пользователь " +
                "с id = %s отсутствует в БД. Выполнить операцию невозможно!", userId)));

        long itemId = initialBookingDto.getItemId();
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Вещь " +
                "с id = %s отсутствует в БД. Выполнить операцию невозможно!", itemId)));

        if (!item.isAvailable()) {
            throw new ValidationException(String.format("Вещь, которую хочет забронировать пользователь с id = %s," +
                    "в данный момент недоступна. Выполнить операцию невозможно!", userId));
        } else {
            Booking initialBooking = BookingMapper.toInitialBooking(initialBookingDto, userId);
            Booking booking = bookingRepository.save(initialBooking);
            log.info("Данные бронирования вещи добавлены в БД: {}.", booking);

            UserDto bookerDto = UserMapper.toUserDto(booker);
            ItemDto itemDto = ItemMapper.toItemDto(item);
            FinalBookingDto bookingDto = BookingMapper.toFinalBookingDto(booking, itemDto, bookerDto);
            log.info("Новое бронирование вещи создано: {}.", bookingDto);
            return bookingDto;
        }
    }

    /*@Override
    public BookingDto getById(long id) {
        //User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Пользователь " +
         //       "с id = %s отсутствует в БД. Выполнить операцию невозможно!", id)));
        log.info("Бронирование вещи найдено в БД: {}.", id); //
        //UserDto userDto = UserMapper.toUserDto(user);
        log.info("Данные бронирования вещи получены: {}.", id); //
        //return userDto;
        return
    }

    @Override
    public List<BookingDto> getBookingsOneUser(long userId, String state) {
        //userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("Пользователь " +
        //        "с id = %s отсутствует в БД. Выполнить операцию невозможно!", userId)));
        log.info("Получение списка всех бронирований текущего пользователя из БД.");
        /*List<ItemDto> itemDtoList = itemRepository.findAll()
                .stream()
                .filter(item -> item.getOwnerId() == userId)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());*/
    /*    log.info("Сформирован список всех бронирований пользователя с id = {} в количестве {}.", userId, state/*itemDtoList.size()*///);
    /*    return //itemDtoList;
    }

    @Override
    public List<BookingDto> getBookingsOneOwner(long userId, String state) {
        //userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("Пользователь " +
        //        "с id = %s отсутствует в БД. Выполнить операцию невозможно!", userId)));
        log.info("Получение списка бронирований для всех вещей текущего пользователя из БД.");
        /*List<ItemDto> itemDtoList = itemRepository.findAll()
                .stream()
                .filter(item -> item.getOwnerId() == userId)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());*/
    /*     log.info("Сформирован список бронирований для вещей пользователя с id = {} в количестве {}.", userId, state/*itemDtoList.size()*///);
    /*    return //itemDtoList;
    }

    @Override
    public BookingDto approveBooking(long id, boolean approved, long userId) {

        return
    }
*/
}