package com.moa.moa.api.shop.review.presentation.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record AddReviewExternalDto() {
    public record Request(
            @NotNull
            Long shopId,
            @NotNull
            Double score,
            @NotNull
            String content,
            List<String> images
    ) {}

    public record Response(Long id) {}
}
