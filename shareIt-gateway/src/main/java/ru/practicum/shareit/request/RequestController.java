package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.common.Headers;
import ru.practicum.shareit.request.dto.ItemRequestAddDto;


@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader(Headers.USER_HEADER) Long userId,
                                                 @Valid @RequestBody ItemRequestAddDto itemRequestAddDto) {
        log.info("Add item request: {}, userId: {}", itemRequestAddDto, userId);
        return requestClient.addItemRequest(userId, itemRequestAddDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequests(@RequestHeader(Headers.USER_HEADER) Long userId) {
        log.info("Get item requests userId: {}", userId);
        return requestClient.getItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(Headers.USER_HEADER) Long userId,
                                                     @RequestParam(defaultValue = "0", required = false) int from,
                                                     @RequestParam(defaultValue = "20", required = false) int size) {
        log.info("Get item requests all users from: {}, size: {}", from, size);
        return requestClient.getAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader(Headers.USER_HEADER) Long userId,
                                                 @PathVariable Long requestId) {
        log.info("Get item request: {}}", requestId);
        return requestClient.getItemRequest(userId, requestId);
    }
}
