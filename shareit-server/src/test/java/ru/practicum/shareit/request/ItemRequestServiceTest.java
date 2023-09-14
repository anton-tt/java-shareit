package test.java.ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.FullResponseItemRequestDto;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private final long requestorId = 10;
    private final String requestorName = "requestorUser";
    private final String requestorMail = "requestor@mail.com";
    private final User requestor = User.builder().id(requestorId).name(requestorName).email(requestorMail).build();
    private final ResponseUserDto responseUserDto = ResponseUserDto.builder().id(requestorId).name(requestorName)
            .email(requestorMail).build();

    private final long ownerId = 12;
    private final String ownerName = "ownerUser";
    private final String ownerMail = "owner@mail.com";
    private final User owner = User.builder().id(ownerId).name(ownerName).email(ownerMail).build();

    private final long requestId = 50;
    private final String requestDescription = "testTestTestTestTest";
    private final LocalDateTime requestDateTime = LocalDateTime.of(2023, 9, 10, 12, 0);
    RequestItemRequestDto requestItemRequestDto = RequestItemRequestDto.builder().description(requestDescription).build();
    ItemRequest itemRequest = ItemRequest.builder().id(requestId).description(requestDescription)
            .created(requestDateTime).requestor(requestor).build();
    ResponseItemRequestDto responseItemRequestDto = ResponseItemRequestDto.builder().id(requestId)
            .description(requestDescription).created(requestDateTime).requestor(responseUserDto).build();
    FullResponseItemRequestDto fullResponseItemRequestDto = FullResponseItemRequestDto.builder().id(requestId)
            .description(requestDescription).created(requestDateTime).build();

    private Page<ItemRequest> getItemRequestPage(ItemRequest itemRequest) {
        List<ItemRequest> itemRequestList = new ArrayList<>();
        itemRequestList.add(itemRequest);
        Page<ItemRequest> itemRequests = new PageImpl<>(itemRequestList);
        return itemRequests;
    }

    private final long itemId = 15;
    private final String itemName = "itemItem";
    private final String itemDescription = "itemItemItemItemItem";
    private final Item item = Item.builder().id(itemId).name(itemName).description(itemDescription).available(true)
            .owner(owner).itemRequest(itemRequest).build();
    private final ResponseItemDto responseItemDto = ResponseItemDto.builder().id(itemId).name(itemName)
            .description(itemDescription).available(true).requestId(requestId).build();

    private List<Item> getItemList(Item item) {
        List<Item> oneRequestItems = new ArrayList<>();
        oneRequestItems.add(item);
        return oneRequestItems;
    }

    private List<ResponseItemDto> getResponseItemDtoList(ResponseItemDto responseItemDto) {
        List<ResponseItemDto> oneRequestResponseItemsDto = new ArrayList<>();
        oneRequestResponseItemsDto.add(responseItemDto);
        return oneRequestResponseItemsDto;
    }

    private final int from = 0;
    private final int size = 10;

    @Test
    void testCreateItemRequest() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(requestor));
        Mockito.when(itemRequestRepository.save(Mockito.any()))
                .thenReturn(itemRequest);

        assertEquals(itemRequestService.create(requestItemRequestDto, requestId), responseItemRequestDto);
    }

    @Test
    void testGetItemRequestById() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(requestor));
        Mockito.when(itemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(itemRequest));

        Mockito.when(itemRepository.findByItemRequestId(Mockito.anyLong()))
                .thenReturn(List.of(item));

        fullResponseItemRequestDto.setItems(getResponseItemDtoList(responseItemDto));
        assertEquals(itemRequestService.getById(requestId, ownerId), fullResponseItemRequestDto);
    }

    @Test
    void testGetOwnItemRequests() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(requestor));
        Mockito.when(itemRequestRepository.existsAllByRequestorId(Mockito.anyLong()))
                .thenReturn(true);
        Mockito.when(itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(Mockito.anyLong()))
                .thenReturn(List.of(itemRequest));
        Mockito.when(itemRepository.findAllByItemRequestIdIn(Mockito.any()))
                .thenReturn(List.of(item));

        fullResponseItemRequestDto.setItems(getResponseItemDtoList(responseItemDto));
        assertEquals(itemRequestService.getOwnItemRequests(requestorId),
                List.of(fullResponseItemRequestDto));
    }

    @Test
    void testGetAllItemRequests() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        Mockito.when(itemRequestRepository.findAllByRequestorIdNot(Mockito.anyLong(), Mockito.any()))
                .thenReturn(getItemRequestPage(itemRequest));
        Mockito.when(itemRepository.findByItemRequestIdIsNotNull())
                .thenReturn(List.of(item));

        fullResponseItemRequestDto.setItems(getResponseItemDtoList(responseItemDto));
        assertEquals(itemRequestService.getAll(ownerId, from, size),
               List.of(fullResponseItemRequestDto));
    }

}