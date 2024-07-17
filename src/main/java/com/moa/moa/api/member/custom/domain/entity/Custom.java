package com.moa.moa.api.member.custom.domain.entity;

import com.moa.moa.api.member.custom.util.convert.ClothesTypeConverter;
import com.moa.moa.api.member.custom.util.convert.EquipmentTypeConverter;
import com.moa.moa.api.member.custom.util.convert.GenderConverter;
import com.moa.moa.api.member.custom.util.convert.PackageTypeConverter;
import com.moa.moa.api.member.custom.util.enumerated.ClothesType;
import com.moa.moa.api.member.custom.util.enumerated.EquipmentType;
import com.moa.moa.api.member.custom.util.enumerated.Gender;
import com.moa.moa.api.member.custom.util.enumerated.PackageType;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "custom")
public class Custom extends BaseEntity {
    @Comment("회원")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", columnDefinition = "BIGINT", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Comment("성별")
    @Column(name = "gender", columnDefinition = "BIGINT")
    @Convert(converter = GenderConverter.class)
    private Gender gender;

    @Comment("스키어 별칭")
    @Column(name = "nickname", columnDefinition = "VARCHAR(50)")
    private String nickname;

    @Comment("패키지 타입")
    @Column(name = "package_type", columnDefinition = "BIGINT")
    @Convert(converter = PackageTypeConverter.class)
    private PackageType packageType;

    @Comment("장비 타입")
    @Column(name = "equipment_type", columnDefinition = "BIGINT")
    @Convert(converter = EquipmentTypeConverter.class)
    private EquipmentType equipmentType;

    @Comment("의류 타입")
    @Column(name = "clothes_type", columnDefinition = "BIGINT")
    @Convert(converter = ClothesTypeConverter.class)
    private ClothesType clothesType;
}
