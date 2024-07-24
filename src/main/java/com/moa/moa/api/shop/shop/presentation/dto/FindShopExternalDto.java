package com.moa.moa.api.shop.shop.presentation.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record FindShopExternalDto() {
    @Builder
    public record Response(
            Long id,
            Long categoryId,
            String name,
            Boolean pickUp,
            String storeUrl,
            LocalDateTime createdAt,
            Boolean isWish,
            List<FindShopExternalDto.PlaceResponse> places,
            FindShopExternalDto.ImageResponse image,
            FindShopExternalDto.AddressResponse address,
            FindShopExternalDto.MoaReviewResponse moaReview,
            FindShopExternalDto.NaverReviewResponse naverReview
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
            String originImageUrl,
            String lowImageUrl,
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
