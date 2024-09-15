package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ItemAddDto {
    Long id;
    String name;
    String description;
    User owner;
    Boolean available;
    Long requestId;
}
