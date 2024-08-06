package com.moa.moa.api.place.place.domain;

import com.moa.moa.api.place.place.domain.entity.Place;

import java.util.List;

public interface PlaceProcessor {
    List<Place> findAllPlaceInMap(Double leftTopX, Double leftTopY, Double rightBottomX, Double rightBottomY);
}
