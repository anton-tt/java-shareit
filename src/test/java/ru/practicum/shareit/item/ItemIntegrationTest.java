package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.ItemBookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.FullResponseItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@Rollback
@SpringBootTest(properties = "db.name=test", webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemIntegrationTest {

    private final EntityManager em;
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;
    private final BookingServiceImpl bookingService;

    private final long ownerId = 1L;
    private final String ownerName = "ownerUser";
    private final String ownerMail = "owner@mail.com";
    private final RequestUserDto requestOwnerDto = RequestUserDto.builder().name(ownerName).email(ownerMail).build();
    private final ResponseUserDto responseOwnerDto = ResponseUserDto.builder().id(ownerId).name(ownerName)
            .email(ownerMail).build();

    private final long oneBookerId = 12;
    //private final User oneBooker = User.builder().id(testOneBookerId).name("oneBooker").email("oneBooker@mail.com").build();

    private final long twoBookerId = 13;
    // private final User twoBooker = User.builder().id(testTwoBookerId).name("twoBooker").email("twoBooker@mail.com").build();

    private final long itemId = 1L;
    private final long oneBookingId = 1;
    private final LocalDateTime startOneBooking = LocalDateTime.of(2023, 9, 1, 12,
            10, 10);
    private final LocalDateTime endOneBooking = LocalDateTime.of(2023, 9, 10, 12,
            10, 10);
    //private final Booking oneBooking = Booking.builder().id(oneBookingId).start(startOneBooking).end(endOneBooking)
    //     .status(Status.APPROVED).item(item).booker(oneBooker).build();
    private final ItemBookingDto oneBookingDto = ItemBookingDto.builder().id(oneBookingId).start(startOneBooking)
            .end(endOneBooking).status(Status.APPROVED).itemId(itemId).bookerId(oneBookerId).build();

    private final long twoBookingId = 2;
    private final LocalDateTime startTwoBooking = LocalDateTime.of(2023, 9, 11, 12,
            10,10);
    private final LocalDateTime endTwoBooking = LocalDateTime.of(2023, 9, 20, 12,
            10,10);
    //private final Booking twoBooking = Booking.builder().id(twoBookingId).start(startTwoBooking).end(endTwoBooking)
    //  .status(Status.APPROVED).item(item).booker(twoBooker).build();
    private final ItemBookingDto twoBookingDto = ItemBookingDto.builder().id(twoBookingId).start(startTwoBooking)
            .end(endTwoBooking).status(Status.APPROVED).itemId(itemId).bookerId(twoBookerId).build();

    private final String itemName = "itemTest";
    private final String itemDescription = "itemTestTestTest";
    private final RequestItemDto requestItemDto = RequestItemDto.builder().name(itemName).description(itemDescription)
            .available(true).requestId(0).build();
    private final ResponseItemDto responseItemDto = ResponseItemDto.builder().id(itemId).name(itemName)
            .description(itemDescription).available(true).requestId(0).build();
    private final FullResponseItemDto fullResponseItemDto = FullResponseItemDto.builder().id(itemId).name(itemName)
            .description(itemDescription).available(true)/*.lastBooking(oneBookingDto).nextBooking(twoBookingDto)*/.build();
    private final String newItemName = "newItemTest";
    private final String newItemDescription = "newItemTestTestTest";
    private final RequestItemDto newDataItemDto = RequestItemDto.builder().name(newItemName)
            .description(newItemDescription).available(true).requestId(0).build();
    private final ResponseItemDto newResponseItemDto = ResponseItemDto.builder().id(itemId).name(newItemName)
            .description(newItemDescription).available(true).requestId(0).build();


    @Test
    void createUserTest() {
        userService.create(requestOwnerDto);
        itemService.create(requestItemDto, ownerId);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.name = :name", Item.class);
        Item item = query.setParameter("name", requestItemDto.getName()).getSingleResult();

        assertThat(item.getId(), equalTo(itemId));
        assertThat(item.getOwner().getId(), equalTo(ownerId));
        assertThat(item.getName(), equalTo(requestItemDto.getName()));
        assertThat(item.getDescription(), equalTo(requestItemDto.getDescription()));
        assertThat(item.isAvailable(), equalTo(requestItemDto.getAvailable()));
    }

    @Test
    void getItemByIdTest() {
        userService.create(requestOwnerDto);
        itemService.create(requestItemDto, ownerId);
        FullResponseItemDto responseItemDto = itemService.getById(itemId, ownerId);

        assertThat(responseItemDto.getId(), equalTo(itemId));
        assertThat(responseItemDto.getName(), equalTo(requestItemDto.getName()));
        assertThat(responseItemDto.getDescription(), equalTo(requestItemDto.getDescription()));
        assertThat(responseItemDto.getAvailable(), equalTo(requestItemDto.getAvailable()));
    }

    @Test
    void getItemsOneOwnerTest() {
        userService.create(requestOwnerDto);
        itemService.create(requestItemDto, ownerId);
        List<FullResponseItemDto> responseItemDtoList = itemService.getItemsOneOwner(ownerId, 0,10);
        FullResponseItemDto responseUserDto = responseItemDtoList.get(0);

        assertThat(responseItemDtoList.size(),  equalTo(1));
        assertThat(responseItemDto.getName(), equalTo(requestItemDto.getName()));
        assertThat(responseItemDto.getDescription(), equalTo(requestItemDto.getDescription()));
        assertThat(responseItemDto.getAvailable(), equalTo(requestItemDto.getAvailable()));
    }

    @Test
    void updateItemTest() {
        userService.create(requestOwnerDto);
        itemService.create(requestItemDto, ownerId);

        List<FullResponseItemDto> responseItemDtoList = itemService.getItemsOneOwner(ownerId, 0,10);
        assertThat(responseItemDtoList.size(),  equalTo(1));
        assertThat(responseItemDtoList.get(0).getName(), equalTo(requestItemDto.getName()));

        itemService.update(itemId, newDataItemDto, ownerId);
        responseItemDtoList = itemService.getItemsOneOwner(ownerId, 0,10);
        assertThat(responseItemDtoList.size(),  equalTo(1));
        assertThat(responseItemDtoList.get(0).getName(), equalTo(newDataItemDto.getName()));
    }

    @Test
    void deleteItemTest() {
        userService.create(requestOwnerDto);
        itemService.create(requestItemDto, ownerId);

        List<FullResponseItemDto> responseItemDtoList = itemService.getItemsOneOwner(ownerId, 0,10);
        assertThat(responseItemDtoList.size(),  equalTo(1));
        assertThat(responseItemDtoList.get(0).getName(), equalTo(requestItemDto.getName()));

        itemService.delete(itemId, ownerId);
        responseItemDtoList = itemService.getItemsOneOwner(ownerId, 0,10);
        assertThat(responseItemDtoList.size(),  equalTo(0));
    }

}