package com.moa.moa.api.member.wish.domain.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record FindAllWishDto() {
    @Builder
    public record Response(
            Long id,
            LocalDateTime createdAt,
            FindAllWishDto.ShopResponse shop,
            FindAllWishDto.ImageResponse image,
            FindAllWishDto.AddressResponse address,
            FindAllWishDto.MoaReviewResponse moaReview,
            List<FindAllWishDto.PlaceResponse> places
    ) {
    }

    @Builder
    public record ShopResponse(
            Long id,
            String name
    ) {
    }

    @Builder
    public record ImageResponse(
            Long id,
            String originImageUrl,
            String lowImageUrl,
            LocalDateTime createdAt
    ) {
    }

    @Builder
    public record AddressResponse(
            Long id,
            String address,
            String addressDetail,
            Double locationX,
            Double locationY,
            String mapUrl
    ) {
    }

    @Builder
    public record MoaReviewResponse(
            Double avgScore,
            Long totalCount
    ) {
    }

    @Builder
    public record PlaceResponse(
            Long id,
            String name
    ) {
    }
}
