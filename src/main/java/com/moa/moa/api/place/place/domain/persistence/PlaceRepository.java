package com.moa.moa.api.place.place.domain.persistence;

import com.moa.moa.api.place.place.domain.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long>,  PlaceDslRepository{
    Optional<Place> findPlaceByIdAndDeletedAtIsNull(Long id);

    Optional<Place> findPlaceByNameAndDeletedAtIsNull(String placeName);
}
