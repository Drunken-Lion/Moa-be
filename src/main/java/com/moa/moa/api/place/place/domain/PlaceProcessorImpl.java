package com.moa.moa.api.place.place.domain;

import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.place.place.domain.persistence.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PlaceProcessorImpl implements PlaceProcessor{
    private final PlaceRepository placeRepository;

    @Override
    public List<Place> findAllPlaceInMap(Double leftTopX, Double leftTopY, Double rightBottomX, Double rightBottomY) {
        return placeRepository.findAllPlaceInMap(leftTopX, leftTopY, rightBottomX, rightBottomY);
    }
}
