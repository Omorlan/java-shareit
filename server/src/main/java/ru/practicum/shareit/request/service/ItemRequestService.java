package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestAddDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(Long userId, ItemRequestAddDto itemRequestAddDto);

    List<ItemRequestResponseDto> getItemRequests(Long userId);

    List<ItemRequestDto> getAllItemRequests(Long userId, int from, int size);

    ItemRequestResponseDto getItemRequest(Long userId, Long requestId);
}
