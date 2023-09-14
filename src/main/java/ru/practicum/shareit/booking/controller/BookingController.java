package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import static ru.practicum.shareit.utils.Constants.X_SHARER_USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseBookingDto createBooking(@RequestHeader(X_SHARER_USER_ID) long userId,
                                            @RequestBody @Valid RequestBookingDto booking) {
        log.info("");
        log.info("Добавление от пользователя с id = {} нового запроса на бронирование: {}", userId, booking);
        return bookingService.create(booking, userId);
    }


    @PatchMapping("/{bookingId}")
    public ResponseBookingDto approveBooking(@RequestHeader(X_SHARER_USER_ID) long userId,
                                             @PathVariable long bookingId,
                                             @RequestParam boolean approved) {
        log.info("");
        log.info("Обновление статуса запроса на бронирование с id = {}", bookingId);
        return bookingService.approve(bookingId, approved, userId);
    }


   @GetMapping("/{bookingId}")
    public ResponseBookingDto getBookingById(@RequestHeader(X_SHARER_USER_ID) long userId,
                                             @PathVariable long bookingId) {
        log.info("");
        log.info("Получение данных бронирования с id = {}", bookingId);
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public List<ResponseBookingDto> getBookingsOneUser(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                       @RequestParam(defaultValue = "ALL") String state,
                                                       @RequestParam(defaultValue = "0") @Min(0) int from,
                                                       @RequestParam(defaultValue = "10") @Min(1) int size) {
        log.info("");
        log.info("Поиск и сортировка бронирований, запрошенных пользователем с id = {}", userId);
        return bookingService.getBookingsOneBooker(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<ResponseBookingDto> getBookingsOneOwner(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                        @RequestParam (defaultValue = "ALL") String state,
                                                        @RequestParam(defaultValue = "0") @Min(0) int from,
                                                        @RequestParam(defaultValue = "10") @Min(1) int size) {
        log.info("");
        log.info("Поиск и сортировка бронирований для вещей, владелец которых пользователь с id = {}", userId);
        return bookingService.getBookingsOneOwner(userId, state, from, size);
    }

}