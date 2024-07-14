package com.moa.moa.api.place.place.domain.entity;

import com.moa.moa.api.address.address.domain.entity.Address;
import com.moa.moa.api.category.category.domain.entity.Category;
import com.moa.moa.api.time.businesstime.domain.entity.BusinessTime;
import com.moa.moa.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "place")
public class Place extends BaseEntity {
    @Comment("카테고리")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Category category;

    @Comment("주소")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Address address;

    @Comment("비지니스 타임")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_time_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private BusinessTime businessTime;

    @Comment("스키장 이름")
    @Column(name = "name", columnDefinition = "VARCHAR(50)")
    private String name;

    @Comment("개장일")
    @JoinColumn(name = "open", columnDefinition = "DATE")
    private LocalDate openDate;

    @Comment("폐장일")
    @JoinColumn(name = "close", columnDefinition = "DATE")
    private LocalDate closeDate;

    @Comment("추천 스키어")
    @Column(name = "rec_level", columnDefinition = "VARCHAR(20)")
    private String recLevel;
}
