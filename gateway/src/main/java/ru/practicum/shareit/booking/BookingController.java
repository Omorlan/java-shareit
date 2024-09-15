package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.common.Headers;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(Headers.USER_HEADER) Long userId,
                                             @Valid @RequestBody BookingCreateDto bookingDto) {
        return bookingClient.addBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader(Headers.USER_HEADER) Long userId,
                                                 @PathVariable Long bookingId,
                                                 @RequestParam boolean approved) {
        return bookingClient.bookingApproved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(Headers.USER_HEADER) Long userId,
                                             @PathVariable Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader(Headers.USER_HEADER) Long userId,
                                              @RequestParam(required = false, defaultValue = "ALL")
                                              String state) {
        BookingState stateParam = BookingState.parse(state);
        return bookingClient.getBookings(userId, stateParam);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsOwner(@RequestHeader(Headers.USER_HEADER) Long userId,
                                                   @RequestParam(required = false, defaultValue = "ALL")
                                                   String state) {
        BookingState stateParam = BookingState.parse(state);
        return bookingClient.getBookingsOwner(userId, stateParam);
    }

}
