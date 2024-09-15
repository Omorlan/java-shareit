package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.common.exception.ErrorHandler;
import ru.practicum.shareit.common.exception.NotAvailableException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ErrorHandlerTest {

    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void handleNotFound() {
        var ex = new NotFoundException("Object not found!");
        var result = errorHandler.handleNotFound(ex);
        assertNotNull(result);
        assertEquals("Object not found!", result.getError());
        assertEquals(ex.getMessage(), result.getDescription());
    }

    @Test
    void handleValidation() {
        var ex = new ValidationException("Validation error occurred!");
        var result = errorHandler.handleValidation(ex);
        assertNotNull(result);
        assertEquals("CONFLICT Error!", result.getError());
        assertEquals(ex.getMessage(), result.getDescription());
    }

    @Test
    void handleRuntimeException() {
        var ex = new RuntimeException("Runtime error occurred!");
        var result = errorHandler.handleRuntimeException(ex);
        assertNotNull(result);
        assertEquals("Internal Error!", result.getError());
        assertEquals(ex.getMessage(), result.getDescription());
    }

    @Test
    void handleNotAvailableException() {
        var ex = new NotAvailableException("Resource not available!");
        var result = errorHandler.handleNotAvailableException(ex);
        assertNotNull(result);
        assertEquals("Bad Request Error!", result.getError());
        assertEquals(ex.getMessage(), result.getDescription());
    }
}
