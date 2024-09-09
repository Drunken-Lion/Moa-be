package com.moa.moa.api.shop.shop.domain.persistence;

import com.moa.moa.api.shop.shop.domain.entity.Shop;

import java.util.List;
import java.util.Optional;

public interface ShopDslRepository {
    List<Shop> findAllShopWithinRange(Double leftTopX, Double leftTopY, Double rightBottomX, Double rightBottomY);

    Optional<Shop> findShopById(Long id);
}
