package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private final long bookerId = 10L;
    private final String bookerName = "bookerUser";
    private final String bookerMail = "booker@mail.com";
    private final User booker = User.builder().id(bookerId).name(bookerName).email(bookerMail).build();
    private final ResponseUserDto responseBookerDto = ResponseUserDto.builder().id(bookerId).name(bookerName)
            .email(bookerMail).build();

    private final long notExistingBookerId = 9999L;

    private final long ownerId = 11L;
    private final String ownerName = "ownerUser";
    private final String ownerMail = "owner@mail.com";
    private final User owner = User.builder().id(ownerId).name(ownerName).email(ownerMail).build();

    private final long itemId = 5L;
    private final String itemName = "itemTest";
    private final String itemDescription = "itemTestTestTest";
    private final Item item = Item.builder().id(itemId).name(itemName).description(itemDescription).available(true)
            .owner(owner).build();
    private final ResponseItemDto responseItemDto = ResponseItemDto.builder().id(itemId).name(itemName)
            .description(itemDescription).available(true).requestId(0).build();

    private final long notExistingItemId = 99999L;

    private final long bookingId = 300L;
    private final LocalDateTime bookingStartOne = LocalDateTime.of(2023, 9, 11, 12, 0);
    private final LocalDateTime bookingEndOne = LocalDateTime.of(2023, 9, 12, 12, 0);
    private final RequestBookingDto requestBookingDto = RequestBookingDto.builder().start(bookingStartOne)
            .end(bookingEndOne).itemId(itemId).build();
    private final Booking booking = Booking.builder().id(bookingId).start(bookingStartOne).end(bookingEndOne)
            .status(Status.WAITING).item(item).booker(booker).build();
    private final Booking bookingApproved = Booking.builder().id(bookingId).start(bookingStartOne).end(bookingEndOne)
            .status(Status.APPROVED).item(item).booker(booker).build();
    private final Booking bookingNotApproved = Booking.builder().id(bookingId).start(bookingStartOne).end(bookingEndOne)
            .status(Status.REJECTED).item(item).booker(booker).build();
    private final ResponseBookingDto responseBookingDto = ResponseBookingDto.builder().id(bookingId)
            .start(bookingStartOne).end(bookingEndOne).status(Status.WAITING).item(responseItemDto)
            .booker(responseBookerDto).build();
    private final ResponseBookingDto responseBookingApprovedDto = ResponseBookingDto.builder().id(bookingId)
            .start(bookingStartOne).end(bookingEndOne).status(Status.APPROVED).item(responseItemDto)
            .booker(responseBookerDto).build();
    private final ResponseBookingDto responseBookingNotApprovedDto = ResponseBookingDto.builder().id(bookingId)
            .start(bookingStartOne).end(bookingEndOne).status(Status.REJECTED).item(responseItemDto)
            .booker(responseBookerDto).build();

    private final long notExistingBookingId = 999L;

    private Page<Booking> getBookingPage(Booking booking) {
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        Page<Booking> bookings = new PageImpl<>(bookingList);
        return bookings;
    }

    @Test
    void testCreateBooking() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));
        Mockito.when(bookingRepository.save(Mockito.any()))
                .thenReturn(booking);

        assertEquals(bookingService.create(requestBookingDto, bookerId), responseBookingDto);
    }

    @Test
    void testCreateBookingNotExistingUser() {
        Mockito.when(userRepository.findById(Mockito.any()))
                .thenThrow(new NotFoundException(String.format("Пользователь с id = %s отсутствует в БД. " +
                        "Выполнить операцию невозможно!", notExistingBookerId)));
        Exception exception = assertThrows(NotFoundException.class, () -> bookingService.create(requestBookingDto,
                notExistingBookerId));

        assertEquals("Пользователь с id = " + notExistingBookerId + " отсутствует в БД. Выполнить операцию невозможно!",
                exception.getMessage());
    }

    @Test
    void testCreateBookingNotExistingItem() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        Mockito.when(itemRepository.findById(Mockito.any()))
                .thenThrow(new NotFoundException(String.format("Вещь с id = %s отсутствует в БД. " +
                        "Выполнить операцию невозможно!", notExistingItemId)));
        Exception exception = assertThrows(NotFoundException.class, () -> bookingService.create(requestBookingDto,
                notExistingItemId));

        assertEquals("Вещь с id = " + notExistingItemId + " отсутствует в БД. Выполнить операцию невозможно!",
                exception.getMessage());
    }

    @Test
    void testApproveBooking() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(bookingRepository.save(Mockito.any()))
                .thenReturn(bookingApproved);
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booker));

        assertEquals(bookingService.approve(bookingId, true, ownerId), responseBookingApprovedDto);
    }

    @Test
    void testNotApproveBooking() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(bookingRepository.save(Mockito.any()))
                .thenReturn(bookingNotApproved);
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booker));

        assertEquals(bookingService.approve(bookingId, false, ownerId), responseBookingNotApprovedDto);
    }

    @Test
    void testGetBookingById() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(bookingApproved));
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booker));

        assertEquals(bookingService.getById(bookingId, ownerId), responseBookingApprovedDto);
    }

    @Test
    void testGetNotExistingBookingById() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        Mockito.when(bookingRepository.findById(Mockito.any()))
                .thenThrow(new NotFoundException(String.format("Бронирование с id = %s отсутствует в БД. " +
                        "Выполнить операцию невозможно!", notExistingBookingId)));
        Exception exception = assertThrows(NotFoundException.class, () -> bookingService.getById(notExistingBookingId,
                        ownerId));

        assertEquals("Бронирование с id = " + notExistingBookingId + " отсутствует в БД. Выполнить операцию невозможно!",
                exception.getMessage());
    }

    @Test
    void testGetBookingsOneBooker() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        Mockito.when(bookingRepository.findAllByBookerId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(getBookingPage(bookingApproved));

        assertEquals(bookingService.getBookingsOneBooker(bookerId, "ALL", 0, 10),
                List.of(responseBookingApprovedDto));
    }

    @Test
    void testGetBookingsOneBookerEmpty() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        List<Booking> bookingList = new ArrayList<>();
        Page<Booking> bookings = new PageImpl<>(bookingList);
        Mockito.when(bookingRepository.findAllByBookerId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(bookings);

        assertEquals(bookingService.getBookingsOneBooker(bookerId, "ALL", 0, 10),
                new ArrayList<>());
    }

    @Test
    void testGetBookingsOneBookerStateCurrent() {
        LocalDateTime currentBookingStart = LocalDateTime.of(2023, 9, 1, 12, 0);
        LocalDateTime currentBookingEnd = LocalDateTime.of(2023, 9, 30, 12, 0);
        bookingApproved.setStart(currentBookingStart);
        bookingApproved.setEnd(currentBookingEnd);
        responseBookingApprovedDto.setStart(currentBookingStart);
        responseBookingApprovedDto.setEnd(currentBookingEnd);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        Mockito.when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(Mockito.anyLong(), Mockito.any(),
                        Mockito.any(), Mockito.any()))
                .thenReturn(getBookingPage(bookingApproved));

        assertEquals(bookingService.getBookingsOneBooker(bookerId, "CURRENT", 0, 10),
                List.of(responseBookingApprovedDto));
    }

    @Test
    void testGetBookingsOneBookerStatePast() {
        LocalDateTime currentBookingStart = LocalDateTime.of(2023, 8, 1, 12, 0);
        LocalDateTime currentBookingEnd = LocalDateTime.of(2023, 8, 31, 12, 0);
        bookingApproved.setStart(currentBookingStart);
        bookingApproved.setEnd(currentBookingEnd);
        responseBookingApprovedDto.setStart(currentBookingStart);
        responseBookingApprovedDto.setEnd(currentBookingEnd);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        Mockito.when(bookingRepository.findAllByBookerIdAndEndBefore(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(getBookingPage(bookingApproved));

        assertEquals(bookingService.getBookingsOneBooker(bookerId, "PAST", 0, 10),
                List.of(responseBookingApprovedDto));
    }

    @Test
    void testGetBookingsOneBookerStateFuture() {
        LocalDateTime currentBookingStart = LocalDateTime.of(2023, 10, 1, 12, 0);
        LocalDateTime currentBookingEnd = LocalDateTime.of(2023, 10, 31, 12, 0);
        bookingApproved.setStart(currentBookingStart);
        bookingApproved.setEnd(currentBookingEnd);
        responseBookingApprovedDto.setStart(currentBookingStart);
        responseBookingApprovedDto.setEnd(currentBookingEnd);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        Mockito.when(bookingRepository.findAllByBookerIdAndStartAfter(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(getBookingPage(bookingApproved));

        assertEquals(bookingService.getBookingsOneBooker(bookerId, "FUTURE", 0, 10),
                List.of(responseBookingApprovedDto));
    }

    @Test
    void testGetBookingsOneBookerStateWaiting() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        Mockito.when(bookingRepository.findAllByBookerIdAndStatus(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(getBookingPage(booking));

        assertEquals(bookingService.getBookingsOneBooker(bookerId, "WAITING", 0, 10),
                List.of(responseBookingDto));
    }

    @Test
    void testGetBookingsOneBookerStateRejected() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        Mockito.when(bookingRepository.findAllByBookerIdAndStatus(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(getBookingPage(bookingNotApproved));

        assertEquals(bookingService.getBookingsOneBooker(bookerId, "REJECTED", 0, 10),
                List.of(responseBookingNotApprovedDto));
    }

    @Test
    void testGetBookingsOneOwner() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        Mockito.when(itemRepository.existsAllByOwnerId(Mockito.anyLong()))
                .thenReturn(true);
        Mockito.when(bookingRepository.allBookersByOwnerId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(getBookingPage(bookingApproved));

        assertEquals(bookingService.getBookingsOneOwner(bookerId, "ALL", 0, 10),
                List.of(responseBookingApprovedDto));
    }

    @Test
    void testGetBookingsOneOwnerStateCurrent() {
        LocalDateTime currentBookingStart = LocalDateTime.of(2023, 9, 1, 12, 0);
        LocalDateTime currentBookingEnd = LocalDateTime.of(2023, 9, 30, 12, 0);
        bookingApproved.setStart(currentBookingStart);
        bookingApproved.setEnd(currentBookingEnd);
        responseBookingApprovedDto.setStart(currentBookingStart);
        responseBookingApprovedDto.setEnd(currentBookingEnd);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        Mockito.when(itemRepository.existsAllByOwnerId(Mockito.anyLong()))
                .thenReturn(true);
        Mockito.when(bookingRepository.currentBookersByOwnerId(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(getBookingPage(bookingApproved));

        assertEquals(bookingService.getBookingsOneOwner(bookerId, "CURRENT", 0, 10),
                List.of(responseBookingApprovedDto));
    }

    @Test
    void testGetBookingsOneOwnerStatePast() {
        LocalDateTime currentBookingStart = LocalDateTime.of(2023, 8, 1, 12, 0);
        LocalDateTime currentBookingEnd = LocalDateTime.of(2023, 8, 31, 12, 0);
        bookingApproved.setStart(currentBookingStart);
        bookingApproved.setEnd(currentBookingEnd);
        responseBookingApprovedDto.setStart(currentBookingStart);
        responseBookingApprovedDto.setEnd(currentBookingEnd);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        Mockito.when(itemRepository.existsAllByOwnerId(Mockito.anyLong()))
                .thenReturn(true);
        Mockito.when(bookingRepository.pastBookersByOwnerId(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(getBookingPage(bookingApproved));

        assertEquals(bookingService.getBookingsOneOwner(bookerId, "PAST", 0, 10),
                List.of(responseBookingApprovedDto));
    }

    @Test
    void testGetBookingsOneOwnerStateFuture() {
        LocalDateTime currentBookingStart = LocalDateTime.of(2023, 10, 1, 12, 0);
        LocalDateTime currentBookingEnd = LocalDateTime.of(2023, 10, 31, 12, 0);
        bookingApproved.setStart(currentBookingStart);
        bookingApproved.setEnd(currentBookingEnd);
        responseBookingApprovedDto.setStart(currentBookingStart);
        responseBookingApprovedDto.setEnd(currentBookingEnd);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        Mockito.when(itemRepository.existsAllByOwnerId(Mockito.anyLong()))
                .thenReturn(true);
        Mockito.when(bookingRepository.futureBookersByOwnerId(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(getBookingPage(bookingApproved));

        assertEquals(bookingService.getBookingsOneOwner(bookerId, "FUTURE", 0, 10),
                List.of(responseBookingApprovedDto));
    }

    @Test
    void testGetBookingsOneOwnerStateWaiting() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        Mockito.when(itemRepository.existsAllByOwnerId(Mockito.anyLong()))
                .thenReturn(true);
        Mockito.when(bookingRepository.bookersByStatusAndOwnerId(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(getBookingPage(booking));

        assertEquals(bookingService.getBookingsOneOwner(bookerId, "WAITING", 0, 10),
                List.of(responseBookingDto));
    }

    @Test
    void testGetBookingsOneOwnerStateRejected() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        Mockito.when(itemRepository.existsAllByOwnerId(Mockito.anyLong()))
                .thenReturn(true);
        Mockito.when(bookingRepository.bookersByStatusAndOwnerId(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(getBookingPage(bookingNotApproved));

        assertEquals(bookingService.getBookingsOneOwner(bookerId, "REJECTED", 0, 10),
                List.of(responseBookingNotApprovedDto));
    }

}