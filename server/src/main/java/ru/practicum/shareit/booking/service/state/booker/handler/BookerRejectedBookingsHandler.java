package ru.practicum.shareit.booking.service.state.booker.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.state.BookingRequest;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookerRejectedBookingsHandler extends AbstractBookingStateHandler {
    private final BookingRepository bookingRepository;

    @Override
    public List<Booking> handle(BookingRequest request) {
        if (request.getState() == BookingState.REJECTED) {
            return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(
                    request.getUserId(), BookingStatus.REJECTED);
        }
        return super.handle(request);
    }
}