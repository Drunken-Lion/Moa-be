package com.moa.moa.api.member.custom.domain.dto;

import com.moa.moa.api.member.custom.util.enumerated.ClothesType;
import com.moa.moa.api.member.custom.util.enumerated.EquipmentType;
import com.moa.moa.api.member.custom.util.enumerated.Gender;
import com.moa.moa.api.member.custom.util.enumerated.PackageType;
import jakarta.validation.constraints.NotNull;

public record AddCustomDto() {
    public record Request(
            @NotNull
            Gender gender,
            @NotNull
            String nickname,
            @NotNull
            PackageType packageType,
            ClothesType clothesType,
            EquipmentType equipmentType
    ) {}

    public record Response(
            Long id
    ) {}
}
