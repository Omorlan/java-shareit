package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {


    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    ItemDto findItemById(Long itemId);

    List<ItemDto> getItemsByUser(Long userId);

    void delete(Long itemId);

    List<ItemDto> findItemsByText(String text);
}
