package ru.practicum.shareit.booking.service.state.booker.handler;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.state.BookingRequest;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookerPastBookingsHandler extends AbstractBookingStateHandler {
    private final BookingRepository bookingRepository;

    @Override
    public List<Booking> handle(BookingRequest request) {
        if (request.getState() == BookingState.PAST) {
            return bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(
                    request.getUserId(), LocalDateTime.now());
        }
        return super.handle(request);
    }
}