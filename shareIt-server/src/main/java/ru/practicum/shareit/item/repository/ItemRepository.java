package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("""
            select i from Item as i join fetch i.owner as o where o.id = :userId
            """)
    List<Item> getItemsByUser(@Param("userId") Long userId);

    @Query("""
             select i from Item as i where (upper(i.name) like upper(concat('%', :text, '%')) or
             upper(i.description) like upper(concat('%', :text, '%'))) and i.available = true
            """)
    List<Item> searchText(@Param("text") String text);
}
