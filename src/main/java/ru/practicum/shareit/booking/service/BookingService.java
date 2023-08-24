package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.FinalBookingDto;
import ru.practicum.shareit.booking.dto.InitialBookingDto;
import java.util.List;

public interface BookingService {

    FinalBookingDto create(InitialBookingDto bookingDto, long userId);
    FinalBookingDto approve(long id, boolean approved, long userId);
    FinalBookingDto getById(long id);
    List<FinalBookingDto> getBookingsOneBooker(long userId, String state);
    /*List<BookingDto> getBookingsOneOwner(long userId, String state);
    */

}