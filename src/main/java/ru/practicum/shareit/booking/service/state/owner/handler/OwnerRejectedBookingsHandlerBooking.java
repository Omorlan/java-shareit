package ru.practicum.shareit.booking.service.state.owner.handler;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.state.BookingRequest;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OwnerRejectedBookingsHandlerBooking extends AbstractOwnerBookingStateHandler {
    private final BookingRepository bookingRepository;

    @Override
    public List<Booking> handle(BookingRequest request) {
        if (request.getState() == BookingState.REJECTED) {
            return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(request.getUserId(), BookingStatus.REJECTED);
        }
        return super.handle(request);
    }
}
