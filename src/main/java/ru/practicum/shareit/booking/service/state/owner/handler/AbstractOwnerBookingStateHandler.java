package ru.practicum.shareit.booking.service.state.owner.handler;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.state.BookingRequest;

import java.util.Collections;
import java.util.List;

public abstract class AbstractOwnerBookingStateHandler implements OwnerBookingStateHandler {
    protected OwnerBookingStateHandler nextHandler;

    @Override
    public void setNext(OwnerBookingStateHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public List<Booking> handle(BookingRequest request) {
        if (nextHandler != null) {
            return nextHandler.handle(request);
        }
        return Collections.emptyList();
    }
}
