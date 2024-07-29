package com.moa.moa.api.member.member.presentation.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public record FindMemberExternalDto() {
    @Builder
    public record Response(
            Long id,
            String email,
            String nickname,
            LocalDateTime createdAt
    ) {
    }
}
