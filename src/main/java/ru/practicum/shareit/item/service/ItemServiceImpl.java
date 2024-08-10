package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        validateUser(userId);
        Item item = Item.builder()
                .name(itemDto.getName())
                .owner(userId)
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .request(null)
                .build();
        return ItemMapper.toDto(itemRepository.create(item));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        validateUser(userId);
        validateOwnership(userId, itemId);
        Item itemToUpdate = ItemMapper.toEntity(itemDto);
        itemToUpdate.setId(itemId);
        return ItemMapper.toDto(itemRepository.update(itemToUpdate));
    }

    @Override
    public ItemDto findItemById(Long itemId) {
        return ItemMapper.toDto(itemRepository.findItemById(itemId));
    }

    @Override
    public List<ItemDto> getItemsByUser(Long userId) {
        validateUser(userId);
        return ItemMapper.toDtoList(itemRepository.getItemsByUser(userId).stream().toList());
    }

    @Override
    public void delete(Long itemId) {
        if (!itemRepository.isItemExist(itemId)) {
            throw new NotFoundException("Item (id = " + itemId + ") not found!");
        }
        itemRepository.delete(itemId);
    }

    @Override
    public List<ItemDto> findItemsByText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return List.of();
        }
        return ItemMapper.toDtoList(itemRepository.findItemsByText(text).stream().toList());
    }

    private void validateUser(Long userId) {
        if (userId == null || !userRepository.isUserExist(userId)) {
            throw new NotFoundException("User (id = " + userId + ") not found!");
        }
    }

    private void validateOwnership(Long userId, Long itemId) {
        if (itemId == null || !itemRepository.isOwner(userId, itemId)) {
            throw new NotFoundException("Only the owner can edit this item!");
        }
    }
}
