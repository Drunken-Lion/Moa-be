package com.moa.moa.api.shop.shop.presentation.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record FindAllShopReviewExternalDto() {
    @Builder
    public record Response(
            Boolean isPhoto,
            List<FindAllShopReviewExternalDto.ReviewResponse> reviews
    ) {}

    @Builder
    public record ReviewResponse(
            Long id,
            Double score,
            String content,
            String writer,
            LocalDateTime createdAt,
            FindAllShopReviewExternalDto.ImageResponse image
    ) {}

    @Builder
    public record ImageResponse(
            Long id,
            String originImageUrl,
            String lowImageUrl,
            LocalDateTime createdAt
    ) {}
}
