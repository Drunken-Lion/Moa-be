package com.moa.moa.api.place.place.application;

import com.moa.moa.api.place.amenity.domain.entity.Amenity;
import com.moa.moa.api.place.place.application.mapstruct.PlaceMapstructMapper;
import com.moa.moa.api.place.place.domain.PlaceProcessor;
import com.moa.moa.api.place.place.domain.dto.FindAllPlaceDto;
import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.place.placeamenity.domain.entity.PlaceAmenity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceProcessor placeProcessor;
    private final PlaceMapstructMapper placeMapstructMapper;

    public List<FindAllPlaceDto.Response> findAllPlace(Double leftTopX,
                                                       Double leftTopY,
                                                       Double rightBottomX,
                                                       Double rightBottomY) {
        List<Place> places = placeProcessor.findAllPlaceInMap(leftTopX, leftTopY, rightBottomX, rightBottomY);
        List<FindAllPlaceDto.Response> findAllPlaceList = new ArrayList<>();

        for (Place place : places) {
            List<Amenity> amenities = place.getAmenities().stream()
                    .map(PlaceAmenity::getAmenity)
                    .collect(Collectors.toList());

            findAllPlaceList.add(placeMapstructMapper.of(
                    place,
                    null,
                    place.getAddress(),
                    (place.getBusinessTime() != null) ? place.getBusinessTime().getOperatingTimes() : new ArrayList<>(),
                    (place.getBusinessTime() != null) ? place.getBusinessTime().getSpecificDays() : new ArrayList<>(),
                    amenities,
                    place.getSlopes()
            ));
        }

        return findAllPlaceList;
    }
}
