package com.innowise.orderservice.repository;

import com.innowise.orderservice.entity.Item;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

  @Query("from items as i where upper(i.name) like upper(:name) and i.deleted = false")
  Optional<Item> findByName(@Param("name") String name);

  @Query("from items as i where i.id in :ids and i.deleted = false")
  List<Item> findAllActiveByIds(List<Long> ids);

  @Query("from items as i where i.id = :id and i.deleted = false")
  Optional<Item> findActiveById(@Param("id") Long id);
}
