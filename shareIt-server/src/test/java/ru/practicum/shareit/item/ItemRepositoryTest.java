package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("Oleg Gazmanov")
                .email("vpole.skonem@viydu.ru")
                .build();
        user = userRepository.save(user);

        item = Item.builder()
                .name("Item 1")
                .description("Item 1")
                .available(true)
                .owner(user)
                .build();
        item = itemRepository.save(item);
    }

    @AfterEach
    void tearDown() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findAllByOwnerIdshouldReturnItems() {
        List<Item> items = itemRepository.getItemsByUser(user.getId());
        assertThat(items).hasSize(1);
        assertThat(items).containsExactly(item);
    }

    @Test
    void searchTextshouldReturnItems() {
        List<Item> items = itemRepository.searchText("Item 1");
        assertThat(items).hasSize(1);
        assertThat(items).containsExactly(item);
    }

    @Test
    void searchTextnoMatchingItems() {
        List<Item> items = itemRepository.searchText("Nonexistent");
        assertThat(items).isEmpty();
    }

    @Test
    void findAllByOwnerIdnoItems() {
        User anotherUser = User.builder()
                .name("Another User")
                .email("another.user@example.com")
                .build();
        userRepository.save(anotherUser);

        List<Item> items = itemRepository.getItemsByUser(anotherUser.getId());
        assertThat(items).isEmpty();
    }
}
