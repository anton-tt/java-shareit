package main.java.ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;

import java.util.List;

public interface BookingService {

    ResponseBookingDto create(RequestBookingDto bookingDto, long userId);

    ResponseBookingDto approve(long id, boolean approved, long userId);

    ResponseBookingDto getById(long id, long userId);

    List<ResponseBookingDto> getBookingsOneBooker(long userId, String state, int from, int size);

    List<ResponseBookingDto> getBookingsOneOwner(long userId, String state, int from, int size);

}