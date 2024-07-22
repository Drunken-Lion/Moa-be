package com.moa.moa.api.shop.review.presentation.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

public record ModReviewExternalDto() {
    public record Request(
            @NotNull
            Double score,
            @NotNull
            String content,
            List<Integer> remove,
            List<String> images
    ) {}
    @Builder
    public record Response(Long id) {}
}
