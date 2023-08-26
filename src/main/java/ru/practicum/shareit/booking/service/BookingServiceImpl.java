package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.FinalBookingDto;
import ru.practicum.shareit.booking.dto.InitialBookingDto;
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
import java.util.ArrayList;
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
    public FinalBookingDto create(InitialBookingDto initialBookingDto, long userId) {
        User booker = getUserById(userId);
        Item item = getItemById(initialBookingDto.getItemId());

        if (!item.isAvailable()) {
            throw new ValidationException(String.format("Вещь, которую хочет забронировать пользователь с id = %s," +
                    "в данный момент недоступна. Выполнить операцию невозможно!", userId));
        } else if (item.getOwnerId() == userId) {
            throw new NotFoundException(String.format("Пользователь с id = %s, который хочет забронировать вещь " +
                    "является её владельцем. Выполнить операцию невозможно!", userId));
        } else {
            Booking initialBooking = BookingMapper.toInitialBooking(initialBookingDto, userId);
            Booking booking = bookingRepository.save(initialBooking);
            log.info("Данные бронирования вещи добавлены в БД: {}.", booking);

            FinalBookingDto bookingDto =
                    BookingMapper.toFinalBookingDto(booking, ItemMapper.toItemDto(item), UserMapper.toUserDto(booker));
            log.info("Новое бронирование вещи создано: {}.", bookingDto);
            return bookingDto;
        }
    }

    @Override
    public FinalBookingDto approve(long id, boolean approved, long userId) {
        getUserById(userId);
        Booking initialBooking = getBookingById(id);
        Item item = getItemById(initialBooking.getItemId());

        if (item.getOwnerId() != userId) {
            throw new NotFoundException(String.format("Пользователь с id = %s не является владельцем вещи с id = %s!" +
                    "Изменить статус бронирования этой вещи невозможно.", userId, item.getId()));
        }
        Booking booking = changeBookingStatus(approved, initialBooking);

        User booker = getUserById(booking.getBookerId());
        FinalBookingDto bookingDto = BookingMapper.toFinalBookingDto(booking,
                ItemMapper.toItemDto(item), UserMapper.toUserDto(booker));
        log.info("Статус бронирования изменён хозяином вещи: {}.", bookingDto);
        return bookingDto;
    }

    private Booking changeBookingStatus(boolean approved, Booking initialBooking) {
        if (!initialBooking.getStatus().equals(Status.WAITING)) {
            throw new ValidationException("Владелец в ответ на запрос о бронировании вещи уже изменил статус бронирования." +
                    " Повторное изменение не требуется.");
        }

        Status newBookingStatus;
        if (approved) {
            newBookingStatus = Status.APPROVED;
        } else {
            newBookingStatus = Status.REJECTED;
        }
        log.info("Новый статус бронирования: {}.", newBookingStatus);

        initialBooking.setStatus(newBookingStatus);
        Booking booking = bookingRepository.save(initialBooking);
        log.info("Статус бронирования вещи изменён в БД: {}.", booking);
        return booking;
    }

    @Override
    public FinalBookingDto getById(long id, long userId) {
        getUserById(userId);
        Booking booking = getBookingById(id);
        log.info("Бронирование вещи найдено в БД: {}.", booking);
        Item item = getItemById(booking.getItemId());
        User booker = getUserById(booking.getBookerId());

        if (userId != item.getOwnerId() && userId != booker.getId()) {
            throw new NotFoundException(String.format("Пользователь с id = %s, запросивший информацию о бронировании " +
                    "не является ни владельцем, ни арендатором вещи! Операцию выполнить невозможно.", userId));
        }
        FinalBookingDto bookingDto = BookingMapper.toFinalBookingDto(booking,
                ItemMapper.toItemDto(item), UserMapper.toUserDto(booker));
        log.info("Данные бронирования вещи получены: {}.", bookingDto);
        return bookingDto;
    }


    @Override
    public List<FinalBookingDto> getBookingsOneBooker(long bookerId, String state) {
        User booker = getUserById(bookerId);

        List<FinalBookingDto> bookingDtoList = selectBookingsByState(bookerId, state, TYPE_BOOKER)
            .stream()
            .map(booking -> {
                Item item = getItemById(booking.getItemId());
                return BookingMapper.toFinalBookingDto(booking, ItemMapper.toItemDto(item),
                        UserMapper.toUserDto(booker));
                })
            .collect(Collectors.toList());
        log.info("Сформирован список бронирований пользователя с id = {} в количестве {} в соответствии " +
                        "с поставленным запросом.", bookerId, bookingDtoList.size());
        return bookingDtoList;
    }

    @Override
    public List<FinalBookingDto> getBookingsOneOwner(long ownerId, String state) {
        User owner = getUserById(ownerId);
        if (itemRepository.findAllByOwnerId(ownerId).isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id = %s, запросивший информацию о бронировании " +
                    " своих вещей, не имеет ни одной вещи! Операцию выполнить невозможно.", ownerId));
        }
        List<FinalBookingDto> bookingDtoList = selectBookingsByState(ownerId, state, TYPE_OWNER)
                .stream()
                .map(booking -> {
                    Item item = getItemById(booking.getItemId());
                    User booker = getUserById(booking.getBookerId());
                    return BookingMapper.toFinalBookingDto(booking, ItemMapper.toItemDto(item),
                            UserMapper.toUserDto(booker));
                })
                .collect(Collectors.toList());
        log.info("Сформирован список бронирований для вещей пользователя с id = {} в количестве {} в соответствии " +
                "с поставленным запросом.", ownerId, bookingDtoList.size());
        return bookingDtoList;
    }

    private List<Booking> selectBookingsByState(Long userId, String state, String typeOfUser) {
        List<Booking> bookingList = new ArrayList<>();
        Sort sortByStart = Sort.by(Sort.Direction.DESC, "start");
        LocalDateTime currentMoment = LocalDateTime.now();

        boolean isBookerType = typeOfUser.equals(TYPE_BOOKER);

        try {
            switch (BookingState.valueOf(state)) {
                case ALL:
                    log.info("Получение из БД списка всех бронирований. Категория ALL.");
                    if (isBookerType) {
                        bookingList = bookingRepository.findAllByBookerId(userId, sortByStart);
                    } else {
                        bookingList = bookingRepository.allBookersByOwnerId(userId, sortByStart);
                    }
                    break;

                case CURRENT:
                    log.info("Получение из БД списка действующих бронирований. Категория CURRENT.");
                    if (isBookerType) {
                        bookingList = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(userId, currentMoment,
                            currentMoment, sortByStart);
                    } else {
                        bookingList = bookingRepository.currentBookersByOwnerId(userId, currentMoment, sortByStart);
                    }
                    break;

                case PAST:
                    log.info("Получение из БД списка завершённых бронирований. Категория PAST.");
                    if (isBookerType) {
                        bookingList = bookingRepository.findAllByBookerIdAndEndBefore(userId, currentMoment, sortByStart);
                    } else {
                        bookingList = bookingRepository.pastBookersByOwnerId(userId, currentMoment, sortByStart);
                    }
                    break;

                case FUTURE:
                    log.info("Получение из БД списка будущих бронирований. Категория FUTURE.");
                    if (isBookerType) {
                        bookingList = bookingRepository.findAllByBookerIdAndStartAfter(userId, currentMoment, sortByStart);
                    } else {
                        bookingList = bookingRepository.futureBookersByOwnerId(userId, currentMoment, sortByStart);
                    }
                    break;

                case WAITING:
                    log.info("Получение из БД списка бронирований  со статусом Ожидает подтверждения. " +
                                "Категория WAITING.");
                    if (isBookerType) {
                        bookingList = bookingRepository.findAllByBookerIdAndStatus(userId, Status.WAITING, sortByStart);
                    } else {
                        bookingList = bookingRepository.bookersByStatusAndOwnerId(userId, Status.WAITING, sortByStart);
                    }
                    break;

                    case REJECTED:
                    log.info("Получение из БД списка бронирований текущего пользователя со статусом " +
                        "Отклонённые владельцем. Категория WAITING.");
                    if (isBookerType) {
                        bookingList = bookingRepository.findAllByBookerIdAndStatus(userId, Status.REJECTED, sortByStart);
                    } else {
                        bookingList = bookingRepository.bookersByStatusAndOwnerId(userId, Status.REJECTED, sortByStart);
                    }
                    break;
            }

        } catch (Exception e) {
            throw new ValidationException(String.format("Unknown state: %s", state));
        }

        return bookingList;
    }

}