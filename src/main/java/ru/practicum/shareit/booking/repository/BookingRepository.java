package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(Long bookerId, Sort sortByStart);
    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime currentMomentOne,
                                                             LocalDateTime currentMomentTwo, Sort sortByStart);
    List<Booking> findAllByBookerIdAndEndBefore(Long bookerId, LocalDateTime currentMoment, Sort sortByStart);
    List<Booking> findAllByBookerIdAndStartAfter(Long bookerId, LocalDateTime currentMoment, Sort sortByStart);
    List<Booking> findAllByBookerIdAndStatus(long bookerId, Status status, Sort sortByStart);

}