package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.FinalBookingDto;
import ru.practicum.shareit.booking.dto.InitialBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import static ru.practicum.shareit.utils.Constants.X_SHARER_USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public FinalBookingDto createBooking(@RequestHeader(X_SHARER_USER_ID) long userId,
                                         @RequestBody InitialBookingDto booking) {
        log.info("");
        log.info("Добавление от пользователя с id = {} нового запроса на бронирование: {}", userId, booking);
        return bookingService.create(booking, userId);
    }


    @PatchMapping("/{bookingId}")
    public FinalBookingDto approveBooking(@RequestHeader(X_SHARER_USER_ID) long userId,
                                          @PathVariable long bookingId,
                                          @RequestParam boolean approved) {
        log.info("");
        log.info("Обновление статуса запроса на бронирование с id = {}", bookingId);
        return bookingService.approve(bookingId, approved, userId);
    }

    /*@PatchMapping("/{bookingId}")
    public FinalBookingDto approve(@RequestHeader(X_SHARER_USER_ID) long userId,
                                   @PathVariable long bookingId,
                                      @RequestParam boolean approved) {
        log.info("");
        log.info("Обновление статуса запроса на бронирование с id = {}", bookingId);
        return bookingService.approve(bookingId, approved, userId);
    }

   /* @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(X_SHARER_USER_ID) long userId,
                                     @PathVariable long id) {
        log.info("");
        log.info("Получение данных бронирования с id = {}", id);
        return bookingService.getById(id);
    }

    @GetMapping
    public List<BookingDto> getBookingsOneUser(@RequestHeader(X_SHARER_USER_ID) long userId,
                                               @RequestParam (defaultValue = "All") String state) {
        log.info("");
        log.info("Поиск всех бронирований, запрошенных пользователем с id = {}", userId);
        return bookingService.getBookingsOneUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsOneOwner(@RequestHeader(X_SHARER_USER_ID) long userId,
                                               @RequestParam (defaultValue = "All") String state) {
        log.info("");
        log.info("Поиск всех бронирований вещей, добавленных пользователем с id = {}", userId);
        return bookingService.getBookingsOneOwner(userId, state);
    }

    */

}