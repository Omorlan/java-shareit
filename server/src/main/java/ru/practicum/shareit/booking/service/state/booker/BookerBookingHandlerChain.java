package ru.practicum.shareit.booking.service.state.booker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.state.BookingRequest;
import ru.practicum.shareit.booking.service.state.booker.handler.BookerAllBookingsHandler;
import ru.practicum.shareit.booking.service.state.booker.handler.BookerCurrentBookingsHandler;
import ru.practicum.shareit.booking.service.state.booker.handler.BookerFutureBookingsHandler;
import ru.practicum.shareit.booking.service.state.booker.handler.BookerPastBookingsHandler;
import ru.practicum.shareit.booking.service.state.booker.handler.BookerRejectedBookingsHandler;
import ru.practicum.shareit.booking.service.state.booker.handler.BookerWaitingBookingsHandler;
import ru.practicum.shareit.booking.service.state.booker.handler.BookingStateHandler;

import java.util.List;

@Component
public class BookerBookingHandlerChain {
    private final BookingStateHandler firstHandler;

    @Autowired
    public BookerBookingHandlerChain(BookerAllBookingsHandler allHandler,
                                     BookerCurrentBookingsHandler currentHandler,
                                     BookerPastBookingsHandler pastHandler,
                                     BookerFutureBookingsHandler futureHandler,
                                     BookerWaitingBookingsHandler waitingHandler,
                                     BookerRejectedBookingsHandler rejectedHandler) {
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
