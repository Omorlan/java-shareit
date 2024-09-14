package ru.practicum.shareit.booking.service.state.booker.handler;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.state.BookingRequest;

import java.util.List;

public interface BookingStateHandler {

    List<Booking> handle(BookingRequest request);

    void setNext(BookingStateHandler nextHandler);
}
