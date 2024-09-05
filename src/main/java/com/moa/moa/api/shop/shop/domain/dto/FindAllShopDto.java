package com.moa.moa.api.shop.shop.domain.dto;

import com.moa.moa.api.member.custom.util.enumerated.PackageType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record FindAllShopDto() {
    @Builder
    public record Response(
            Long id,
            String name,
            Long wishId,
            FindAllShopDto.ImageResponse images,
            List<FindAllShopDto.PackageResponse> packages,
            FindAllShopDto.MoaReviewResponse moaReview,
            FindAllShopDto.NaverReviewResponse naverReview,
            List<FindAllShopDto.PlaceResponse> places
    ) {}

    @Builder
    public record ImageResponse(
            Long id,
            String keyName,
            LocalDateTime createdAt
    ) {}

    @Builder
    public record PackageResponse(
            Long id,
            PackageType type,
            String name,
            BigDecimal price
    ) {}

    @Builder
    public record MoaReviewResponse(
            Double avgScore,
            Long totalCount
    ) {}

    @Builder
    public record NaverReviewResponse(
            Double avgScore,
            Long totalCount
    ) {}

    @Builder
    public record PlaceResponse(
            Long id,
            String name
    ) {}
}
