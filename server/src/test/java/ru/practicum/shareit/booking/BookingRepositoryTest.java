package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookingRepositoryTest {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private final User booker = User.builder()
            .name("name")
            .email("email@email.com")
            .build();

    private final User owner = User.builder()
            .name("name2")
            .email("email2@email.com")
            .build();

    private final Item item = Item.builder()
            .name("name")
            .description("description")
            .available(true)
            .owner(owner)
            .build();

    private final Booking booking = Booking.builder()
            .item(item)
            .booker(booker)
            .status(BookingStatus.APPROVED)
            .start(LocalDateTime.now().minusHours(1L))
            .end(LocalDateTime.now().plusDays(1L))
            .build();

    private final Booking pastBooking = Booking.builder()
            .item(item)
            .booker(booker)
            .status(BookingStatus.APPROVED)
            .start(LocalDateTime.now().minusDays(2L))
            .end(LocalDateTime.now().minusDays(1L))
            .build();

    private final Booking futureBooking = Booking.builder()
            .item(item)
            .booker(booker)
            .status(BookingStatus.APPROVED)
            .start(LocalDateTime.now().plusDays(1L))
            .end(LocalDateTime.now().plusDays(2L))
            .build();

    @BeforeEach
    public void init() {
        testEntityManager.persist(booker);
        testEntityManager.persist(owner);
        testEntityManager.persist(item);
        testEntityManager.flush();
        bookingRepository.save(booking);
        bookingRepository.save(pastBooking);
        bookingRepository.save(futureBooking);
    }

    @AfterEach
    public void deleteAll() {
        bookingRepository.deleteAll();
    }

    @Test
    void findBookingsByBookerIdOrderedByStartDesc_ReturnsBookings() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(booker.getId());
        assertEquals(3, bookings.size());
        assertEquals(booker.getId(), bookings.get(0).getBooker().getId());
    }

    @Test
    void findBookingsByBookerIdOrderedByStartDesc_ReturnsEmptyListWhenNoBookings() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(999L);
        assertEquals(0, bookings.size());
    }

    @Test
    void findBookingsByBookerIdAndEndBeforeOrderedByStartDesc_ReturnsBookings() {
        List<Booking> bookings = bookingRepository
                .findAllByBookerIdAndEndBeforeOrderByStartDesc(booker.getId(), LocalDateTime.now());
        assertEquals(1, bookings.size());
        assertEquals(booker.getId(), bookings.get(0).getBooker().getId());
    }

    @Test
    void findBookingsByBookerIdAndEndBeforeOrderedByStartDesc_ReturnsEmptyListWhenNoBookings() {
        List<Booking> bookings = bookingRepository
                .findAllByBookerIdAndEndBeforeOrderByStartDesc(booker.getId(), LocalDateTime.now().minusDays(5));
        assertEquals(0, bookings.size());
    }

    @Test
    void findBookingsByBookerIdAndStartAfterOrderedByStartDesc_ReturnsBookings() {
        List<Booking> bookings = bookingRepository
                .findAllByBookerIdAndStartAfterOrderByStartDesc(booker.getId(), LocalDateTime.now());
        assertEquals(1, bookings.size());
        assertEquals(booker.getId(), bookings.get(0).getBooker().getId());
    }

    @Test
    void findBookingsByBookerIdAndStatusOrderedByStartDesc_ReturnsBookings() {
        List<Booking> bookings = bookingRepository
                .findAllByBookerIdAndStatusOrderByStartDesc(booker.getId(), BookingStatus.APPROVED);
        assertEquals(3, bookings.size());
        assertEquals(booker.getId(), bookings.get(0).getBooker().getId());
    }

    @Test
    void findBookingsByItemOwnerIdOrderedByStartDesc_ReturnsBookings() {
        List<Booking> bookings = bookingRepository
                .findAllByItemOwnerIdOrderByStartDesc(owner.getId());
        assertEquals(3, bookings.size());
        assertEquals(owner.getId(), bookings.get(0).getItem().getOwner().getId());
    }

    @Test
    void findBookingsByItemOwnerIdOrderedByStartDesc_ReturnsEmptyListWhenNoBookings() {
        List<Booking> bookings = bookingRepository
                .findAllByItemOwnerIdOrderByStartDesc(999L);
        assertEquals(0, bookings.size());
    }

    @Test
    void findBookingsByItemIdAndBookerIdAndStatusAndEndBefore_ReturnsBookings() {
        List<Booking> bookings = bookingRepository
                .findByItemIdAndBookerIdAndStatusAndEndBefore(
                        item.getId(), booker.getId(), BookingStatus.APPROVED, LocalDateTime.now().plusDays(5)
                );
        assertEquals(3, bookings.size());
    }

    @Test
    void findBookingsByItemIdAndItemOwnerIdAndStatusNotIn_ReturnsBookings() {
        List<Booking> bookings = bookingRepository
                .findAllByItemIdAndItemOwnerIdAndStatusNotIn(
                        item.getId(), owner.getId(), List.of(BookingStatus.APPROVED, BookingStatus.REJECTED)
                );
        assertEquals(0, bookings.size());
    }

    @Test
    void findBookingsByItemInAndStatusNotIn_ReturnsBookings() {
        List<Booking> bookings = bookingRepository
                .findAllByItemInAndStatusNotIn(
                        List.of(item), List.of(BookingStatus.REJECTED)
                );
        assertEquals(3, bookings.size());
    }
}
