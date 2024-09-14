package ru.practicum.shareit.booking.service.state;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.booking.enums.BookingState;

@Getter
@AllArgsConstructor
public class BookingRequest {
    private Long userId;
    private BookingState state;
}
