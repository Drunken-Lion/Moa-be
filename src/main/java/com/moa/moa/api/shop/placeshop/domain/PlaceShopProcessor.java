package com.moa.moa.api.shop.placeshop.domain;

import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.shop.placeshop.domain.entity.PlaceShop;
import com.moa.moa.api.shop.placeshop.domain.persistence.PlaceShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PlaceShopProcessor {
    private final PlaceShopRepository placeShopRepository;

    public List<PlaceShop> findAllShopRelatedToPlace(Place place) {
        return placeShopRepository.findAllPlaceShopByPlaceAndDeletedAtIsNull(place);
    }
}
