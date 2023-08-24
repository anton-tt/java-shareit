package ru.practicum.shareit.booking.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.FinalBookingDto;
import ru.practicum.shareit.booking.dto.InitialBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import java.time.LocalDateTime;

@Slf4j
public class BookingMapper {

    private BookingMapper() {

    }

    public static Booking toInitialBooking(InitialBookingDto initialBooking, long userId) {
        LocalDateTime currentDateTime = LocalDateTime.now().withNano(0);
        LocalDateTime startBooking = initialBooking.getStart();
        LocalDateTime endBooking = initialBooking.getEnd();

        if ((startBooking == null) || (startBooking.isBefore(currentDateTime))
                || (startBooking.isEqual(currentDateTime))) {
            throw new ValidationException(String.format("У бронирования отсутствует или неправильно задана " +
                    "дата начала: %s! Операцию выполнить невозможно.", startBooking));
        } else if ((endBooking == null) || (endBooking.isBefore(startBooking)) || (endBooking.isEqual(startBooking))) {
            throw new ValidationException(String.format("У бронирования отсутствует или неправильно задана " +
                    "дата окончания: %s! Операцию выполнить невозможно.", endBooking));
        } else {
            return Booking.builder()
                    .start(startBooking)
                    .end(endBooking)
                    .status(Status.WAITING)
                    .itemId(initialBooking.getItemId())
                    .bookerId(userId)
                    .build();
        }
    }

    public static FinalBookingDto toFinalBookingDto(Booking booking, ItemDto itemDto, UserDto bookerDto) {
        return FinalBookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(itemDto)
                .booker(bookerDto)
                .build();
    }

}