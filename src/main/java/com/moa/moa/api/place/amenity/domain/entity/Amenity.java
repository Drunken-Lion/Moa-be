package com.moa.moa.api.place.amenity.domain.entity;

import com.moa.moa.api.place.amenity.util.convert.AmenityTypeConverter;
import com.moa.moa.api.place.amenity.util.enumerated.AmenityType;
import com.moa.moa.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "amenity")
public class Amenity extends BaseEntity {
    @Comment("편의 시설 이름")
    @Column(name = "type", columnDefinition = "BIGINT")
    @Convert(converter = AmenityTypeConverter.class)
    private AmenityType type;
}
