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
            List<ImageResponse> images
    ) {}

    @Builder
    public record ImageResponse(
            Long id,
            String originImageUrl,
            String lowImageUrl,
            LocalDateTime createdAt) {}
}
