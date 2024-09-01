package ru.practicum.shareit.booking.service.state.booker.handler;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.state.BookingRequest;

import java.util.Collections;
import java.util.List;


public abstract class AbstractBookingStateHandler implements BookingStateHandler {
    protected BookingStateHandler nextHandler;

    @Override
    public void setNext(BookingStateHandler nextHandler) {
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