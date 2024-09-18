package com.moa.moa.api.shop.shop.domain.dto;

import com.moa.moa.api.member.custom.util.enumerated.ClothesType;
import com.moa.moa.api.member.custom.util.enumerated.EquipmentType;
import com.moa.moa.api.member.custom.util.enumerated.Gender;
import com.moa.moa.api.member.custom.util.enumerated.PackageType;
import com.moa.moa.api.place.place.util.enumerated.PlaceLevel;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record FindAllShopLowPriceDto() {
    @Builder
    public record Request(
            FindAllShopLowPriceDto.PlaceRequest place,
            FindAllShopLowPriceDto.ShopRequest shop,
            List<FindAllShopLowPriceDto.CustomRequest> custom
    ) {}

    public record PlaceRequest(
            @NotNull
            Long id,
            @NotNull
            LocalDate visitDate
    ) {}

    public record ShopRequest(
            @NotNull
            Boolean pickUp
    ) {}

    public record CustomRequest(
            @NotNull
            Gender gender,
            @NotNull
            String nickname,
            @NotNull
            String liftType,
            @NotNull
            String liftTime,
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
            FindAllShopLowPriceDto.PlaceResponse place,
            List<FindAllShopLowPriceDto.ShopResponse> shop
    ) {}

    @Builder
    public record PlaceResponse(
            Long id,
            String name,
            LocalDate open,
            LocalDate close,
            FindAllShopLowPriceDto.AddressResponse address,
            FindAllShopLowPriceDto.ImageResponse images,
            PlaceLevel recLevel
    ) {}

    @Builder
    public record ShopResponse(
            Long id,
            Long wishId,
            BigDecimal totalPrice,
            String memberName,
            String name,
            Boolean pickUp,
            String storeUrl,
            FindAllShopLowPriceDto.MoaReviewResponse moaReview,
            FindAllShopLowPriceDto.NaverReviewResponse naverReview,
            FindAllShopLowPriceDto.AddressResponse address,
            FindAllShopLowPriceDto.ImageResponse image,
            List<FindAllShopLowPriceDto.CustomResponse> customs
    ) {}

    @Builder
    public record ImageResponse(
            Long id,
            String keyName,
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
}
