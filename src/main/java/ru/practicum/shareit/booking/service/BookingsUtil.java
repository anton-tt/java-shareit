package ru.practicum.shareit.booking.service;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import static ru.practicum.shareit.utils.Constants.TYPE_BOOKER;
import static ru.practicum.shareit.utils.Constants.TYPE_OWNER;

@UtilityClass
@Slf4j
public class BookingsUtil {

    public Page<Booking> selectBookingsByStateAll(BookingRepository bookingRepository, String typeOfUser,
                                                  long userId, Pageable pageable) {
        log.info("Получение из БД списка всех бронирований. Категория ALL.");
        List<Booking> list = Collections.emptyList();
        Page<Booking> bookingList = new PageImpl<>(list);

        switch (typeOfUser) {
            case TYPE_BOOKER:
                bookingList = bookingRepository.findAllByBookerId(userId, pageable);
                break;
            case TYPE_OWNER:
                bookingList = bookingRepository.allBookersByOwnerId(userId, pageable);
                break;
            default:
                bookingList = null;
        }
        return bookingList;
    }

    public Page<Booking> selectBookingsByStateCurrent(BookingRepository bookingRepository, String typeOfUser,
                                                      long userId, LocalDateTime currentMoment, Pageable pageable) {
        log.info("Получение из БД списка действующих бронирований. Категория CURRENT.");
        List<Booking> list = Collections.emptyList();
        Page<Booking> bookingList = new PageImpl<>(list);

        switch (typeOfUser) {
            case TYPE_BOOKER:
                bookingList = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(userId, currentMoment,
                    currentMoment, pageable);
                break;
            case TYPE_OWNER:
                bookingList = bookingRepository.currentBookersByOwnerId(userId, currentMoment, pageable);
                break;
            default:
                bookingList = null;
        }
        return bookingList;
    }

    public Page<Booking> selectBookingsByStatePast(BookingRepository bookingRepository, String typeOfUser,
                                               long userId, LocalDateTime currentMoment, Pageable pageable) {
        log.info("Получение из БД списка завершённых бронирований. Категория PAST.");
        List<Booking> list = Collections.emptyList();
        Page<Booking> bookingList = new PageImpl<>(list);

        switch (typeOfUser) {
            case TYPE_BOOKER:
                bookingList = bookingRepository.findAllByBookerIdAndEndBefore(userId, currentMoment, pageable);
                break;
            case TYPE_OWNER:
                bookingList = bookingRepository.pastBookersByOwnerId(userId, currentMoment, pageable);
                break;
            default:
                bookingList = null;
        }
        return bookingList;
    }

    public Page<Booking> selectBookingsByStateFuture(BookingRepository bookingRepository, String typeOfUser,
                                               long userId, LocalDateTime currentMoment, Pageable pageable) {
        log.info("Получение из БД списка будущих бронирований. Категория FUTURE.");
        List<Booking> list = Collections.emptyList();
        Page<Booking> bookingList = new PageImpl<>(list);

        switch (typeOfUser) {
            case TYPE_BOOKER:
                bookingList = bookingRepository.findAllByBookerIdAndStartAfter(userId, currentMoment, pageable);
                break;
            case TYPE_OWNER:
                bookingList = bookingRepository.futureBookersByOwnerId(userId, currentMoment, pageable);
                break;
            default:
                bookingList = null;
        }
        return bookingList;
    }

    public Page<Booking> selectBookingsByStateWaiting(BookingRepository bookingRepository, String typeOfUser,
                                               long userId, Pageable pageable) {
        log.info("Получение из БД списка бронирований  со статусом Ожидает подтверждения. " +
                "Категория WAITING.");
        List<Booking> list = Collections.emptyList();
        Page<Booking> bookingList = new PageImpl<>(list);

        switch (typeOfUser) {
            case TYPE_BOOKER:
                bookingList = bookingRepository.findAllByBookerIdAndStatus(userId, Status.WAITING, pageable);
                break;
            case TYPE_OWNER:
                bookingList = bookingRepository.bookersByStatusAndOwnerId(userId, Status.WAITING, pageable);
                break;
            default:
                bookingList = null;
        }
        return bookingList;
    }

    public Page<Booking> selectBookingsByStateRejected(BookingRepository bookingRepository, String typeOfUser,
                                               long userId, Pageable pageable) {
        log.info("Получение из БД списка бронирований текущего пользователя со статусом " +
                "Отклонённые владельцем. Категория REJECTED.");
        List<Booking> list = Collections.emptyList();
        Page<Booking> bookingList = new PageImpl<>(list);

        switch (typeOfUser) {
            case TYPE_BOOKER:
                bookingList = bookingRepository.findAllByBookerIdAndStatus(userId, Status.REJECTED, pageable);
                break;
            case TYPE_OWNER:
                bookingList = bookingRepository.bookersByStatusAndOwnerId(userId, Status.REJECTED, pageable);
                break;
            default:
                bookingList = null;
        }
        return bookingList;
    }

}