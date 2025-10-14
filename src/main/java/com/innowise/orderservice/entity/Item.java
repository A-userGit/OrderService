package com.innowise.orderservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "items")
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "items_seq")
  @SequenceGenerator(name = "items_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private long id;

  @Column(name = "name")
  private String name;

  @Column(name = "price")
  private long price;

  @Column(name = "deleted")
  private boolean deleted;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Item item = (Item) o;
    return id == item.id && price == item.price && deleted == item.deleted
        && Objects.equals(name, item.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, price, deleted);
  }
}
