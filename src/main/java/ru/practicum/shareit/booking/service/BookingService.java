package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.FinalBookingDto;
import ru.practicum.shareit.booking.dto.InitialBookingDto;

public interface BookingService {

    FinalBookingDto create(InitialBookingDto bookingDto, long userId);
    FinalBookingDto approve(long id, boolean approved, long userId);
   /* BookingDto getById(long id);
    List<BookingDto> getBookingsOneUser(long userId, String state);
    List<BookingDto> getBookingsOneOwner(long userId, String state);
    */

}