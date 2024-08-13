package com.moa.moa.api.time.specificday.domain.entity;

import com.moa.moa.api.time.businesstime.domain.entity.BusinessTime;
import com.moa.moa.api.time.specificday.util.convert.SpecificDayTypeConverter;
import com.moa.moa.api.time.specificday.util.enumerated.SpecificDayType;
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
import java.time.LocalDate;
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
@Table(name = "specific_time")
public class SpecificDay extends BaseEntity {
    @Comment("비지니스 타임")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_time_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private BusinessTime businessTime;

    @Comment("영업 상태")
    @Column(name = "status", columnDefinition = "BIGINT")
    @Convert(converter = SpecificDayTypeConverter.class)
    private SpecificDayType status;

    @Comment("특정일 이유")
    @Column(name = "reason", columnDefinition = "VARCHAR(100)")
    private String reason;

    @Comment("날짜")
    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date;

    @Comment("시작 시간")
    @Column(name = "open", columnDefinition = "TIME")
    private LocalTime openTime;

    @Comment("종료 시간")
    @Column(name = "close", columnDefinition = "TIME")
    private LocalTime closeTime;
}
