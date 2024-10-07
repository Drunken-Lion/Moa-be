package com.moa.moa.api.place.place.domain;

import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.place.place.domain.persistence.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlaceProcessor {
    private final PlaceRepository placeRepository;

    public List<Place> findAllPlaceInMap(Double leftTopX, Double leftTopY, Double rightBottomX, Double rightBottomY) {
        return placeRepository.findAllPlaceInMap(leftTopX, leftTopY, rightBottomX, rightBottomY);
    }

    public Optional<Place> findPlaceById(Long id) {
        return placeRepository.findPlaceById(id);
    }

    public Optional<Place> findPlaceByIdAndDeletedAtIsNull(Long id) {
        return placeRepository.findPlaceByIdAndDeletedAtIsNull(id);
    }
}
