package com.moa.moa.api.place.slope.domain.entity;

import com.moa.moa.api.place.place.domain.entity.Place;
import com.moa.moa.api.place.slope.util.convert.SlopeLevelConverter;
import com.moa.moa.api.place.slope.util.enumerated.SlopeLevel;
import com.moa.moa.global.common.entity.BaseEntity;
import jakarta.persistence.*;
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
@Table(name = "slope")
public class Slope extends BaseEntity {
    @Comment("장소")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Place place;

    @Comment("슬로프 이름")
    @Column(name = "name", columnDefinition = "VARCHAR(50)")
    private String name;

    @Comment("슬로프 난이도")
    @Column(name = "level", columnDefinition = "BIGINT")
    @Convert(converter = SlopeLevelConverter.class)
    private SlopeLevel level;
}
