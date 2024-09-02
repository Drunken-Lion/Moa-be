package com.moa.moa.api.shop.shop.domain.dto;

import com.moa.moa.api.member.custom.util.enumerated.ClothesType;
import com.moa.moa.api.member.custom.util.enumerated.EquipmentType;
import com.moa.moa.api.member.custom.util.enumerated.Gender;
import com.moa.moa.api.member.custom.util.enumerated.PackageType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record FindAllShopLowPriceDto() {
    @Builder
    public record Request(
            PlaceRequest place,
            List<CustomRequest> custom
    ) {}

    public record PlaceRequest(
            @NotNull
            Long id,
            @NotNull
            LocalDate visitDate,
            @NotNull
            Boolean pickUp
    ) {}

    public record CustomRequest(
            @NotNull
            Gender gender,
            @NotNull
            String nickname,
            @NotNull
            PackageType packageType,
            @NotNull
            ClothesType clothesType,
            @NotNull
            EquipmentType equipmentType
    ) {}

    @Builder
    public record Response(
            LocalDate visitDate,
            PlaceResponse place,
            List<ShopResponse> shop
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
            ImageResponse image,
            List<CustomResponse> custom,
            AddressResponse address,
            MoaReviewResponse moaReview,
            NaverReviewResponse naverReview,
            WishResponse wish
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
            Gender gender,
            String nickname,
            PackageType packageType,
            ClothesType clothesType,
            EquipmentType equipmentType,
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
