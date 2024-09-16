package com.moa.moa.api.shop.shop.domain.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record FindAllShopReviewDto() {
    @Builder
    public record Response(
            Boolean isPhoto,
            List<FindAllShopReviewDto.ReviewResponse> reviews
    ) {}

    @Builder
    public record ReviewResponse(
            Long id,
            Double score,
            String content,
            String writer,
            LocalDateTime createdAt,
            FindAllShopReviewDto.ImageResponse image
    ) {}

    @Builder
    public record ImageResponse(
            Long id,
            String originImageUrl,
            String lowImageUrl,
            LocalDateTime createdAt
    ) {}
}
