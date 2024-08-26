package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserRepository {
    User getUserById(Long id);

    List<User> findAll();

    User create(User user);

    User update(User user);

    void delete(Long id);

    boolean isUserExist(Long userId);
}
