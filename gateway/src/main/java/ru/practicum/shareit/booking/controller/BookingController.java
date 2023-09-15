package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import static ru.practicum.shareit.utils.Constants.X_SHARER_USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
@Slf4j
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                @RequestBody @Valid RequestBookingDto booking) {
        log.info("");
        log.info("Gateway: поступил запрос на создание бронирования.");
        return bookingClient.create(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                 @PathVariable long bookingId,
                                                 @RequestParam boolean approved) {
        log.info("");
        log.info("Gateway: поступил запрос на обновление статуса бронирования.");
        return bookingClient.approve(userId, bookingId, approved);
    }

   @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                 @PathVariable long bookingId) {
        log.info("");
        log.info("Gateway: поступил запрос на получение данных бронирования.");
        return bookingClient.getById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsOneUser(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                     @RequestParam(defaultValue = "ALL") String state,
                                                     @RequestParam(defaultValue = "0") @Min(0) int from,
                                                     @RequestParam(defaultValue = "10") @Min(1) int size) {
        log.info("");
        log.info("Gateway: от пользователя поступил запрос на поиск и сортировку бронирований.");
        return bookingClient.getBookingsOneBooker(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsOneOwner(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                      @RequestParam (defaultValue = "ALL") String state,
                                                      @RequestParam(defaultValue = "0") @Min(0) int from,
                                                      @RequestParam(defaultValue = "10") @Min(1) int size) {
        log.info("");
        log.info("Gateway: от хозяина вещей поступил запрос на поиск и сортировку их бронирований.");
        return bookingClient.getBookingsOneOwner(userId, state, from, size);
    }

}