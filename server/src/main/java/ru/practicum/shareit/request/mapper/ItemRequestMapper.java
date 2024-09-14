package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestAddDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {
    public static ItemRequest toItemRequest(ItemRequestAddDto addDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(addDto.getDescription());
        return itemRequest;
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(itemRequest.getRequestor())
                .created(itemRequest.getCreated())
                .build();
    }

    public static List<ItemRequestResponseDto> fromItemRequestList(List<ItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestResponseDto)
                .toList();
    }

    public static ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest) {
        List<ItemRequestResponseDto.RequestItem> items = itemRequest.getItems().stream()
                .map(i -> new ItemRequestResponseDto.RequestItem(
                        i.getId(), i.getName(), i.getOwner().getId()))
                .toList();
        return ItemRequestResponseDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(items)
                .build();
    }
}
