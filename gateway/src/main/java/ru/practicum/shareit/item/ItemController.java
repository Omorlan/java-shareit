package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import ru.practicum.shareit.item.comment.CommentAddDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;


@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(Headers.USER_HEADER) Long userId,
                                          @Valid @RequestBody ItemDto itemDto) {
        log.info("Adding item: {}", itemDto);
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(Headers.USER_HEADER) Long userId,
                                             @Valid @RequestBody ItemUpdateDto patchDto,
                                             @PathVariable int itemId) {
        log.info("Updating by id: {} item: {}", itemId, patchDto);
        return itemClient.updateItem(userId, itemId, patchDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems(@RequestHeader(Headers.USER_HEADER) Long userId) {
        log.info("Getting all items by user: {}", userId);
        return itemClient.getAllItems(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(Headers.USER_HEADER) Long userId,
                                              @PathVariable Long itemId) {
        return itemClient.getItemById(itemId, userId);

    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam String text) {
        log.info("Searching by text: {}", text);
        return itemClient.getItemsByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addCommentToItem(@RequestHeader(Headers.USER_HEADER) Long userId,
                                                   @PathVariable Long itemId,
                                                   @Valid @RequestBody CommentAddDto commentAddDto) {
        log.info("Adding comment to item: {}", itemId);
        return itemClient.addCommentToItem(userId, itemId, commentAddDto);
    }
}
