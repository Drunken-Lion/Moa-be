package com.moa.moa.api.member.custom.domain.dto;

import com.moa.moa.api.member.custom.util.enumerated.ClothesType;
import com.moa.moa.api.member.custom.util.enumerated.EquipmentType;
import com.moa.moa.api.member.custom.util.enumerated.Gender;
import com.moa.moa.api.member.custom.util.enumerated.PackageType;
import com.moa.moa.api.shop.itemoption.util.enumerated.ItemOptionName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Schema(description = "최저가 검색 시에 필요한 커스텀 유저 정보")
public class FindLowPriceCustomDto {
    @Schema(description = "스키어 성별", example = "MAIL")
    Gender gender;

    @Schema(description = "스키어 별칭", example = "nickname")
    String nickname;

    @Schema(description = "리프트 타입", example = "스마트권 / 시간지정권-오후권")
    String liftType;

    @Schema(description = "리프트 시간", example = "5")
    String liftTime;

    @Schema(description = "아이템 이름", example = "주중 스마트4시간권+장비+의류")
    String itemName;

    @Schema(description = "패키지 타입", example = "LIFT_EQUIPMENT_CLOTHES")
    PackageType packageType;

    @Schema(description = "의류 타입", example = "STANDARD")
    ClothesType clothesType;

    @Schema(description = "장비 타입", example = "SKI")
    EquipmentType equipmentType;

    @Schema(description = "의류, 장비 타입을 의류, 장비 대여 타입으로 변경")
    List<ItemOptionName> itemOptionNames;
}
