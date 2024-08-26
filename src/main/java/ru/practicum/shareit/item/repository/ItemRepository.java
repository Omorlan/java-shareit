package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    Item create(Item item);

    Item update(Item item);

    Item findItemById(Long itemId);

    List<Item> getItemsByUser(Long userId);

    void delete(Long itemId);

    boolean isOwner(Long userId, Long itemId);

    boolean isItemExist(Long itemId);

    List<Item> findItemsByText(String text);
}
