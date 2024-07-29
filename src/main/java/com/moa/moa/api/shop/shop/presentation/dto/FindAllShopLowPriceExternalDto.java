package com.moa.moa.api.shop.shop.presentation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record FindAllShopLowPriceExternalDto() {
    @Builder
    public record Request(
            FindAllShopLowPriceExternalDto.PlaceRequest place,
            List<FindAllShopLowPriceExternalDto.CustomRequest> custom
    ) {}

    @Builder
    public record PlaceRequest(
            @NotNull
            Long id,
            @NotNull
            LocalDate date,
            @NotNull
            Boolean pickUp
    ) {}

    @Builder
    public record CustomRequest(
            @NotNull
            String gender,
            @NotNull
            String nickname,
            @NotNull
            String packageType,
            @NotNull
            String clothesType,
            @NotNull
            String equipmentType
    ) {}

    @Builder
    public record Response(
            LocalDate rsvDate,
            FindAllShopLowPriceExternalDto.PlaceResponse place,
            List<FindAllShopLowPriceExternalDto.ShopResponse> shop
    ) {}

    @Builder
    public record PlaceResponse(
            Long id,
            Long categoryId,
            Long addressId,
            String name,
            LocalDate open,
            LocalDate close,
            String recLevel
    ) {}

    @Builder
    public record ShopResponse(
            Long id,
            Long memberId,
            Long categoryId,
            String name,
            Boolean pickUp,
            String storeUrl,
            BigDecimal totalPrice,
            FindAllShopLowPriceExternalDto.ImageResponse image,
            List<FindAllShopLowPriceExternalDto.CustomResponse> custom,
            FindAllShopLowPriceExternalDto.AddressResponse address,
            FindAllShopLowPriceExternalDto.MoaReviewResponse moaReview,
            FindAllShopLowPriceExternalDto.NaverReviewResponse naverReview,
            FindAllShopLowPriceExternalDto.WishResponse wish
    ) {}

    @Builder
    public record ImageResponse(
            Long id,
            String originImageUrl,
            String lowImageUrl,
            LocalDateTime createdAt
    ) {}

    @Builder
    public record CustomResponse(
            String gender,
            String nickname,
            String packageType,
            String clothesType,
            String equipmentType,
            BigDecimal price
    ) {}

    @Builder
    public record AddressResponse(
            Long id,
            String address,
            String addressDetail,
            Double locationX,
            Double locationY,
            String mapUrl
    ) {}

    @Builder
    public record MoaReviewResponse(
            Double avgScore,
            Long totalCount
    ) {}

    @Builder
    public record NaverReviewResponse(
            Double avgScore,
            Long totalCount
    ) {}

    @Builder
    public record WishResponse(
            Long id
    ) {}
}
