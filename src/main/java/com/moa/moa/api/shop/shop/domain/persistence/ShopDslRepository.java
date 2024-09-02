package com.moa.moa.api.shop.shop.domain.persistence;

import com.moa.moa.api.shop.shop.domain.entity.Shop;

import java.util.List;

public interface ShopDslRepository {
    List<Shop> findAllShopWithinRange(Double leftTopX, Double leftTopY, Double rightBottomX, Double rightBottomY);
}
