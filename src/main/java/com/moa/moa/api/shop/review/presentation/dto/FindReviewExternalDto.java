package com.moa.moa.api.shop.review.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public record FindReviewExternalDto() {
    @Builder
    public record Response(
            Long id,
            Double score,
            String content,
            String writer,
            LocalDateTime createdAt,

            ShopResponse shop,
            List<ImageResponse> images
    ) {}

    @Builder
    public record ShopResponse(
            Long id,
            String name) {}

    @Builder
    public record ImageResponse(
            Long id,
            String originImageUrl,
            String lowImageUrl,
            LocalDateTime createdAt) {}
}
