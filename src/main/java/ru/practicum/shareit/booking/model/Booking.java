package ru.practicum.shareit.booking.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "start_date", nullable = false)
    @Future
    private LocalDateTime start;
    @Column(name = "end_date", nullable = false)
    @Future
    private LocalDateTime end;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "item_id", nullable = false)
    private long itemId;
    @Column(name = "booker_id", nullable = false)
    private long bookerId;

}
