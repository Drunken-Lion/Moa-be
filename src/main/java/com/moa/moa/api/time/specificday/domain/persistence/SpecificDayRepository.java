package com.moa.moa.api.time.specificday.domain.persistence;

import com.moa.moa.api.time.specificday.domain.entity.SpecificDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecificDayRepository extends JpaRepository<SpecificDay, Long> {
}
