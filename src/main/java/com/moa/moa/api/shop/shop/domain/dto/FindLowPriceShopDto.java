package com.moa.moa.api.shop.shop.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "최저가 검색 시 shop과 관련된 데이터 조회")
public class FindLowPriceShopDto {
    @Schema(description = "렌탈샵 고유 번호", example = "1")
    Long shopId;

    @Schema(description = "렌탈샵 이름", example = "찐렌탈샵")
    String name;

    @Schema(description = "렌탈샵 픽업 여부", example = "true")
    Boolean pickUp;

    @Schema(description = "렌탈샵 url", example = "https://smartstore.naver.com/jjinrental/products/605289690b")
    String storeUrl;

    @Schema(description = "렌탈샵에 따른 커스텀들의 가격", example = "키-닉네임, 값-87000.000")
    private Map<String, BigDecimal> customPrices = new HashMap<>();

    @Schema(description = "렌탈샵에서 조회한 커스텀들 총 가격", example = "200000.000")
    private BigDecimal totalPrice;

    @Schema(description = "렌탈샵 리뷰 평점 [모아]", example = "2.5")
    Double avgScore;

    @Schema(description = "렌탈샵 리뷰 개수 [모아]", example = "40")
    Long totalCount;

    @Schema(description = "렌탈샵 리뷰 평점 [네이버]", example = "3.5")
    Double nrAvgScore;

    @Schema(description = "렌탈샵 리뷰 개수 [네이버]", example = "300")
    Long nrTotalCount;
}
