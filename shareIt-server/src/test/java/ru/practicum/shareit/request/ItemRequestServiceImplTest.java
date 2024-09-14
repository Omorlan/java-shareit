package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.dto.ItemRequestAddDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    ItemRequestRepository itemRequestRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ItemRequestServiceImpl itemRequestService;

    private final User user = User.builder()
            .id(1L)
            .name("Oleg Gazmanov")
            .email("vpole.skonem@viydu.ru")
            .build();

    private ItemRequest request;

    @BeforeEach
    void setUp() {
        request = new ItemRequest();
        request.setId(1L);
        request.setRequestor(user);
        request.setDescription("Description");
        request.setCreated(LocalDateTime.now());
    }

    @Test
    void addItemRequestSuccessful() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(request);
        ItemRequestAddDto addDto = new ItemRequestAddDto();
        addDto.setDescription("Description");
        ItemRequestDto dto = itemRequestService.addItemRequest(user.getId(), addDto);
        assertEquals(request.getId(), dto.getId());
    }

    @Test
    void getItemRequestsUserHasRequests() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findByRequestorIdOrderByCreatedDesc(anyLong())).thenReturn(List.of(request));
        List<ItemRequestResponseDto> dtos = itemRequestService.getItemRequests(user.getId());
        assertEquals(1, dtos.size());
        assertEquals(request.getId(), dtos.get(0).getId());
    }

    @Test
    void getAllItemRequestsSuccessful() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "created"));
        Page<ItemRequest> page = new PageImpl<>(List.of(request), pageRequest, 1);
        when(itemRequestRepository.findAll(pageRequest)).thenReturn(page);
        List<ItemRequestDto> dtos = itemRequestService.getAllItemRequests(user.getId(), 0, 20);
        assertEquals(1, dtos.size());
        assertEquals(request.getId(), dtos.get(0).getId());
    }

    @Test
    void getItemRequestRequestExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(request.getId())).thenReturn(Optional.of(request));
        ItemRequestResponseDto dto = itemRequestService.getItemRequest(user.getId(), request.getId());
        assertEquals(request.getId(), dto.getId());
    }
}
