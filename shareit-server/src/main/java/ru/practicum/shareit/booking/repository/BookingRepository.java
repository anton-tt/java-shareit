package main.java.ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            "JOIN Item i ON (i.id = b.item.id) " +
            "JOIN User u ON (u.id = i.owner.id) ";

    Page<Booking> findAllByBookerId(Long bookerId, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime currentMomentOne,
                                                             LocalDateTime currentMomentTwo, Pageable pageable);

    Page<Booking> findAllByBookerIdAndEndBefore(Long bookerId, LocalDateTime currentMoment, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartAfter(Long bookerId, LocalDateTime currentMoment, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStatus(long bookerId, Status status, Pageable pageable);

    @Query(queryBasis +
           "WHERE i.owner.id = ?1")
    Page<Booking> allBookersByOwnerId(long ownerId, Pageable pageable);

    @Query(queryBasis +
            "WHERE i.owner.id = ?1 AND b.start < ?2 AND b.end > ?2")
    Page<Booking> currentBookersByOwnerId(Long bookerId, LocalDateTime currentMoment, Pageable pageable);

    @Query(queryBasis +
            "WHERE i.owner.id = ?1 AND b.end < ?2")
    Page<Booking> pastBookersByOwnerId(Long bookerId, LocalDateTime currentMoment, Pageable pageable);

    @Query(queryBasis +
            "WHERE i.owner.id = ?1 AND b.start > ?2")
    Page<Booking> futureBookersByOwnerId(Long bookerId, LocalDateTime currentMoment, Pageable pageable);

    @Query(queryBasis +
            "WHERE i.owner.id = ?1 AND b.status = ?2")
    Page<Booking> bookersByStatusAndOwnerId(Long bookerId, Status status, Pageable pageable);

    Boolean existsAllByBookerIdAndStatusAndEndBefore(Long bookerId, Status status, LocalDateTime currentMoment);

    List<Booking> findByItemId(Long itemId, Sort sortByStart);

    List<Booking> findAllByItemIdInAndStatus(List<Long> itemIdList, Status status, Sort sortByStart);

}