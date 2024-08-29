package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto addBooking(BookingCreateDto bookingDto, Long userId);

    BookingResponseDto bookingApproved(Long userId, Long bookingId, boolean approved);

    BookingResponseDto getBooking(Long userId, Long bookingId);

    List<BookingShortResponseDto> getBookings(Long userId, String state);

    List<BookingShortResponseDto> getBookingsOwner(Long userId, String state);
}
