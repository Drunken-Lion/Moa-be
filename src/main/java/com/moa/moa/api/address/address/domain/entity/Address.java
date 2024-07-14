package com.moa.moa.api.address.address.domain.entity;

import com.moa.moa.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "address")
public class Address extends BaseEntity {
    @Comment("주소")
    @Column(name = "address", columnDefinition = "VARCHAR(200)")
    private String address;

    @Comment("상세 주소")
    @Column(name = "address_detail", columnDefinition = "VARCHAR(200)")
    private String addressDetail;

    @Comment("위치값")
    @Column(name = "location", columnDefinition = "POINT")
    private Point location;

    @Comment("네이버 주소 url")
    @Column(name = "url", columnDefinition = "VARCHAR(2048)")
    private String url;
}
