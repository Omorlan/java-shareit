package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.Headers;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;


    @PostMapping
    public BookingResponseDto addBooking(@RequestHeader(Headers.USER_HEADER) Long userId,
                                         @Valid @RequestBody BookingCreateDto bookingDto) {
        return bookingService.addBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@RequestHeader(Headers.USER_HEADER) Long userId,
                                             @PathVariable Long bookingId,
                                             @RequestParam boolean approved) {
        return bookingService.bookingApproved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@RequestHeader(Headers.USER_HEADER) Long userId,
                                         @PathVariable Long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingShortResponseDto> getBookings(@RequestHeader(Headers.USER_HEADER) Long userId,
                                                     @RequestParam(required = false, defaultValue = Headers.DEFAULT_STATE)
                                                     String state) {
        return bookingService.getBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingShortResponseDto> getBookingsOwner(@RequestHeader(Headers.USER_HEADER) Long userId,
                                                          @RequestParam(required = false, defaultValue = Headers.DEFAULT_STATE)
                                                          String state) {
        return bookingService.getBookingsOwner(userId, state);
    }
}
