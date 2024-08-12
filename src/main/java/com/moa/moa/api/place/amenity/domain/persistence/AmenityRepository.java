package com.moa.moa.api.place.amenity.domain.persistence;

import com.moa.moa.api.place.amenity.domain.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
}
