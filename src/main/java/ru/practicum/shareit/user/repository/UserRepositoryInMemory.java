package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class UserRepositoryInMemory implements UserRepository {
    private final Map<Long, User> userMap = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
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
        validateEmail(user.getEmail());
        emails.add(user.getEmail());
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
        if (!updateUser.getEmail().equals(user.getEmail())) {
            validateEmail(user.getEmail());
            emails.add(user.getEmail());
        }
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

    private void validateEmail(String email) {
        if (emails.contains(email)) {
            throw new DuplicateEmailException(
                    String.format("User with email: %s is already registered.", email));
        }
    }
}
