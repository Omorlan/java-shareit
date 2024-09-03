package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.Patcher;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        userRepository.findByEmail(userDto.getEmail()).ifPresent(user -> {
            throw new ValidationException("User with email " + user.getEmail() + " already exists");
        });
        return UserMapper.toDto(userRepository.save(UserMapper.toEntity(userDto)));
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserUpdateDto userDto) {
        User user = UserMapper.updateUser(userDto);
        User updatedUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException(
                "User with id " + user.getId() + " not found"));
        userRepository.findByEmail(user.getEmail()).ifPresent(
                u -> {
                    if (!updatedUser.getEmail().equals(u.getEmail())) {
                        throw new ValidationException("User with email " + u.getEmail() + " already exists");
                    }
                });
        try {
            Patcher.patch(updatedUser, user);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return UserMapper.toDto(userRepository.save(updatedUser));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.findById(id).orElseThrow(() -> new NotFoundException(
                "User with id " + id + " not found"));
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(
                "User with id " + id + " not found"));
        return UserMapper.toDto(user);
    }
}
