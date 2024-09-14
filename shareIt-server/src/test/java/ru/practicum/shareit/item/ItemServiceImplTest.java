package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.exception.NotAvailableException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentAddDto;
import ru.practicum.shareit.item.dto.ItemAddDto;
import ru.practicum.shareit.item.dto.ItemCommentNextLastDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;

@MockitoSettings(strictness = LENIENT)
@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    BookingRepository bookingRepository;


    @Mock
    ItemRequestRepository requestRepository;

    @InjectMocks
    ItemServiceImpl itemService;

    private Item item1, item2;
    private final User owner = User.builder()
            .id(1L)
            .name("Oleg Gazmanov")
            .email("vpole.skonem@viydu.ru")
            .build();

    @BeforeEach
    void setUp() {
        item1 = Item.builder()
                .id(1L)
                .name("Item 1")
                .description("Item 1")
                .available(true)
                .owner(owner)
                .request(null)
                .build();
        item2 = Item.builder()
                .id(2L)
                .name("Item 2")
                .description("Item 2")
                .available(true)
                .owner(owner)
                .request(null)
                .build();
    }

    @Test
    void createShouldReturnCreatedItem() {
        when(itemRepository.save(any())).thenReturn(item1);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(requestRepository.findById(anyLong())).thenReturn(Optional.empty());

        ItemAddDto addDto = ItemAddDto.builder()
                .name(item1.getName())
                .description(item1.getDescription())
                .available(item1.getAvailable())
                .requestId(null)
                .build();
        ItemDto itemDto = itemService.create(owner.getId(), addDto);
        assertNotNull(itemDto);
        assertThrows(NotFoundException.class, () -> itemService.create(3L, addDto));
    }

    @Test
    void updateShouldReturnUpdatedItem() {
        when(itemRepository.save(any())).thenReturn(item1);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(item1.getId())).thenReturn(Optional.of(item1));
        ItemUpdateDto updateDto = ItemUpdateDto.builder()
                .name("Updated Item Name")
                .build();
        ItemDto itemDto = itemService.update(owner.getId(), item1.getId(), updateDto);
        assertEquals(itemDto.getId(), item1.getId());
        assertThrows(NotFoundException.class, () -> itemService.update(3L, item1.getId(), updateDto));
        assertThrows(NotFoundException.class, () -> itemService.update(owner.getId(), 5L, updateDto));
    }

    @Test
    void findItemByIdShouldReturnItemDetails() {
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(item1.getId())).thenReturn(Optional.of(item1));
        when(bookingRepository.findAllByItemIdAndItemOwnerIdAndStatusNotIn(
                anyLong(), anyLong(), any()))
                .thenReturn(Collections.emptyList());
        ItemCommentNextLastDto itemDto = itemService.findItemById(item1.getId(), owner.getId());
        assertEquals(itemDto.getId(), item1.getId());
        assertThrows(NotFoundException.class, () -> itemService.findItemById(3L, owner.getId()));
        assertThrows(NotFoundException.class, () -> itemService.findItemById(item1.getId(), 5L));
    }

    @Test
    void getItemsByUserShouldReturnUserItems() {
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.getItemsByUser(owner.getId())).thenReturn(List.of(item1, item2));

        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .status(BookingStatus.APPROVED)
                .item(item1)
                .booker(owner)
                .build();

        when(bookingRepository.findAllByItemInAndStatusNotIn(List.of(item1, item2), List.of(BookingStatus.REJECTED)))
                .thenReturn(List.of(booking));

        List<ItemCommentNextLastDto> allItems = itemService.getItemsByUser(owner.getId());
        assertEquals(2, allItems.size());
    }

    @Test
    void findItemsByTextShouldReturnSearchedItems() {
        when(itemRepository.searchText("text")).thenReturn(List.of(item1, item2));
        List<ItemDto> items = itemService.findItemsByText("text");
        assertEquals(2, items.size());
        items = itemService.findItemsByText("");
        assertEquals(Collections.emptyList(), items);
    }

    @Test
    void addCommentToItemShouldThrowNotAvailableException() {
        User user = User.builder()
                .id(2L)
                .name("Oleg Gazmanov")
                .email("vpole.skonem@viydu.ru")
                .build();

        when(itemRepository.findById(item1.getId())).thenReturn(Optional.of(item1));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        when(bookingRepository.findByItemIdAndBookerIdAndStatusAndEndBefore(anyLong(), anyLong(),
                any(BookingStatus.class), any(LocalDateTime.class))).thenReturn(Collections.emptyList());

        CommentAddDto addDto = new CommentAddDto();
        addDto.setText("text");

        assertThrows(NotAvailableException.class, () -> itemService.addCommentToItem(user.getId(), item1.getId(), addDto));
    }
}
