package com.moa.moa.api.shop.shop.domain.persistence;

import com.moa.moa.api.shop.shop.domain.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long>, ShopDslRepository {
    Optional<Shop> findShopByIdAndDeletedAtIsNull(Long shopId);

    Optional<Shop> findShopByNameAndDeletedAtIsNull(String shopName);
}
