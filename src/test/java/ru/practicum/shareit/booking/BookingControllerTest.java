package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utils.Constants.X_SHARER_USER_ID;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookingService bookingService;
    @Autowired
    private MockMvc mvc;

    private final long bookerId = 10L;
    private final String bookerName = "bookerUser";
    private final String bookerMail = "booker@mail.com";
    private final ResponseUserDto responseBookerDto = ResponseUserDto.builder().id(bookerId).name(bookerName)
            .email(bookerMail).build();

    private final long ownerId = 11L;

    private final long itemId = 5L;
    private final String itemName = "itemTest";
    private final String itemDescription = "itemTestTestTest";
    private final ResponseItemDto responseItemDto = ResponseItemDto.builder().id(itemId).name(itemName)
            .description(itemDescription).available(true).requestId(0).build();

    private final long bookingId = 300L;
    private final LocalDateTime bookingStartOne = LocalDateTime.now().plusDays(1).withNano(0);
    private final LocalDateTime bookingEndOne = LocalDateTime.now().plusDays(2).withNano(0);
    private final RequestBookingDto requestBookingDto = RequestBookingDto.builder().start(bookingStartOne)
            .end(bookingEndOne).itemId(itemId).build();
    private final ResponseBookingDto responseBookingDto = ResponseBookingDto.builder().id(bookingId)
            .start(bookingStartOne).end(bookingEndOne).status(Status.WAITING).item(responseItemDto)
            .booker(responseBookerDto).build();
    private final ResponseBookingDto responseBookingApprovedDto = ResponseBookingDto.builder().id(bookingId)
            .start(bookingStartOne).end(bookingEndOne).status(Status.APPROVED).item(responseItemDto)
            .booker(responseBookerDto).build();

    private List<ResponseBookingDto> getResponseBookingDtoList(ResponseBookingDto responseBookingDto) {
        List<ResponseBookingDto> responseBookingDtoList = new ArrayList<>();
        responseBookingDtoList.add(responseBookingDto);
        return responseBookingDtoList;
    }

    @Test
    void testCreateBooking() throws Exception {
        when(bookingService.create(any(), anyLong()))
                .thenReturn(responseBookingDto);

        mvc.perform(post("/bookings")
                        .header(X_SHARER_USER_ID, bookerId)
                        .content(mapper.writeValueAsString(requestBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.start", is(responseBookingDto.getStart().withNano(0)
                        .toString())))
                .andExpect(jsonPath("$.end", is(responseBookingDto.getEnd().withNano(0)
                        .toString())))
                .andExpect(jsonPath("$.status", is(responseBookingDto.getStatus().toString())))
                .andExpect(jsonPath("$.item.id", is(requestBookingDto.getItemId()), Long.class));
    }

    @Test
    void testApproveBooking() throws Exception {
        when(bookingService.approve(anyLong(), anyBoolean(), anyLong()))
                .thenReturn(responseBookingApprovedDto);

        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header(X_SHARER_USER_ID, bookerId)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Status.APPROVED.toString())));
    }

    @Test
    void testGetBookingById() throws Exception {
        when(bookingService.getById(anyLong(), anyLong()))
                .thenReturn(responseBookingDto);

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header(X_SHARER_USER_ID, bookerId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService).getById(bookingId, bookerId);
    }

    @Test
    void testGetBookingsOneUser() throws Exception {
        List<ResponseBookingDto> responseBookingDtoList = getResponseBookingDtoList(responseBookingApprovedDto);
        when(bookingService.getBookingsOneBooker(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(responseBookingDtoList);

        String bookingDtoListString = mvc.perform(get("/bookings")
                        .header(X_SHARER_USER_ID, bookerId)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(responseBookingDtoList), bookingDtoListString);
    }

    @Test
    void testGetBookingsOneOwner() throws Exception {
        List<ResponseBookingDto> responseBookingDtoList = getResponseBookingDtoList(responseBookingApprovedDto);
        when(bookingService.getBookingsOneOwner(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(responseBookingDtoList);

        String bookingDtoListString = mvc.perform(get("/bookings/owner")
                        .header(X_SHARER_USER_ID, ownerId)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(responseBookingDtoList), bookingDtoListString);
    }

}