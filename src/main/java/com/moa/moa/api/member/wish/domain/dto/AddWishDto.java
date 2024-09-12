package com.moa.moa.api.member.wish.domain.dto;

import jakarta.validation.constraints.NotNull;

public record AddWishDto() {
    public record Request(
            @NotNull
            Long shopId
    ) {}

    public record Response(Long id) {}
}
