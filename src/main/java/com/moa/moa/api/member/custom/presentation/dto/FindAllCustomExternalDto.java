package com.moa.moa.api.member.custom.presentation.dto;

import com.moa.moa.api.member.custom.util.enumerated.ClothesType;
import com.moa.moa.api.member.custom.util.enumerated.EquipmentType;
import com.moa.moa.api.member.custom.util.enumerated.Gender;
import com.moa.moa.api.member.custom.util.enumerated.PackageType;
import lombok.Builder;

import java.time.LocalDateTime;

public record FindAllCustomExternalDto() {
    @Builder
    public record Response(
            Long id,
            Long memberId,
            Gender gender,
            String nickname,
            PackageType packageType,
            ClothesType clothesType,
            EquipmentType equipmentType,
            LocalDateTime createdAt
    ) {
    }
}
