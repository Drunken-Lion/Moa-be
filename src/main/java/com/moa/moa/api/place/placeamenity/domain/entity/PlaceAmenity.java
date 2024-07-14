package com.moa.moa.api.place.placeamenity.domain.entity;

import com.moa.moa.api.place.amenity.domain.entity.Amenity;
import com.moa.moa.api.place.place.domain.entity.Place;
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
@Table(name = "place_amenity")
public class PlaceAmenity extends BaseEntity {
    @Comment("장소")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Place place;

    @Comment("편의시설")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amenity_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Amenity amenity;

    @Comment("사용 여부")
    @Column(name = "use", columnDefinition = "BOOLEAN")
    private Boolean use;
}
