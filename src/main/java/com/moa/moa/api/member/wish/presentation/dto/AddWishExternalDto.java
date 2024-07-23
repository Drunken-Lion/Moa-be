package com.moa.moa.api.member.wish.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record AddWishExternalDto() {
    public record Request(
            @NotNull
            Long shopId
    ) {}

    public record Response(Long id) {}
}
