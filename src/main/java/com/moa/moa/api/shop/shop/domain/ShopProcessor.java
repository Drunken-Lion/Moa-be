package com.moa.moa.api.shop.shop.domain;

import com.moa.moa.api.shop.shop.domain.entity.Shop;
import com.moa.moa.api.shop.shop.domain.persistence.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShopProcessor {
    private final ShopRepository shopRepository;

    public List<Shop> findAllShopWithinRange(Double leftTopX, Double leftTopY, Double rightBottomX, Double rightBottomY) {
        return shopRepository.findAllShopWithinRange(leftTopX, leftTopY, rightBottomX, rightBottomY);
    }
}
