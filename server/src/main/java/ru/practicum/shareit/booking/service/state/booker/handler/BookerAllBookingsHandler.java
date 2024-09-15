package ru.practicum.shareit.booking.service.state.booker.handler;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.state.BookingRequest;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookerAllBookingsHandler extends AbstractBookingStateHandler {
    private final BookingRepository bookingRepository;

    @Override
    public List<Booking> handle(BookingRequest request) {
        if (request.getState() == BookingState.ALL) {
            return bookingRepository.findAllByBookerIdOrderByStartDesc(request.getUserId());
        }
        return super.handle(request);
    }
}