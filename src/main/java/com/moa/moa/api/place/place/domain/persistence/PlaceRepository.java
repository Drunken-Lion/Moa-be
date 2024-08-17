package com.moa.moa.api.place.place.domain.persistence;

import com.moa.moa.api.place.place.domain.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
