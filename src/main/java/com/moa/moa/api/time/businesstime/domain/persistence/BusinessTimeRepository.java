package com.moa.moa.api.time.businesstime.domain.persistence;

import com.moa.moa.api.time.businesstime.domain.entity.BusinessTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessTimeRepository extends JpaRepository<BusinessTime, Long> {
}
