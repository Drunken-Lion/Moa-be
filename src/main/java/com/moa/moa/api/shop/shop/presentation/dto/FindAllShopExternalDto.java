package com.moa.moa.api.shop.shop.presentation.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record FindAllShopExternalDto() {
    @Builder
    public record Response(
            Long id,
            String name,
            Long wishId,
            FindAllShopExternalDto.ImageResponse image,
            List<FindAllShopExternalDto.PackageResponse> packages,
            FindAllShopExternalDto.MoaReviewResponse moaReview,
            FindAllShopExternalDto.NaverReviewResponse naverReview,
            List<FindAllShopExternalDto.PlaceResponse> places
    ) {}

    @Builder
    public record ImageResponse(
            Long id,
            String originImageUrl,
            String lowImageUrl,
            LocalDateTime createdAt
    ) {}

    @Builder
    public record PackageResponse(
            Long id,
            String type,
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
