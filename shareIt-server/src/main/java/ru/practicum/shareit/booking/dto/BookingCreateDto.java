package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingCreateDto {
    @NotNull(message = "Item ID cannot be null")
    Long itemId;

    @FutureOrPresent(message = "The start date cannot be in the past")
    @NotNull(message = "Start date cannot be null")
    LocalDateTime start;

    @Future(message = "The end date must be in the future")
    @NotNull(message = "End date cannot be null")
    LocalDateTime end;
}