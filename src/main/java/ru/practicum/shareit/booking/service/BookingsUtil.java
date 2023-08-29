package ru.practicum.shareit.booking.service;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static ru.practicum.shareit.utils.Constants.TYPE_BOOKER;
import static ru.practicum.shareit.utils.Constants.TYPE_OWNER;

@UtilityClass
@Slf4j
public class BookingsUtil {

    public List<Booking> selectBookingsByStateAll(BookingRepository bookingRepository, String typeOfUser,
                                                  long userId, Sort sortByStart) {
        log.info("Получение из БД списка всех бронирований. Категория ALL.");
        List<Booking> bookingList = new ArrayList<>();

        switch (typeOfUser) {
            case TYPE_BOOKER:
                bookingList = bookingRepository.findAllByBookerId(userId, sortByStart);
                break;
            case TYPE_OWNER:
                bookingList = bookingRepository.allBookersByOwnerId(userId, sortByStart);
                break;
        }
        return bookingList;
    }

    public List<Booking> selectBookingsByStateCurrent(BookingRepository bookingRepository, String typeOfUser,
                                                      long userId, LocalDateTime currentMoment, Sort sortByStart) {
        log.info("Получение из БД списка действующих бронирований. Категория CURRENT.");
        List<Booking> bookingList = new ArrayList<>();

        switch (typeOfUser) {
            case TYPE_BOOKER:
                bookingList = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(userId, currentMoment,
                    currentMoment, sortByStart);
                break;
            case TYPE_OWNER:
             bookingList = bookingRepository.currentBookersByOwnerId(userId, currentMoment, sortByStart);
                break;
        }
        return bookingList;
    }

    public List<Booking> selectBookingsByStatePast(BookingRepository bookingRepository, String typeOfUser,
                                               long userId, LocalDateTime currentMoment, Sort sortByStart) {
        log.info("Получение из БД списка завершённых бронирований. Категория PAST.");
        List<Booking> bookingList = new ArrayList<>();

        switch (typeOfUser) {
            case TYPE_BOOKER:
                bookingList = bookingRepository.findAllByBookerIdAndEndBefore(userId, currentMoment, sortByStart);
                break;
            case TYPE_OWNER:
                bookingList = bookingRepository.pastBookersByOwnerId(userId, currentMoment, sortByStart);
                break;
        }
        return bookingList;
    }

    public List<Booking> selectBookingsByStateFuture(BookingRepository bookingRepository, String typeOfUser,
                                               long userId, LocalDateTime currentMoment, Sort sortByStart) {

        log.info("Получение из БД списка будущих бронирований. Категория FUTURE.");
        List<Booking> bookingList = new ArrayList<>();

        switch (typeOfUser) {
            case TYPE_BOOKER:
                bookingList = bookingRepository.findAllByBookerIdAndStartAfter(userId, currentMoment, sortByStart);
                break;
            case TYPE_OWNER:
                bookingList = bookingRepository.futureBookersByOwnerId(userId, currentMoment, sortByStart);
                break;
        }
        return bookingList;
    }

    public List<Booking> selectBookingsByStateWaiting(BookingRepository bookingRepository, String typeOfUser,
                                               long userId, Sort sortByStart) {
        log.info("Получение из БД списка бронирований  со статусом Ожидает подтверждения. " +
                "Категория WAITING.");
        List<Booking> bookingList = new ArrayList<>();

        switch (typeOfUser) {
            case TYPE_BOOKER:
                bookingList = bookingRepository.findAllByBookerIdAndStatus(userId, Status.WAITING, sortByStart);
                break;
            case TYPE_OWNER:
                bookingList = bookingRepository.bookersByStatusAndOwnerId(userId, Status.WAITING, sortByStart);
                break;
        }
        return bookingList;
    }

    public List<Booking> selectBookingsByStateRejected(BookingRepository bookingRepository, String typeOfUser,
                                               long userId, Sort sortByStart) {
        log.info("Получение из БД списка бронирований текущего пользователя со статусом " +
                "Отклонённые владельцем. Категория WAITING.");
        List<Booking> bookingList = new ArrayList<>();

        switch (typeOfUser) {
            case TYPE_BOOKER:
                bookingList = bookingRepository.findAllByBookerIdAndStatus(userId, Status.REJECTED, sortByStart);
                break;
            case TYPE_OWNER:
                bookingList = bookingRepository.bookersByStatusAndOwnerId(userId, Status.REJECTED, sortByStart);
                break;
        }
        return bookingList;
    }

}