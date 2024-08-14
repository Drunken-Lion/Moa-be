package com.moa.moa.api.time.operatingtime.domain.persistence;

import com.moa.moa.api.time.operatingtime.domain.entity.OperatingTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperatingTimeRepository extends JpaRepository<OperatingTime, Long> {
}
