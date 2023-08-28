package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.ItemBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.comment.dto.ResponseCommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.FullResponseItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    private User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Пользователь " +
                "с id = %s отсутствует в БД. Выполнить операцию невозможно!", id)));
    }

    private Item getItemById(long id) {
        return itemRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Вещь " +
                "с id = %s отсутствует в БД. Выполнить операцию невозможно!", id)));
    }

    @Override
    public ResponseItemDto create(RequestItemDto itemDto, long userId) {
        User owner = getUserById(userId);
        Item dataItem = ItemMapper.toItem(itemDto, owner);
        Item item = itemRepository.save(dataItem);
        log.info("Данные вещи добавлены в БД: {}.", item);
        ResponseItemDto responseItemDto = ItemMapper.toResponseItemDto(item);
        log.info("Новая вещь создана: {}.", responseItemDto);
        return responseItemDto;
    }

    @Override
    public FullResponseItemDto getById(long id, long ownerId) {
        Item item = getItemById(id);
        log.info("Вещь найдена в БД: {}.", item);
        FullResponseItemDto itemDto = ItemMapper.toFullResponseItemDto(item);

        setComments(itemDto);
        if (item.getOwner().getId() == ownerId) {
            setLastAndNextBookings(itemDto, LocalDateTime.now());
        }

        log.info("Все данные вещи получены: {}.", itemDto);
        return itemDto;
    }

    @Override
    public List<FullResponseItemDto> getItemsOneOwner(long userId) {
        getUserById(userId);
        log.info("Получение данных всех вещей пользователя из БД.");
        LocalDateTime currentMoment = LocalDateTime.now();
        List<FullResponseItemDto> itemDtoList = itemRepository.findAll()
                .stream()
                .filter(item -> item.getOwner().getId() == userId)
                .map(ItemMapper::toFullResponseItemDto)
                .map(itemDto -> setLastAndNextBookings(itemDto, currentMoment))
                .collect(Collectors.toList());
        log.info("Сформирован список всех вещей пользователя с id = {} в количестве {}.", userId, itemDtoList.size());
        return itemDtoList;
    }

    @Override
    public ResponseItemDto update(long id, RequestItemDto requestItemDto, long userId) {
        getUserById(userId);
        Item item = getItemById(id);
        log.info("Вещь найдена в БД: {}.", item);
        if (item.getOwner().getId() == userId) {
            Item newDataItem = itemRepository.save(ItemMapper.toUpdatedItem(item, requestItemDto));
            log.info("Данные вещи обновлены в БД: {}.", newDataItem);
            ResponseItemDto responseItemDto = ItemMapper.toResponseItemDto(newDataItem);
            log.info("Данные вещи обновлены: {}.", responseItemDto);
            return responseItemDto;
        } else {
            throw new NotFoundException(String.format("Пользователь с id = %s не является владельцем вещи с id = %s!" +
                    "Обновить её данные невозможно.", userId, id));
        }
    }

    @Override
    public void delete(long id, long userId) {
        getUserById(userId);
        Item item = getItemById(id);
        log.info("Вещь найдена в БД: {}.", item);
        if (item.getOwner().getId() == userId) {
            itemRepository.delete(item);
            log.info("Все данные вещи удалены.");
        } else {
            throw new NotFoundException(String.format("Пользователь с id = %s не является владельцем вещи с id = %s!" +
                    "Удалить её данные невозможно.", userId, id));
        }
    }

    @Override
    public List<ResponseItemDto> search(String text) {
        List<ResponseItemDto> itemDtoList = new ArrayList<>();
        if (!text.isBlank()) {
            itemDtoList = itemRepository.search(text)
                    .stream()
                    .filter(Item::isAvailable)
                    .map(ItemMapper::toResponseItemDto)
                    .collect(Collectors.toList());
        }
        log.info("Сформирован список всех доступных для аренды вещей в количестве {} штук" +
                " по запросу: {}.", itemDtoList.size(), text);
        return itemDtoList;
    }

    @Override
    public ResponseCommentDto createComment(long itemId, RequestCommentDto requestCommentDto, long userId) {
        Item item = getItemById(itemId);
        User user = getUserById(userId);
        LocalDateTime currentMoment = LocalDateTime.now();
        List<Booking> oneUserBookingsOneItem = bookingRepository.findAllByBookerIdAndStatusAndEndBefore(userId,
                        Status.APPROVED, currentMoment)
                .stream()
                .filter(booking -> booking.getItem().getId() == itemId)
                .collect(Collectors.toList());
        if (oneUserBookingsOneItem.isEmpty()) {
            throw new ValidationException(String.format("Пользователь с id = %s, который хочет добавить комментарий, " +
                    "никогда не бронировал вещь с id = %s. Выполнить операцию невозможно!", userId, itemId));
        }

        Comment commentData = CommentMapper.toComment(item, requestCommentDto, user, currentMoment);
        Comment comment = commentRepository.save(commentData);
        log.info("Данные комментария добавлены в БД: {}.", comment);
        ResponseCommentDto responseCommentDto = CommentMapper.toResponseCommentDto(comment, user);
        log.info("Новая вещь создана: {}.", responseCommentDto);
        return responseCommentDto;

    }

    private void setComments(FullResponseItemDto item) {
        log.info("Поиск в БД отзывов о вещи, при положительном результате добавление данных к объекту вещи.");
        List<ResponseCommentDto> responseCommentDtoList =  commentRepository.findByItemId(item.getId())
                .stream()
                .map(comment -> {
                    User author = getUserById(comment.getAuthor().getId());
                    return CommentMapper.toResponseCommentDto(comment, author); }
                )
                .collect(Collectors.toList());
        item.setComments(responseCommentDtoList);
    }

    private FullResponseItemDto setLastAndNextBookings(FullResponseItemDto itemDto, LocalDateTime currentMoment) {
        log.info("Поиск в БД бронирований вещи, отбор последнего бронирования и ближайшего следующего бронирования " +
                "при положительном результате добавление данных к объекту вещи.");
        List<Booking> itemBookings = bookingRepository.findAllByItemIdAndStatus(itemDto.getId(), Status.APPROVED);

        if (itemBookings != null) {
            Booking lastBooking = null;
            Booking nextBooking = null;

            for (Booking booking : itemBookings) {
                LocalDateTime bookingStart = booking.getStart();
                boolean startIsNotNull = bookingStart != null;
                LocalDateTime bookingEnd = booking.getEnd();
                boolean endIsNotNull = bookingEnd != null;

                boolean startIsNotBeforeCurrentMoment = startIsNotNull &&
                        (bookingStart.isAfter(currentMoment) || bookingStart.equals(currentMoment));
                boolean endIsNotAfterCurrentMoment = endIsNotNull &&
                        (bookingEnd.isBefore(currentMoment) || bookingEnd.equals(currentMoment));
                boolean bookingIsCurrent = (bookingStart.isBefore(currentMoment) && (bookingEnd.isAfter(currentMoment))) ||
                        ((bookingStart.equals(currentMoment)) && (bookingEnd.isAfter(currentMoment))) ||
                        ((bookingStart.isBefore(currentMoment)) && (bookingEnd.equals(currentMoment)));

                if (startIsNotNull && endIsNotNull && bookingIsCurrent) {
                    lastBooking = booking;
                } else if (endIsNotAfterCurrentMoment &&
                        ((lastBooking == null) || (lastBooking.getEnd().isBefore(bookingEnd)))) {
                    lastBooking = booking;
                }

                if (startIsNotBeforeCurrentMoment &&
                        (nextBooking == null || (nextBooking.getStart().isAfter(bookingStart)))) {
                    nextBooking = booking;
                }
            }

            if (lastBooking != null) {
                ItemBookingDto lastBookingDto = BookingMapper.toItemBookingDto(lastBooking);
                itemDto.setLastBooking(lastBookingDto);
                log.info("Дата последнего бронирования и ближайшего следующего бронирования добавлена.");
            }
            if (nextBooking != null) {
                ItemBookingDto nextBookingDto = BookingMapper.toItemBookingDto(nextBooking);
                itemDto.setNextBooking(nextBookingDto);
                log.info("Дата ближайшего следующего бронирования добавлена.");
            }
        }

        return itemDto;
    }

}