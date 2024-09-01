package ru.practicum.shareit.booking.service.state.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.state.BookingRequest;
import ru.practicum.shareit.booking.service.state.owner.handler.OwnerAllBookingsHandlerBooking;
import ru.practicum.shareit.booking.service.state.owner.handler.OwnerBookingStateHandler;
import ru.practicum.shareit.booking.service.state.owner.handler.OwnerCurrentBookingsHandlerBooking;
import ru.practicum.shareit.booking.service.state.owner.handler.OwnerFutureBookingsHandlerBooking;
import ru.practicum.shareit.booking.service.state.owner.handler.OwnerPastBookingsHandlerBooking;
import ru.practicum.shareit.booking.service.state.owner.handler.OwnerRejectedBookingsHandlerBooking;
import ru.practicum.shareit.booking.service.state.owner.handler.OwnerWaitingBookingsHandlerBooking;

import java.util.List;

@Component
public class OwnerBookingHandlerChain {
    private final OwnerBookingStateHandler firstHandler;

    @Autowired
    public OwnerBookingHandlerChain(OwnerAllBookingsHandlerBooking allHandler,
                                    OwnerCurrentBookingsHandlerBooking currentHandler,
                                    OwnerPastBookingsHandlerBooking pastHandler,
                                    OwnerFutureBookingsHandlerBooking futureHandler,
                                    OwnerWaitingBookingsHandlerBooking waitingHandler,
                                    OwnerRejectedBookingsHandlerBooking rejectedHandler) {
        allHandler.setNext(currentHandler);
        currentHandler.setNext(pastHandler);
        pastHandler.setNext(futureHandler);
        futureHandler.setNext(waitingHandler);
        waitingHandler.setNext(rejectedHandler);

        this.firstHandler = allHandler;
    }

    public List<Booking> handle(BookingRequest request) {
        return firstHandler.handle(request);
    }
}
