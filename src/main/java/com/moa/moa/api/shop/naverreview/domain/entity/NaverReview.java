package com.moa.moa.api.shop.naverreview.domain.entity;

import com.moa.moa.api.shop.shop.domain.entity.Shop;
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
@Table(name = "naver_review")
public class NaverReview extends BaseEntity {
    @Comment("렌탈샵")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Shop shop;

    @Comment("평균 평점")
    @Column(name = "avg_score", columnDefinition = "DOUBLE")
    private Double avgScore;

    @Comment("총 리뷰 수")
    @Column(name = "total_review", columnDefinition = "BIGINT")
    private Long totalReview;
}
