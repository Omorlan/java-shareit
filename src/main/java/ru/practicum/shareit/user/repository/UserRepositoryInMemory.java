package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserRepositoryInMemory implements UserRepository {
    private final Map<Long, User> userMap;
    private Long id = 0L;

    @Override
    public User getUserById(Long id) {
        return userMap.get(id);
    }

    @Override
    public List<User> findAll() {
        return userMap.values().stream().toList();
    }

    @Override
    public User create(User user) {
        user.setId(id++);
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!userMap.containsKey(user.getId())) {
            throw new NotFoundException(String.format("User with id %s not found", user.getId()));
        }
        User updateUser = userMap.get(user.getId());
        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            updateUser.setEmail(user.getEmail());
        }
        userMap.put(user.getId(), updateUser);
        return updateUser;
    }

    @Override
    public void delete(Long id) {
        userMap.remove(id);
    }

    @Override
    public boolean isUserExist(Long userId) {
        return userMap.containsKey(userId);
    }
}
