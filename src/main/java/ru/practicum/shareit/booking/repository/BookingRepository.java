package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    String queryBasis = "SELECT b " +
            "FROM Booking b " +
            "JOIN Item i ON (i.id = b.itemId) " +
            "JOIN User u ON (u.id = i.ownerId) ";

    List<Booking> findAllByBookerId(Long bookerId, Sort sortByStart);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime currentMomentOne,
                                                             LocalDateTime currentMomentTwo, Sort sortByStart);

    List<Booking> findAllByBookerIdAndEndBefore(Long bookerId, LocalDateTime currentMoment, Sort sortByStart);

    List<Booking> findAllByBookerIdAndStartAfter(Long bookerId, LocalDateTime currentMoment, Sort sortByStart);

    List<Booking> findAllByBookerIdAndStatus(long bookerId, Status status, Sort sortByStart);

    @Query(queryBasis +
           "WHERE i.ownerId = ?1")
    List<Booking> allBookersByOwnerId(long ownerId, Sort sortByStart);

    @Query(queryBasis +
            "WHERE i.ownerId = ?1 AND b.start < ?2 AND b.end > ?2")
    List<Booking> currentBookersByOwnerId(Long bookerId, LocalDateTime currentMoment, Sort sortByStart);

    @Query(queryBasis +
            "WHERE i.ownerId = ?1 AND b.end < ?2")
    List<Booking> pastBookersByOwnerId(Long bookerId, LocalDateTime currentMoment, Sort sortByStart);

    @Query(queryBasis +
            "WHERE i.ownerId = ?1 AND b.start > ?2")
    List<Booking> futureBookersByOwnerId(Long bookerId, LocalDateTime currentMoment, Sort sortByStart);

    @Query(queryBasis +
            "WHERE i.ownerId = ?1 AND b.status = ?2")
    List<Booking> bookersByStatusAndOwnerId(Long bookerId, Status status, Sort sortByStart);

    List<Booking> findAllByBookerIdAndStatusAndEndBefore(Long bookerId, Status status, LocalDateTime currentMoment);

    List<Booking> findAllByItemIdAndStatus(Long itemId, Status status);

}