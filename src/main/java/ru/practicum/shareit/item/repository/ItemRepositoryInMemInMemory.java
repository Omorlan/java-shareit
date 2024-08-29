package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryInMemInMemory implements ItemRepositoryInMem {
    private final Map<Long, Item> itemMap = new HashMap<>();
    private Long id = 0L;

    @Override
    public Item create(Item item) {
        item.setId(++id);
        itemMap.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item newItem) {
        Item item = itemMap.get(newItem.getId());
        if (newItem.getName() != null
                && !newItem.getName().isBlank()) {
            item.setName(newItem.getName());
        }
        if (newItem.getDescription() != null
                && !newItem.getDescription().isBlank()) {
            item.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) {
            item.setAvailable(newItem.getAvailable());
        }
        return item;
    }

    @Override
    public Item findItemById(Long itemId) {
        return itemMap.get(itemId);
    }

    @Override
    public List<Item> getItemsByUser(Long userId) {
        return itemMap.values().stream()
                .filter(item -> item.getOwner().equals(userId))
                .toList();
    }

    @Override
    public boolean isItemExist(Long itemId) {
        return itemMap.containsKey(itemId);
    }

    @Override
    public void delete(Long itemId) {
        itemMap.remove(itemId);
    }

    @Override
    public boolean isOwner(Long userId, Long itemId) {
        return itemMap.get(itemId).getOwner().equals(userId);
    }

    @Override
    public List<Item> findItemsByText(String text) {
        String lowerCaseText = text.toLowerCase();
        return itemMap.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(lowerCaseText)
                        || item.getDescription().toLowerCase().contains(lowerCaseText))
                        && item.getAvailable())
                .toList();
    }
}
