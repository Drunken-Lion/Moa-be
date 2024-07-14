package com.moa.moa.api.time.operatingtime.domain.entity;

import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.shop.shop.domain.entity.Shop;
import com.moa.moa.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import java.time.LocalTime;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "operating_time")
public class OperatingTime extends BaseEntity {
    @Comment("장소")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Place place;

    @Comment("렌탈샵")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Shop shop;

    @Comment("영업 상태")
    @Column(name = "status", columnDefinition = "VARCHAR(20)")
    private String status;

    @Comment("요일")
    @Column(name = "day", columnDefinition = "VARCHAR(20)")
    private String day;

    @Comment("시작 시간")
    @Column(name = "open", columnDefinition = "TIME")
    private LocalTime openTime;

    @Comment("종료 시간")
    @Column(name = "close", columnDefinition = "TIME")
    private LocalTime closeTime;
}
