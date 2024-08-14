package com.moa.moa.api.place.slope.domain.persistence;

import com.moa.moa.api.place.slope.domain.entity.Slope;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlopeRepository extends JpaRepository<Slope, Long> {
}
