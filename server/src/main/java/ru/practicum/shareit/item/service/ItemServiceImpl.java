package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.Patcher;
import ru.practicum.shareit.common.exception.NotAvailableException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentAddDto;
import ru.practicum.shareit.item.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repostitory.CommentRepository;
import ru.practicum.shareit.item.dto.ItemAddDto;
import ru.practicum.shareit.item.dto.ItemCommentNextLastDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository requestRepository;

    @Override
    @Transactional
    public ItemDto create(Long userId, ItemAddDto itemDto) {
        User user = validateUser(userId);
        Item item = ItemMapper.toEntity(itemDto);
        item.setOwner(user);
        Long requestId = itemDto.getRequestId();
        if (requestId != null) {
            item.setRequest(requestRepository.findById(requestId)
                    .orElseThrow(() -> new NotFoundException("Request " + requestId + " not found")));
        }
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto update(Long userId, Long itemId, ItemUpdateDto patchDto) {
        validateUser(userId);
        Item updatedItem = validateItem(itemId);

        if (!updatedItem.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Item " + itemId + " does not belong to user " + userId);
        }

        Item patchItem = ItemMapper.fromPatchItemDto(patchDto);
        try {
            Patcher.patch(updatedItem, patchItem);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error applying patch", e);
        }

        return ItemMapper.toDto(itemRepository.save(updatedItem));
    }

    @Override
    public ItemCommentNextLastDto findItemById(Long itemId, Long userId) {
        validateUser(userId);
        Item item = validateItem(itemId);

        List<Booking> bookings = bookingRepository.findAllByItemIdAndItemOwnerIdAndStatusNotIn(
                itemId, userId, List.of(BookingStatus.REJECTED));

        return ItemMapper.toItemCommentNextLastDto(item, bookings);
    }

    @Override
    public List<ItemCommentNextLastDto> getItemsByUser(Long userId) {
        List<Item> items = itemRepository.getItemsByUser(userId);
        List<Booking> bookings = bookingRepository.findAllByItemInAndStatusNotIn(
                items, List.of(BookingStatus.REJECTED));
        return ItemMapper.toItemCommentLastNextDtos(items, bookings);
    }

    @Override
    public List<ItemDto> findItemsByText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return ItemMapper.toDtoList(itemRepository.searchText(text.toLowerCase()));
    }

    @Override
    @Transactional
    public CommentResponseDto addCommentToItem(Long userId, Long itemId, CommentAddDto commentAddDto) {
        Item item = validateItem(itemId);

        User user = validateUser(userId);

        List<Booking> bookings = bookingRepository.findByItemIdAndBookerIdAndStatusAndEndBefore(
                itemId, userId, BookingStatus.APPROVED, LocalDateTime.now());

        if (CollectionUtils.isEmpty(bookings)) {
            throw new NotAvailableException("User id: " + userId + " has not booked item id: " + itemId);
        }

        Comment comment = CommentMapper.toEntity(commentAddDto);
        comment.setItem(item);
        comment.setAuthor(user);

        return CommentMapper.toDto(commentRepository.save(comment));
    }

    private User validateUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
    }

    private Item validateItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item " + itemId + " not found"));
    }
}