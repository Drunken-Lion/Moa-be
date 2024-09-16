package com.moa.moa.api.time.businesstime.domain.persistence;

import com.moa.moa.api.time.businesstime.domain.entity.BusinessTime;
import com.moa.moa.api.time.operatingtime.util.enumerated.DayType;
import com.moa.moa.api.time.operatingtime.util.enumerated.OperatingType;
import com.moa.moa.api.time.specificday.util.enumerated.SpecificDayType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.moa.moa.api.time.businesstime.domain.entity.QBusinessTime.businessTime;
import static com.moa.moa.api.time.operatingtime.domain.entity.QOperatingTime.operatingTime;
import static com.moa.moa.api.time.specificday.domain.entity.QSpecificDay.specificDay;

@RequiredArgsConstructor
public class BusinessTimeDslRepositoryImpl implements BusinessTimeDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Boolean isShopInOperation(Long businessTimeId, LocalDate visitDate, DayType day) {

        List<BusinessTime> shopsInOperation = queryFactory.selectFrom(businessTime)
                .leftJoin(operatingTime).on(businessTime.id.eq(operatingTime.businessTime.id))
                .leftJoin(specificDay).on(businessTime.id.eq(specificDay.businessTime.id)
                        .and(specificDay.date.eq(LocalDate.of(visitDate.getYear(), visitDate.getMonth(), visitDate.getDayOfMonth()))))
                .where(businessTime.id.eq(businessTimeId)
                        .and(
                                operatingTime.status.eq(OperatingType.OPEN).and(
                                                specificDay.status.notIn(SpecificDayType.CLOSED, SpecificDayType.HOLIDAY)
                                                        .or(specificDay.status.isNull())
                                        )
                                        .or(operatingTime.status.eq(OperatingType.CLOSED).and(specificDay.status.in(SpecificDayType.OPEN_WEEK_DAYS, SpecificDayType.WEEKEND_OPEN)))
                        )
                        .and(operatingTime.day.eq(day)))
                .fetch();

        return !shopsInOperation.isEmpty();
    }
}
