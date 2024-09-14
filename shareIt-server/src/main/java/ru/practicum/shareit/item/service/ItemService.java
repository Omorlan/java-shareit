package ru.practicum.shareit.item.service;


import ru.practicum.shareit.item.comment.dto.CommentAddDto;
import ru.practicum.shareit.item.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemAddDto;
import ru.practicum.shareit.item.dto.ItemCommentNextLastDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {


    ItemDto create(Long userId, ItemAddDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemUpdateDto itemDto);

    ItemCommentNextLastDto  findItemById(Long itemId, Long userId);

    List<ItemCommentNextLastDto> getItemsByUser(Long userId);

    List<ItemDto> findItemsByText(String text);

    CommentResponseDto addCommentToItem(Long userId, Long itemId, CommentAddDto commentAddDto);
}
