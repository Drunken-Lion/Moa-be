package com.moa.moa.api.time.businesstime.domain.persistence;

import com.moa.moa.api.time.operatingtime.util.enumerated.DayType;

import java.time.LocalDate;

public interface BusinessTimeDslRepository {
    Boolean isShopInOperation(Long businessTimeId, LocalDate visitDate, DayType day);
}
