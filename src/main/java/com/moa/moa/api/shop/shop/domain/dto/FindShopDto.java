package com.moa.moa.api.shop.shop.domain.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record FindShopDto() {
    @Builder
    public record Response(
            Long id,
            Long categoryId,
            String name,
            Boolean pickUp,
            String storeUrl,
            LocalDateTime createdAt,
            Long wishId,
            List<FindShopDto.PlaceResponse> places,
            FindShopDto.ImageResponse images,
            FindShopDto.AddressResponse address,
            FindShopDto.MoaReviewResponse moaReview,
            FindShopDto.NaverReviewResponse naverReview
    ) {}

    @Builder
    public record PlaceResponse(
            Long id,
            String name,
            LocalDate open,
            LocalDate close
    ) {}

    @Builder
    public record ImageResponse(
            Long id,
            String keyName,
            LocalDateTime createdAt
    ) {}

    @Builder
    public record AddressResponse(
            Long id,
            String address,
            String addressDetail,
            Double locationX,
            Double locationY,
            String mapUrl
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
}
