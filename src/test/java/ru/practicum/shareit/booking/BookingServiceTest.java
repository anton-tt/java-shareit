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

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private final long bookerId = 10;
    private final String bookerName = "bookerUser";
    private final String bookerMail = "booker@mail.com";
    private final User booker = User.builder().id(bookerId).name(bookerName).email(bookerMail).build();
    private final ResponseUserDto responseBookerDto = ResponseUserDto.builder().id(bookerId).name(bookerName)
            .email(bookerMail).build();

    private final long ownerId = 11;
    private final String ownerName = "ownerUser";
    private final String ownerMail = "owner@mail.com";
    private final User owner = User.builder().id(ownerId).name(ownerName).email(ownerMail).build();

    private final long itemId = 5;
    private final String itemName = "itemTest";
    private final String itemDescription = "itemTestTestTest";
    private final Item item = Item.builder().id(itemId).name(itemName).description(itemDescription).available(true)
            .owner(owner).build();
    private final ResponseItemDto responseItemDto = ResponseItemDto.builder().id(itemId).name(itemName)
            .description(itemDescription).available(true).requestId(0).build();

    private final long bookingId = 300;
    private final LocalDateTime bookingStartOne = LocalDateTime.of(2023, 9, 11, 12, 0);
    private final LocalDateTime bookingEndOne = LocalDateTime.of(2023, 9, 12, 12, 0);
    private final RequestBookingDto requestBookingDto = RequestBookingDto.builder().start(bookingStartOne)
            .end(bookingEndOne).itemId(itemId).build();
    private final Booking booking = Booking.builder().id(bookingId).start(bookingStartOne).end(bookingEndOne)
            .status(Status.WAITING).item(item).booker(booker).build();
    private final Booking bookingApproved = Booking.builder().id(bookingId).start(bookingStartOne).end(bookingEndOne)
            .status(Status.APPROVED).item(item).booker(booker).build();
    private final ResponseBookingDto responseBookingDto = ResponseBookingDto.builder().id(bookingId)
            .start(bookingStartOne).end(bookingEndOne).status(Status.WAITING).item(responseItemDto)
            .booker(responseBookerDto).build();
    private final ResponseBookingDto responseBookingApprovedDto = ResponseBookingDto.builder().id(bookingId)
            .start(bookingStartOne).end(bookingEndOne).status(Status.APPROVED).item(responseItemDto)
            .booker(responseBookerDto).build();

    private Page<Booking> getBookingPage(Booking booking) {
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        Page<Booking> bookings = new PageImpl<>(bookingList);
        return bookings;
    }

    private List<ResponseBookingDto> getResponseBookingDtoList(ResponseBookingDto responseBookingDto) {
        List<ResponseBookingDto> responseBookingDtoList = new ArrayList<>();
        responseBookingDtoList.add(responseBookingDto);
        return responseBookingDtoList;
    }

    @Test
    void createBookingTest() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));
        Mockito.when(bookingRepository.save(Mockito.any()))
                .thenReturn(booking);

        assertEquals(bookingService.create(requestBookingDto, bookerId), responseBookingDto);
    }

    @Test
    void approveBookingTest() {
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
    void getBookingByIdTest() {
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
    void getBookingsOneBookerTest() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        Mockito.when(bookingRepository.findAllByBookerId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(getBookingPage(bookingApproved));

        assertEquals(bookingService.getBookingsOneBooker(bookerId, "ALL", 0, 10),
                getResponseBookingDtoList(responseBookingApprovedDto));
    }

    @Test
    void getBookingsOneOwnerTest() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        Mockito.when(itemRepository.existsAllByOwnerId(Mockito.anyLong()))
                .thenReturn(true);
        Mockito.when(bookingRepository.allBookersByOwnerId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(getBookingPage(bookingApproved));

        assertEquals(bookingService.getBookingsOneOwner(bookerId, "ALL", 0, 10),
                getResponseBookingDtoList(responseBookingApprovedDto));
    }

}