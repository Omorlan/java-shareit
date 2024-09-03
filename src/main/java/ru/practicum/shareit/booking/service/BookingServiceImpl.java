package ru.practicum.shareit.booking.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.state.BookingRequest;
import ru.practicum.shareit.booking.service.state.booker.BookerBookingHandlerChain;
import ru.practicum.shareit.booking.service.state.owner.OwnerBookingHandlerChain;
import ru.practicum.shareit.common.exception.NotAvailableException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    private final BookerBookingHandlerChain bookerHandlerChain;
    private final OwnerBookingHandlerChain ownerHandlerChain;

    @Override
    @Transactional
    public BookingResponseDto addBooking(BookingCreateDto bookingDto, Long userId) {
        User booker = getUserById(userId);
        Item item = getItemById(bookingDto.getItemId());
        validateBookingAvailability(item, booker);

        Booking booking = BookingMapper.toEntity(bookingDto);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingResponseDto bookingApproved(Long userId, Long bookingId, boolean approved) {
        Booking booking = getBookingById(bookingId);
        validateOwner(userId, booking);
        updateBookingStatus(booking, approved);

        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto getBooking(Long userId, Long bookingId) {
        Booking booking = getBookingById(bookingId);
        validateAccess(userId, booking);

        return BookingMapper.toDto(booking);
    }

    @Override
    public List<BookingShortResponseDto> getBookings(Long userId, String state) {
        getUserById(userId);
        BookingState bookingState = parseBookingState(state);
        List<Booking> bookings = findBookingsByState(userId, bookingState);

        return BookingMapper.toShortDtoList(bookings);
    }

    @Override
    public List<BookingShortResponseDto> getBookingsOwner(Long userId, String state) {
        getUserById(userId);
        BookingState bookingState = parseBookingState(state);
        List<Booking> bookings = findBookingsByOwnerState(userId, bookingState);

        return BookingMapper.toShortDtoList(bookings);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id " + userId + " not found"));
    }

    private Item getItemById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Item with id " + itemId + " not found"));
    }

    private Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Booking with id " + bookingId + " not found"));
    }

    private void validateBookingAvailability(Item item, User booker) {
        if (!item.getAvailable()) {
            throw new NotAvailableException("Booking is not available");
        }
        if (booker.getId().equals(item.getOwner().getId())) {
            throw new NotFoundException("Not allowed to book your item");
        }
    }

    private void validateOwner(Long userId, Booking booking) {
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotAvailableException("You do not own this item in booking");
        }
    }

    private void validateAccess(Long userId, Booking booking) {
        if (!booking.getItem().getOwner().getId().equals(userId) && !booking.getBooker().getId().equals(userId)) {
            throw new NotFoundException("You do not own this booking or item in booking");
        }
    }

    private void updateBookingStatus(Booking booking, boolean approved) {
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new NotAvailableException("Booking is already approved");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
    }

    private BookingState parseBookingState(String state) {
        try {
            return BookingState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unknown state: " + state);
        }
    }

    public List<Booking> findBookingsByState(Long userId, BookingState state) {
        BookingRequest request = new BookingRequest(userId, state);
        return bookerHandlerChain.handle(request);
    }

    private List<Booking> findBookingsByOwnerState(Long userId, BookingState state) {
        BookingRequest request = new BookingRequest(userId, state);
        return ownerHandlerChain.handle(request);
    }
}
