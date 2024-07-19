package com.moa.moa.global.aws.s3.images.domain.entity;

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

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "aws_file")
public class Image extends BaseEntity {
    @Comment("엔티티 구분")
    @Column(name = "entity_type", columnDefinition = "VARCHAR(20)")
    private String entityType;

    @Comment("엔티티 고유 식별자")
    @Column(name = "entity_id", columnDefinition = "BIGINT")
    private Long entityId;

    @Comment("원본 이미지 URL")
    @Column(name = "origin_image_url", columnDefinition = "VARCHAR(2048)")
    private String originImageUrl;

    @Comment("저화질 이미지 URL")
    @Column(name = "low_image_url", columnDefinition = "VARCHAR(2048)")
    private String lowImageUrl;
}
