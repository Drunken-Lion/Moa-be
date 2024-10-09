package com.moa.moa.api.shop.shop.domain.dto;

import com.moa.moa.api.member.custom.util.enumerated.ClothesType;
import com.moa.moa.api.member.custom.util.enumerated.EquipmentType;
import com.moa.moa.api.member.custom.util.enumerated.Gender;
import com.moa.moa.api.member.custom.util.enumerated.PackageType;
import com.moa.moa.api.place.place.util.enumerated.PlaceLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record FindAllShopLowPriceDto() {
    @Builder
    public record Request(
            @Valid FindAllShopLowPriceDto.PlaceRequest place,
            @Valid FindAllShopLowPriceDto.ShopRequest shop,
            @Valid List<FindAllShopLowPriceDto.CustomRequest> customs
    ) {}

    @Builder
    public record PlaceRequest(
            @NotNull
            Long id,
            @NotNull
            LocalDate visitDate
    ) {}

    @Builder
    public record ShopRequest(
            @NotNull
            Boolean pickUp
    ) {}

    @Builder
    public record CustomRequest(
            @NotNull
            Gender gender,
            String nickname,
            @NotBlank
            @Pattern(regexp = "^(시간지정권-[가-힣]{3}|스마트권)$", message = "'시간지정권-오후권' 또는 '스마트권' 형식으로 입력 가능합니다.")
            String liftType,
            @NotBlank
            @Pattern(regexp = "^(0?[1-9]|1[0-9]|2[0-4])$", message = "0부터 24까지의 숫자만 입력할 수 있습니다.")
            String liftTime,
            @NotNull
            PackageType packageType,
            ClothesType clothesType,
            EquipmentType equipmentType
    ) {}

    @Builder
    public record Response(
            LocalDate visitDate,
            FindAllShopLowPriceDto.PlaceResponse place,
            List<FindAllShopLowPriceDto.ShopResponse> shops
    ) {}

    @Builder
    public record PlaceResponse(
            Long id,
            String name,
            LocalDate open,
            LocalDate close,
            PlaceLevel recLevel,
            FindAllShopLowPriceDto.AddressResponse address,
            FindAllShopLowPriceDto.ImageResponse images
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
            FindAllShopLowPriceDto.ImageResponse images,
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
}
