package com.innowise.orderservice.repository;

import com.innowise.orderservice.entity.OrderedItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedItemRepository extends JpaRepository<OrderedItem, Long> {

  @Query("from order_items as oi where oi.order.id = :orderId and oi.id in :ids")
  List<OrderedItem> findAllByIdInOrder(long orderId, Iterable<Long> ids);
}
