package com.moa.moa.api.shop.shop.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "최저가 검색 시 shop과 관련된 business_id 조회")
public class FindShopBusinessIdDto {
    @Schema(description = "렌탈샵 고유 번호", example = "1")
    Long shopId;

    @Schema(description = "렌탈샵의 시간 테이블 고유 번호", example = "2")
    Long businessId;
}
