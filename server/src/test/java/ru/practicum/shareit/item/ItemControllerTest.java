package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.common.Headers;
import ru.practicum.shareit.item.comment.dto.CommentAddDto;
import ru.practicum.shareit.item.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemAddDto;
import ru.practicum.shareit.item.dto.ItemCommentNextLastDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private User owner;
    private ItemDto itemDto;
    private ItemCommentNextLastDto itemCommentNextLastDto;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1L)
                .name("Oleg Gazmanov")
                .email("vpole.skonem@viydu.ru")
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("Item1")
                .description("Item1")
                .available(true)
                .owner(owner)
                .build();

        itemCommentNextLastDto = ItemCommentNextLastDto.builder()
                .id(2L)
                .name("Item1")
                .description("Item1")
                .available(true)
                .owner(owner)
                .comments(null)
                .build();
    }

    @Test
    @SneakyThrows
    void createItemwhatShouldBeReturned() {
        ItemAddDto itemAddDto = ItemAddDto.builder()
                .id(1L)
                .name("Item1")
                .description("Item1")
                .available(true)
                .owner(owner)
                .build();
        when(itemService.create(1L, itemAddDto)).thenReturn(itemDto);

        String result = mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Headers.USER_HEADER, "1")
                        .content(objectMapper.writeValueAsString(itemAddDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @Test
    @SneakyThrows
    void updateItemwhatShouldBeReturned() {
        ItemUpdateDto itemUpdateDto = ItemUpdateDto.builder()
                .name("Item1")
                .description("Item1")
                .build();
        when(itemService.update(1L, 1L, itemUpdateDto)).thenReturn(itemDto);

        String result = mockMvc.perform(patch("/items/{itemId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemUpdateDto))
                        .header(Headers.USER_HEADER, "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @Test
    @SneakyThrows
    void getAllItemswhatShouldBeReturned() {
        when(itemService.getItemsByUser(1L)).thenReturn(List.of(itemCommentNextLastDto));

        String result = mockMvc.perform(get("/items")
                        .header(Headers.USER_HEADER, "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(itemCommentNextLastDto)), result);
    }

    @Test
    @SneakyThrows
    void getItemByIdwhatShouldBeReturned() {
        when(itemService.findItemById(1L, 1L)).thenReturn(itemCommentNextLastDto);

        String result = mockMvc.perform(get("/items/{itemId}", 1L)
                        .header(Headers.USER_HEADER, "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemCommentNextLastDto), result);
    }

    @Test
    @SneakyThrows
    void searchItemsByTextwhatShouldBeReturned() {
        when(itemService.findItemsByText(anyString())).thenReturn(List.of(itemDto));

        String result = mockMvc.perform(get("/items/search")
                        .param("text", "text"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(itemDto)), result);
    }

    @Test
    @SneakyThrows
    void addCommentToItemwhatShouldBeReturned() {
        CommentAddDto commentAddDto = new CommentAddDto();
        commentAddDto.setText("comment");
        CommentResponseDto responseDto = CommentResponseDto.builder()
                .authorName("John Doe")
                .text("comment")
                .created(LocalDateTime.now())
                .build();
        when(itemService.addCommentToItem(1L, 1L, commentAddDto)).thenReturn(responseDto);

        String result = mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Headers.USER_HEADER, "1")
                        .content(objectMapper.writeValueAsString(commentAddDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(responseDto), result);
    }

    @Test
    @SneakyThrows
    void createItemwhenErrorOccurs_whatShouldBeReturned() {
        ItemAddDto itemAddDto = ItemAddDto.builder()
                .id(1L)
                .name("Item1")
                .description("Item1")
                .available(true)
                .owner(owner)
                .build();
        when(itemService.create(1L, itemAddDto)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Headers.USER_HEADER, "1")
                        .content(objectMapper.writeValueAsString(itemAddDto)))
                .andExpect(status().isInternalServerError());
    }
}
