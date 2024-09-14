package ru.practicum.shareit.item;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoTest {
    @Autowired
    private JacksonTester<ItemDto> json;

    private final User user = User.builder()
            .id(1L)
            .name("Oleg Gazmanov")
            .email("vpole.skonem@viydu.ru")
            .build();

    @Test
    @SneakyThrows
    void serializeItemDto_correctlySerialized() {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Item")
                .description("Description")
                .available(true)
                .owner(user)
                .request(null)
                .build();

        JsonContent<ItemDto> content = json.write(itemDto);

        assertThat(content)
                .hasJsonPath("$.id")
                .hasJsonPath("$.name")
                .hasJsonPath("$.description")
                .hasJsonPath("$.available")
                .hasJsonPath("$.owner")
                .hasJsonPath("$.request");

        assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(content).extractingJsonPathStringValue("$.name").isEqualTo("Item");
        assertThat(content).extractingJsonPathStringValue("$.description").isEqualTo("Description");
        assertThat(content).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(content).extractingJsonPathNumberValue("$.owner.id").isEqualTo(1);
        assertThat(content).extractingJsonPathStringValue("$.request").isNull();
    }

    @Test
    @SneakyThrows
    void deserializeItemDto_correctlyDeserialized() {
        String jsonString = "{\"id\":1,\"name\":\"Item\",\"description\":\"Description\",\"available\":true,\"owner\":{\"id\":1,\"name\":\"Oleg Gazmanov\",\"email\":\"vpole.skonem@viydu.ru\"},\"request\":null}";

        ItemDto itemDto = json.parseObject(jsonString);

        assertThat(itemDto).isNotNull();
        assertThat(itemDto.getId()).isEqualTo(1L);
        assertThat(itemDto.getName()).isEqualTo("Item");
        assertThat(itemDto.getDescription()).isEqualTo("Description");
        assertThat(itemDto.getAvailable()).isTrue();
        assertThat(itemDto.getOwner().getId()).isEqualTo(1L);
        assertThat(itemDto.getRequest()).isNull();
    }
}
