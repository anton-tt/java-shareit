package main.java.ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utils.Constants.TYPE_BOOKER;
import static ru.practicum.shareit.utils.Constants.TYPE_OWNER;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Пользователь " +
                "с id = %s отсутствует в БД. Выполнить операцию невозможно!", id)));
    }

    private Item getItemById(long id) {
        return itemRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Вещь " +
                "с id = %s отсутствует в БД. Выполнить операцию невозможно!", id)));
    }

    private Booking getBookingById(long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new NotFoundException(
                String.format("Бронирование с id = %s отсутствует в БД. Выполнить операцию невозможно!", id)));
    }

    @Override
    public ResponseBookingDto create(RequestBookingDto requestBookingDto, long userId) {
        User booker = getUserById(userId);
        Item item = getItemById(requestBookingDto.getItemId());

         if (!item.isAvailable()) {
            throw new ValidationException(String.format("Вещь, которую хочет забронировать пользователь с id = %s," +
                    "в данный момент недоступна. Выполнить операцию невозможно!", userId));
        } else if (item.getOwner().getId() == userId) {
            throw new NotFoundException(String.format("Пользователь с id = %s, который хочет забронировать вещь " +
                    "является её владельцем. Выполнить операцию невозможно!", userId));
        } else {
            Booking bookingData = BookingMapper.toBooking(requestBookingDto, item, booker);
            Booking booking = bookingRepository.save(bookingData);
            log.info("Данные бронирования вещи добавлены в БД: {}.", booking);

            ResponseBookingDto bookingDto =
                    BookingMapper.toResponseBookingDto(booking, ItemMapper.toResponseItemDto(item),
                            UserMapper.toResponseUserDto(booker));
            log.info("Новое бронирование вещи создано: {}.", bookingDto);
            return bookingDto;
        }
    }

    @Override
    public ResponseBookingDto approve(long id, boolean approved, long userId) {
        getUserById(userId);
        Booking bookingData = getBookingById(id);
        Item item = getItemById(bookingData.getItem().getId());

        if (item.getOwner().getId() != userId) {
            throw new NotFoundException(String.format("Пользователь с id = %s не является владельцем вещи с id = %s!" +
                    "Изменить статус бронирования этой вещи невозможно.", userId, item.getId()));
        }
        Booking booking = changeBookingStatus(approved, bookingData);

        User booker = getUserById(booking.getBooker().getId());
        ResponseBookingDto bookingDto = BookingMapper.toResponseBookingDto(booking,
                ItemMapper.toResponseItemDto(item), UserMapper.toResponseUserDto(booker));
        log.info("Статус бронирования изменён хозяином вещи: {}.", bookingDto);
        return bookingDto;
    }

    private Booking changeBookingStatus(boolean approved, Booking bookingData) {
        if (!bookingData.getStatus().equals(Status.WAITING)) {
            throw new ValidationException("Владелец в ответ на запрос о бронировании вещи уже изменил статус " +
                    "бронирования. Повторное изменение не требуется.");
        }

        Status newBookingStatus;
        if (approved) {
            newBookingStatus = Status.APPROVED;
        } else {
            newBookingStatus = Status.REJECTED;
        }
        log.info("Новый статус бронирования: {}.", newBookingStatus);

        bookingData.setStatus(newBookingStatus);
        Booking booking = bookingRepository.save(bookingData);
        log.info("Статус бронирования вещи изменён в БД: {}.", booking);
        return booking;
    }

    @Override
    public ResponseBookingDto getById(long id, long userId) {
        getUserById(userId);
        Booking booking = getBookingById(id);
        log.info("Бронирование вещи найдено в БД: {}.", booking);
        Item item = getItemById(booking.getItem().getId());
        User booker = getUserById(booking.getBooker().getId());

        if (userId != item.getOwner().getId() && userId != booker.getId()) {
            throw new NotFoundException(String.format("Пользователь с id = %s, запросивший информацию о бронировании " +
                    "не является ни владельцем, ни арендатором вещи! Операцию выполнить невозможно.", userId));
        }
        ResponseBookingDto bookingDto = BookingMapper.toResponseBookingDto(booking,
                ItemMapper.toResponseItemDto(item), UserMapper.toResponseUserDto(booker));
        log.info("Данные бронирования вещи получены: {}.", bookingDto);
        return bookingDto;
    }


    @Override
    public List<ResponseBookingDto> getBookingsOneBooker(long bookerId, String state, int from, int size) {
        User booker = getUserById(bookerId);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        Page<Booking> bookingList = selectBookingsByState(bookerId, state, TYPE_BOOKER, pageable);
        List<ResponseBookingDto> bookingDtoList = bookingList
            .stream()
            .map(booking -> BookingMapper.toResponseBookingDto(booking,
                    ItemMapper.toResponseItemDto(booking.getItem()),
                    UserMapper.toResponseUserDto(booker)))
            .collect(Collectors.toList());
        log.info("Сформирован список бронирований пользователя с id = {} в количестве {} в соответствии " +
                        "с поставленным запросом.", bookerId, bookingDtoList.size());
        return bookingDtoList;

    }

    @Override
    public List<ResponseBookingDto> getBookingsOneOwner(long ownerId, String state, int from, int size) {
        getUserById(ownerId);
        if (!itemRepository.existsAllByOwnerId(ownerId)) {
            throw new NotFoundException(String.format("Пользователь с id = %s, запросивший информацию о бронировании " +
                    " своих вещей, не имеет ни одной вещи! Операцию выполнить невозможно.", ownerId));
        }

        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        Page<Booking> bookingList = selectBookingsByState(ownerId, state, TYPE_OWNER, pageable);
                List<ResponseBookingDto> bookingDtoList = bookingList
                .stream()
                .map(booking -> BookingMapper.toResponseBookingDto(booking,
                        ItemMapper.toResponseItemDto(booking.getItem()),
                        UserMapper.toResponseUserDto(booking.getBooker())))
                .collect(Collectors.toList());
        log.info("Сформирован список бронирований для вещей пользователя с id = {} в количестве {} в соответствии " +
                "с поставленным запросом.", ownerId, bookingDtoList.size());
        return bookingDtoList;
    }

    private Page<Booking> selectBookingsByState(Long userId, String state, String typeOfUser, Pageable pageable) {
        List<Booking> list = Collections.emptyList();
        Page<Booking> bookingList = new PageImpl<>(list);
        LocalDateTime currentMoment = LocalDateTime.now();

        try {
            switch (BookingState.valueOf(state)) {
                case ALL:
                    bookingList = BookingsUtil.selectBookingsByStateAll(bookingRepository, typeOfUser, userId, pageable);
                    break;

                case CURRENT:
                    bookingList = BookingsUtil.selectBookingsByStateCurrent(bookingRepository, typeOfUser, userId,
                            currentMoment, pageable);
                    break;

                case PAST:
                    bookingList = BookingsUtil.selectBookingsByStatePast(bookingRepository, typeOfUser, userId,
                            currentMoment, pageable);
                    break;

                case FUTURE:
                    bookingList = BookingsUtil.selectBookingsByStateFuture(bookingRepository, typeOfUser, userId,
                            currentMoment, pageable);
                    break;

                case WAITING:
                    bookingList = BookingsUtil.selectBookingsByStateWaiting(bookingRepository, typeOfUser, userId,
                            pageable);
                    break;

                case REJECTED:
                    bookingList = BookingsUtil.selectBookingsByStateRejected(bookingRepository, typeOfUser, userId,
                            pageable);
                    break;
            }

        } catch (IllegalArgumentException e) {
            throw new ValidationException(String.format("Unknown state: %s", state));
        }

        return bookingList;
    }

}