package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setUp() {
        testUser1 = User.builder()
                .id(1L)
                .name("Oleg Gazmanov")
                .email("vpole.skonem@viydu.ru")
                .build();
        testUser2 = User.builder()
                .id(2L)
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .build();
    }

    @Test
    void getAllUsers_UsersAreReturned() {
        when(userRepository.findAll()).thenReturn(List.of(testUser1, testUser2));
        List<UserDto> allUsers = userService.getAll();
        assertEquals(2, allUsers.size());
        assertEquals(testUser1.getId(), allUsers.get(0).getId());
        assertEquals(testUser1.getEmail(), allUsers.get(0).getEmail());
        assertEquals(testUser2.getId(), allUsers.get(1).getId());
        assertEquals(testUser2.getName(), allUsers.get(1).getName());
    }

    @Test
    void createUser_UserIsCreated() {
        when(userRepository.findByEmail(testUser1.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser1);
        UserDto userDto = UserDto.builder()
                .id(testUser1.getId())
                .name("Oleg Gazmanov")
                .email("vpole.skonem@viydu.ru")
                .build();
        UserDto createdUser = userService.create(userDto);
        assertEquals(testUser1.getName(), createdUser.getName());
    }

    @Test
    void updateUser_UserIsUpdated() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser1));
        when(userRepository.save(any(User.class))).thenReturn(testUser1);

        UserUpdateDto updateDto = UserUpdateDto.builder()
                .name("Oleg Gazmanov")
                .build();

        UserDto updatedUserDto = userService.update(testUser1.getId(), updateDto);

        assertEquals(testUser1.getName(), updatedUserDto.getName());
    }

    @Test
    void getUserById_UserIsReturned() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(testUser2));
        UserDto userDto = userService.getById(2L);
        assertEquals(testUser2.getName(), userDto.getName());
    }

    @Test
    void deleteUser_UserIsDeleted() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser1));
        userService.delete(testUser1.getId());
        verify(userRepository).deleteById(testUser1.getId());
    }

    @Test
    void createUser_ShouldThrowValidationException_WhenEmailAlreadyExists() {
        when(userRepository.findByEmail(testUser1.getEmail())).thenReturn(Optional.of(testUser1));
        UserDto userDto = UserDto.builder()
                .name("Oleg Gazmanov")
                .email("vpole.skonem@viydu.ru")
                .build();
        assertThrows(ValidationException.class, () -> userService.create(userDto));
    }

    @Test
    void updateUser_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        UserUpdateDto updateDto = UserUpdateDto.builder()
                .name("Oleg Gazmanov")
                .build();
        assertThrows(NotFoundException.class, () -> userService.update(999L, updateDto));
    }

    @Test
    void updateUser_ShouldThrowValidationException_WhenEmailAlreadyExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser1));
        when(userRepository.findByEmail(testUser2.getEmail())).thenReturn(Optional.of(testUser2));
        UserUpdateDto updateDto = UserUpdateDto.builder()
                .email("jane.doe@example.com")
                .build();
        assertThrows(ValidationException.class, () -> userService.update(testUser1.getId(), updateDto));
    }
}
