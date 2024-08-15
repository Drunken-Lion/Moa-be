package com.moa.moa.api.place.placeamenity.domain.persistence;

import com.moa.moa.api.place.placeamenity.domain.entity.PlaceAmenity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceAmenityRepository extends JpaRepository<PlaceAmenity, Long> {
}
