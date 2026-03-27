package com.innowise.orderservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity(name = "order_items")
public class OrderedItem {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ordered_item_seq")
  @SequenceGenerator(name = "ordered_item_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private long id;

  @Column(name = "quantity")
  private int quantity;

  @ManyToOne
  @JoinColumn(name="order_id", nullable=false)
  private Order order;

  @ManyToOne
  @JoinColumn(name="item_id", nullable=false)
  private Item item;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderedItem that = (OrderedItem) o;
    return id == that.id && quantity == that.quantity && Objects.equals(order, that.order)
        && Objects.equals(item, that.item);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, quantity, order, item);
  }
}
