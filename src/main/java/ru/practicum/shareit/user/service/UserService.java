package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

@Service
public interface UserService {
    UserDto create(UserDto userDto);

    UserDto update(Long id, UserUpdateDto userDto);

    void delete(Long id);

    List<UserDto> getAll();

    UserDto getById(Long id);
}