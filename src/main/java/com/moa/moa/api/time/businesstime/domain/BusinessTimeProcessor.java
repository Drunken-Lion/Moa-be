package com.moa.moa.api.time.businesstime.domain;

import com.moa.moa.api.time.businesstime.domain.persistence.BusinessTimeRepository;
import com.moa.moa.api.time.operatingtime.util.enumerated.DayType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class BusinessTimeProcessor {
    private final BusinessTimeRepository businessTimeRepository;

    public Boolean isShopInOperation(Long businessTimeId, LocalDate visitDate, DayType day) {
        return businessTimeRepository.isShopInOperation(businessTimeId, visitDate, day);
    }
}
