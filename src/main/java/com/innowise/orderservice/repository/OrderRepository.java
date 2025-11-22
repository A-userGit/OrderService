package com.innowise.orderservice.repository;

import com.innowise.orderservice.entity.Order;
import com.innowise.orderservice.enums.OrderStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  @Query(value = "from orders as o where o.status in :statuses")
  List<Order> getByStatuses(@Param("statuses") List<OrderStatus> statuses);
}
