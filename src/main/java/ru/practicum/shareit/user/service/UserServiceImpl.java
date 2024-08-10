package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        validateEmail(userDto);
        User user = UserMapper.toEntity(userDto);
        return UserMapper.toDto(userRepository.create(user));
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        validateEmail(userDto);
        User userToUpdate = UserMapper.toEntity(userDto);
        userToUpdate.setId(id);
        User updatedUser = userRepository.update(userToUpdate);
        return UserMapper.toDto(updatedUser);
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(Long id) {
        User user = userRepository.getUserById(id);
        return UserMapper.toDto(user);
    }

    private void validateEmail(UserDto userDto) {
        if (userDto.getEmail() != null) {
            userRepository.findAll().stream()
                    .filter(user -> user.getEmail().equals(userDto.getEmail()))
                    .findAny()
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(userDto.getId())) {
                            throw new DuplicateEmailException(
                                    String.format("User with email: %s is already registered.", userDto.getEmail()));
                        }
                    });
        }
    }
}
