package com.moa.moa.api.shop.placeshop.domain.persistence;

import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.shop.placeshop.domain.entity.PlaceShop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceShopRepository extends JpaRepository<PlaceShop, Long> {
    List<PlaceShop> findAllPlaceShopByPlaceAndDeletedAtIsNull(Place place);
}
