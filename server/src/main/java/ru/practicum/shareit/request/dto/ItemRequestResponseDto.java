package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ItemRequestResponseDto {
    Long id;
    String description;
    LocalDateTime created;
    List<RequestItem> items;

    public record RequestItem(Long id, String name, Long ownerId) {
    }
}
