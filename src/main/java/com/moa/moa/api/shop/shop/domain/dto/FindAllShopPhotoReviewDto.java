package com.moa.moa.api.shop.shop.domain.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public record FindAllShopPhotoReviewDto() {
    @Builder
    public record Response(
            Long reviewId,
            FindAllShopPhotoReviewDto.ImageResponse image
    ) {}

    @Builder
    public record ImageResponse(
            Long id,
            String originImageUrl,
            String lowImageUrl,
            LocalDateTime createdAt
    ) {}
}
