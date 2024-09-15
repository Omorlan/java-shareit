package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.common.Headers;
import ru.practicum.shareit.request.dto.ItemRequestAddDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader(Headers.USER_HEADER) Long userId,
                                         @RequestBody ItemRequestAddDto itemRequestAddDto) {
        return itemRequestService.addItemRequest(userId, itemRequestAddDto);
    }

    @GetMapping
    public List<ItemRequestResponseDto> getItemRequests(@RequestHeader(Headers.USER_HEADER) Long userId) {
        return itemRequestService.getItemRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader(Headers.USER_HEADER) Long userId,
                                                   @RequestParam(defaultValue = "0", required = false) int from,
                                                   @RequestParam(defaultValue = "20", required = false) int size) {
        return itemRequestService.getAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getItemRequest(@RequestHeader(Headers.USER_HEADER) Long userId,
                                                 @PathVariable Long requestId) {
        return itemRequestService.getItemRequest(userId, requestId);
    }
}