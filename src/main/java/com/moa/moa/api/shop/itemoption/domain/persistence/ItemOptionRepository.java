package com.moa.moa.api.shop.itemoption.domain.persistence;

import com.moa.moa.api.shop.itemoption.domain.entity.ItemOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemOptionRepository extends JpaRepository<ItemOption, Long> {
}
