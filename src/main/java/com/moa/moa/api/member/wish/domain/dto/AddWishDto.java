package com.moa.moa.api.member.wish.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record AddWishDto() {
    @Builder
    public record Request(
            @NotNull
            Long shopId
    ) {}

    public record Response(Long id) {}
}
