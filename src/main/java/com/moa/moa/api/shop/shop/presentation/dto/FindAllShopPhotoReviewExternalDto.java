package com.moa.moa.api.shop.shop.presentation.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public record FindAllShopPhotoReviewExternalDto() {
    @Builder
    public record Response(
            Long reviewId,
            FindAllShopPhotoReviewExternalDto.ImageResponse image
    ) {}

    @Builder
    public record ImageResponse(
            Long id,
            String originImageUrl,
            String lowImageUrl,
            LocalDateTime createdAt
    ) {}
}
