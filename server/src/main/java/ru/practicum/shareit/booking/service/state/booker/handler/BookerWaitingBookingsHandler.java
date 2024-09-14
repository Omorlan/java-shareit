package ru.practicum.shareit.booking.service.state.booker.handler;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.state.BookingRequest;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookerWaitingBookingsHandler extends AbstractBookingStateHandler {
    private final BookingRepository bookingRepository;

    @Override
    public List<Booking> handle(BookingRequest request) {
        if (request.getState() == BookingState.WAITING) {
            return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(
                    request.getUserId(), BookingStatus.WAITING);
        }
        return super.handle(request);
    }
}