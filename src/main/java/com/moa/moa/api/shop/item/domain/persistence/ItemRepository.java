package com.moa.moa.api.shop.item.domain.persistence;

import com.moa.moa.api.shop.item.domain.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
