package ru.practicum.shareit.booking;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingCreateDtoJsonTest {

    @Autowired
    private JacksonTester<BookingCreateDto> json;

    private static final String DATE_TIME_START = "1999-04-06T07:33:00";
    private static final String DATE_TIME_END = "1999-04-06T07:33:00";
    private static final Long ITEM_ID = 1L;

    private BookingCreateDto bookingDto;

    @BeforeEach
    void setUp() {
        bookingDto = new BookingCreateDto();
        bookingDto.setStart(LocalDateTime.parse(DATE_TIME_START));
        bookingDto.setItemId(ITEM_ID);
        bookingDto.setEnd(LocalDateTime.parse(DATE_TIME_END));
    }

    @Test
    @SneakyThrows
    void startFieldSerializesCorrectly() {
        JsonContent<BookingCreateDto> jsonContent = json.write(bookingDto);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start")
                .isEqualTo(DATE_TIME_START);
    }

    @Test
    @SneakyThrows
    void endFieldSerializesCorrectly() {
        JsonContent<BookingCreateDto> jsonContent = json.write(bookingDto);
        assertThat(jsonContent).extractingJsonPathStringValue("$.end")
                .isEqualTo(DATE_TIME_END);
    }

    @Test
    @SneakyThrows
    void itemIdFieldSerializesCorrectly() {
        JsonContent<BookingCreateDto> jsonContent = json.write(bookingDto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo(ITEM_ID.intValue());
    }


    @Test
    @SneakyThrows
    void deserializesCorrectly() {
        String content = "{ \"itemId\": " + ITEM_ID + ", \"start\": \"" + DATE_TIME_START + "\", \"end\": \"" + DATE_TIME_END + "\" }";
        BookingCreateDto deserializedDto = json.parse(content).getObject();
        assertThat(deserializedDto.getItemId()).isEqualTo(ITEM_ID);
        assertThat(deserializedDto.getStart()).isEqualTo(LocalDateTime.parse(DATE_TIME_START));
        assertThat(deserializedDto.getEnd()).isEqualTo(LocalDateTime.parse(DATE_TIME_END));
    }

    @Test
    void handlesNullValuesGracefully() throws IOException {
        BookingCreateDto dtoWithNulls = new BookingCreateDto();
        dtoWithNulls.setItemId(null);
        dtoWithNulls.setStart(null);
        dtoWithNulls.setEnd(null);

        JsonContent<BookingCreateDto> jsonContent = json.write(dtoWithNulls);
        assertThat(jsonContent).extractingJsonPathValue("$.itemId").isNull();
        assertThat(jsonContent).extractingJsonPathValue("$.start").isNull();
        assertThat(jsonContent).extractingJsonPathValue("$.end").isNull();
    }
}
