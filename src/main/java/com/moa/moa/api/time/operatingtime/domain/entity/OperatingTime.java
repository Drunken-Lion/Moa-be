package com.moa.moa.api.time.operatingtime.domain.entity;

import com.moa.moa.api.time.businesstime.domain.entity.BusinessTime;
import com.moa.moa.api.time.operatingtime.util.convert.DayTypeConverter;
import com.moa.moa.api.time.operatingtime.util.convert.OperatingTypeConverter;
import com.moa.moa.api.time.operatingtime.util.enumerated.DayType;
import com.moa.moa.api.time.operatingtime.util.enumerated.OperatingType;
import com.moa.moa.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "operating_time")
public class OperatingTime extends BaseEntity {
    @Comment("비지니스 타임")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_time_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private BusinessTime businessTime;

    @Comment("영업 상태")
    @Column(name = "status", columnDefinition = "BIGINT")
    @Convert(converter = OperatingTypeConverter.class)
    private OperatingType status;

    @Comment("요일")
    @Column(name = "day", columnDefinition = "BIGINT")
    @Convert(converter = DayTypeConverter.class)
    private DayType day;

    @Comment("시작 시간")
    @Column(name = "open", columnDefinition = "TIME")
    private LocalTime openTime;

    @Comment("종료 시간")
    @Column(name = "close", columnDefinition = "TIME")
    private LocalTime closeTime;
}
