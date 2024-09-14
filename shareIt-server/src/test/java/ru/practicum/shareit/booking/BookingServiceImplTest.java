package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.service.state.BookingRequest;
import ru.practicum.shareit.booking.service.state.booker.BookerBookingHandlerChain;
import ru.practicum.shareit.booking.service.state.owner.OwnerBookingHandlerChain;
import ru.practicum.shareit.common.exception.NotAvailableException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OwnerBookingHandlerChain ownerHandlerChain;

    @Mock
    private BookerBookingHandlerChain bookerHandlerChain;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User testBooker;
    private User testOwner;
    private Item testItem;
    private Booking testBooking;

    @BeforeEach
    void setUp() {
        testBooker = User.builder()
                .id(1L)
                .name("Alice Smith")
                .email("alice.smith@example.com")
                .build();

        testOwner = User.builder()
                .id(2L)
                .name("Bob Brown")
                .email("bob.brown@example.com")
                .build();

        testItem = Item.builder()
                .id(1L)
                .name("Item 1")
                .description("Description of Item 1")
                .available(true)
                .owner(testOwner)
                .build();

        testBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .status(BookingStatus.APPROVED)
                .item(testItem)
                .booker(testBooker)
                .build();
    }

    @Test
    void addBooking_ShouldReturnBookingResponseDto() {
        when(userRepository.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            if (id.equals(testBooker.getId())) {
                return Optional.of(testBooker);
            }
            return Optional.empty();
        });

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(testItem));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        BookingCreateDto createDto = new BookingCreateDto();
        createDto.setItemId(1L);
        createDto.setStart(LocalDateTime.now());
        createDto.setEnd(LocalDateTime.now().plusDays(1));

        BookingResponseDto responseDto = bookingService.addBooking(createDto, testBooker.getId());
        assertEquals(testBooker, responseDto.getBooker());

        assertThrows(NotFoundException.class, () -> bookingService.addBooking(createDto, 5L));
    }


    @Test
    void approveBooking_ShouldReturnApprovedBookingResponseDto() {
        Booking bookingToApprove = Booking.builder()
                .id(2L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .status(BookingStatus.WAITING)
                .item(testItem)
                .booker(testBooker)
                .build();

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingToApprove));
        when(bookingRepository.save(any(Booking.class))).thenReturn(bookingToApprove);

        BookingResponseDto responseDto = bookingService.bookingApproved(testOwner.getId(), bookingToApprove.getId(), true);

        assertEquals(bookingToApprove.getId(), responseDto.getId());
        assertEquals(BookingStatus.APPROVED, responseDto.getStatus());
        assertThrows(NotAvailableException.class, () -> bookingService.bookingApproved(
                testOwner.getId(), bookingToApprove.getId(), true));
    }

    @Test
    void getBooking_ShouldReturnBookingResponseDto() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(testBooking));

        BookingResponseDto responseDto = bookingService.getBooking(testBooker.getId(), testBooking.getId());

        assertEquals(testBooking.getId(), responseDto.getId());
    }

    @Test
    void getBookingsByBooker_ShouldReturnListOfBookingShortResponseDto() {
        when(userRepository.findById(testBooker.getId())).thenReturn(Optional.of(testBooker));
        when(bookerHandlerChain.handle(any(BookingRequest.class))).thenReturn(List.of(testBooking));


        List<BookingShortResponseDto> all = bookingService.getBookings(testBooker.getId(), "ALL");
        assertEquals(1, all.size());

        List<BookingShortResponseDto> current = bookingService.getBookings(testBooker.getId(), "CURRENT");
        assertEquals(1, current.size());
    }


    @Test
    void getBookingsByOwner_ShouldReturnListOfBookingShortResponseDto() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testOwner));

        when(ownerHandlerChain.handle(any(BookingRequest.class))).thenReturn(List.of(testBooking));

        List<BookingShortResponseDto> responseList = bookingService.getBookingsOwner(testOwner.getId(), "ALL");

        assertEquals(1, responseList.size());
    }


}
