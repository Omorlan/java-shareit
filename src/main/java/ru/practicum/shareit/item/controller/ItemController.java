package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.common.Headers;
import ru.practicum.shareit.item.comment.dto.CommentAddDto;
import ru.practicum.shareit.item.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCommentNextLastDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private static final String USER_HEADER = Headers.USER_HEADER;

    @GetMapping
    public List<ItemCommentNextLastDto> getItemsByUser(@RequestHeader(USER_HEADER) Long userId) {
        return itemService.getItemsByUser(userId);
    }

    @GetMapping("/{itemId}")
    public ItemCommentNextLastDto getItemDto(@RequestHeader(USER_HEADER) Long userId,
                                             @PathVariable Long itemId) {
        return itemService.findItemById(itemId, userId);
    }

    @PostMapping
    public ItemDto create(@RequestHeader(USER_HEADER) Long userId,
                          @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USER_HEADER) Long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemUpdateDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemDto(
            @RequestParam(defaultValue = "", required = false) String text) {
        return itemService.findItemsByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addCommentToItem(@RequestHeader(USER_HEADER) Long userId,
                                               @PathVariable Long itemId,
                                               @Valid @RequestBody CommentAddDto commentAddDto) {
        return itemService.addCommentToItem(userId, itemId, commentAddDto);
    }
}
